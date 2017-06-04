package org.tendons.common.extension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tendons.common.constants.Constants;
import org.tendons.common.exception.ExtensionLoaderException;
import org.tendons.common.util.StringUtils;

/**
 * <pre>
 * 扩展增加的方式(采取了兼容模式)：
 *      支持 JDK ServiceProvider {@link ServiceLoader}
 *      支持 Tendons:spi 配置
 * </pre>
 * 
 * @author: chengweixiong@uworks.cc
 * @date: 2017年6月4日 下午8:05:49
 */
public class ExtensionLoader<T> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExtensionLoader.class);

  private static ConcurrentMap<Class<?>, ExtensionLoader<?>> extensionLoaders =
      new ConcurrentHashMap<Class<?>, ExtensionLoader<?>>();

  // =============================================================================

  // 默认每次都获得拓展点的新的实例，需要调用者注意
  private ConcurrentMap<String, Class<T>> extensionClasses = null;

  private Class<T> type;
  private volatile boolean init = false;

  private static final String SERVICE_PREFIX = "META-INF/services/";
  private ClassLoader classLoader;

  private ExtensionLoader(Class<T> type) {
    this(type, Thread.currentThread().getContextClassLoader());
  }

  private ExtensionLoader(Class<T> type, ClassLoader classLoader) {
    this.type = type;
    this.classLoader = classLoader;
    init();
    LOGGER.info("=================init is ok=================");
  }

  private void init() {
    if (!init) {
      loadExtensionClasses();
    }
  }

  public Class<T> getExtensionClass(String name) {
    return extensionClasses.get(name);
  }

  /**
   * 每次获得都是拓展点的新的实例
   * 
   * @param name
   * @return T
   */
  public T getExtension(String name) {
    if (StringUtils.isBlank(name)) {
      return null;
    }
    final Class<T> clzz = getExtensionClass(name);
    try {
      return clzz != null ? clzz.newInstance() : null;
    } catch (Exception e) {
      exceptionThrows(type, "Error when getExtension " + name, e);
    }
    return null;
  }

  public void addExtensionClass(Class<T> clz) {
    if (clz == null) {
      return;
    }
    extensionClasses.putIfAbsent(getSpiName(clz), clz);
  }

  private synchronized void loadExtensionClasses() {
    if (init) {
      return;
    }
    extensionClasses = loadExtensionClasses(SERVICE_PREFIX);
    init = true;
  }

  @SuppressWarnings("unchecked")
  public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
    checkInterfaceType(type);

    ExtensionLoader<T> loader = (ExtensionLoader<T>) extensionLoaders.get(type);

    if (loader == null) {
      loader = new ExtensionLoader<T>(type);
      extensionLoaders.putIfAbsent(type, loader);
      loader = (ExtensionLoader<T>) extensionLoaders.get(type);
    }

    return loader;
  }

  /**
   * 有些地方需要spi的所有激活的instances，所以需要能返回一个列表的方法 按照spiMeta中的sequence进行排序
   *
   * @return
   */
  public List<T> getExtensions(String key) {
    if (extensionClasses.size() == 0) {
      return Collections.emptyList();
    }

    // 如果只有一个实现，直接返回
    final List<T> exts = new ArrayList<T>(extensionClasses.size());

    // 多个实现，按优先级排序返回
    for (Map.Entry<String, Class<T>> entry : extensionClasses.entrySet()) {
      final Activation activation = entry.getValue().getAnnotation(Activation.class);
      if (StringUtils.isBlank(key)) {
        exts.add(getExtension(entry.getKey()));
      } else if (activation != null && activation.key() != null) {
        for (String k : activation.key()) {
          if (key.equals(k)) {
            exts.add(getExtension(entry.getKey()));
            break;
          }
        }
      }
    }
    Collections.sort(exts, new ActivationComparator<T>());
    return exts;
  }

  private static <T> void checkInterfaceType(Class<T> clz) {
    if (clz == null) {
      exceptionThrows(clz, "Error extension type is null");
    }
    if (!clz.isInterface()) {
      exceptionThrows(clz, "Error extension type is not interface");
    }
    if (!isSpiType(clz)) {
      exceptionThrows(clz, "Error extension type without @Spi annotation");
    }
  }

  /**
   * check extension clz
   * 
   * <pre>
   * 		1) is public class
   * 		2) contain public constructor and has not-args constructor
   * 		3) check extension clz instanceof Type.class
   * </pre>
   * 
   * @param clz
   */
  private void checkExtensionType(Class<T> clz) {
    checkClassPublic(clz);

    checkConstructorPublic(clz);

    checkClassInherit(clz);
  }

  private void checkClassInherit(Class<T> clz) {
    if (!type.isAssignableFrom(clz)) {
      exceptionThrows(clz, "Error is not instanceof " + type.getName());
    }
  }

  private void checkClassPublic(Class<T> clz) {
    if (!Modifier.isPublic(clz.getModifiers())) {
      exceptionThrows(clz, "Error is not a public class");
    }
  }

  private void checkConstructorPublic(Class<T> clz) {
    Constructor<?>[] constructors = clz.getConstructors();

    if (constructors == null || constructors.length == 0) {
      exceptionThrows(clz, "Error has no public no-args constructor");
    }

    for (Constructor<?> constructor : constructors) {
      if (Modifier.isPublic(constructor.getModifiers())
          && constructor.getParameterTypes().length == 0) {
        return;
      }
    }
    exceptionThrows(clz, "Error has no public no-args constructor");
  }

  private static <T> boolean isSpiType(Class<T> clz) {
    return clz.isAnnotationPresent(SPI.class);
  }

  /**
   * 加载可扩展服务进map中
   * 
   * @param prefix
   * @return ConcurrentMap<String,Class<T>>
   */
  private ConcurrentMap<String, Class<T>> loadExtensionClasses(String prefix) {
    final String fullName = prefix + type.getName();
    final Map<String, String> classNames = new HashMap<>();

    try {
      Enumeration<URL> urls;
      if (classLoader == null) {
        urls = ClassLoader.getSystemResources(fullName);
      } else {
        urls = classLoader.getResources(fullName);
      }

      if (urls == null || !urls.hasMoreElements()) {
        return new ConcurrentHashMap<String, Class<T>>();
      }

      while (urls.hasMoreElements()) {
        URL url = urls.nextElement();

        parseUrl(type, url, classNames);
      }
    } catch (Exception e) {
      throw new ExtensionLoaderException("ExtensionLoader loadExtensionClasses error, prefix: "
          + prefix + " type: " + type.getClass(), e);
    }

    return loadClass(classNames);
  }

  @SuppressWarnings("unchecked")
  private ConcurrentMap<String, Class<T>> loadClass(Map<String, String> classNames) {

    final ConcurrentMap<String, Class<T>> map = new ConcurrentHashMap<String, Class<T>>();

    for (Map.Entry<String, String> entry : classNames.entrySet()) {
      final String className = entry.getValue();
      try {
        Class<T> clz;
        if (classLoader == null) {
          clz = (Class<T>) Class.forName(className);
        } else {
          clz = (Class<T>) Class.forName(className, true, classLoader);
        }

        checkExtensionType(clz);

        final String spiName = getSpiName(clz);

        if (map.containsKey(spiName)) {
          exceptionThrows(clz, ":Error spiName already exist " + spiName);
        } else {
          map.put(spiName, clz);
        }
      } catch (Exception e) {
        exceptionLog(type, "Error load spi class", e);
      }
    }
    return map;
  }

  /**
   * <pre>
   * 获取扩展点的名字
   * </pre>
   * 
   * @param clz
   * @return
   */
  private String getSpiName(Class<?> clz) {
    return clz.getSimpleName();
  }

  /**
   * 解析文件地址的URL
   * 
   * @param type 扩展点的类型
   * @param url 扩展点的配置文件
   * @param classNames 扩展点服务的类名
   * @return void
   * @throws ServiceConfigurationError
   */
  private void parseUrl(Class<T> type, URL url, Map<String, String> classNames)
      throws ServiceConfigurationError {
    InputStream inputStream = null;
    BufferedReader reader = null;
    try {
      inputStream = url.openStream();
      reader = new BufferedReader(new InputStreamReader(inputStream, Constants.UTF_8));

      String line = null;
      int indexNumber = 0;

      while ((line = reader.readLine()) != null) {
        indexNumber++;
        parseLine(type, url, line, indexNumber, classNames);
      }
    } catch (Exception e) {
      exceptionLog(type, "Error reading spi configuration file", e);
    } finally {
      try {
        if (reader != null) {
          reader.close();
        }
        if (inputStream != null) {
          inputStream.close();
        }
      } catch (IOException e) {
        exceptionLog(type, "Error closing spi configuration file", e);
      }
    }
  }

  /**
   * 读取可扩展点文件中的每一行，可根据自定的文件内容格式而扩展改方法
   * 
   * @param type
   * @param url
   * @param line
   * @param lineNumber
   * @param names
   * @return void
   * @throws IOException
   * @throws ServiceConfigurationError
   */
  private void parseLine(Class<T> type, URL url, String line, int lineNumber,
      Map<String, String> names) throws IOException, ServiceConfigurationError {
    final int ci = line.indexOf('#');
    if (ci >= 0) {
      line = line.substring(0, ci);
    }

    line = line.trim();
    if (line.length() <= 0) {
      return;
    }

    if ((line.indexOf(' ') >= 0) || (line.indexOf('\t') >= 0)) {
      exceptionThrows(type, url, lineNumber, "Illegal spi configuration-file syntax");
    }

    final int equal = line.indexOf("=");
    String name = null;
    if (equal > 0) {
      name = line.substring(0, equal).trim();
      line = line.substring(equal + 1).trim();
    }
    // check 命名规则是否合法
    int cp = line.codePointAt(0);
    if (!Character.isJavaIdentifierStart(cp)) {
      exceptionThrows(type, url, lineNumber, "Illegal spi provider-class name: " + line);
    }

    final int length = line.length();
    for (int i = Character.charCount(cp); i < length; i += Character.charCount(cp)) {
      cp = line.codePointAt(i);
      if (!Character.isJavaIdentifierPart(cp) && (cp != '.')) {
        exceptionThrows(type, url, lineNumber, "Illegal spi provider-class name: " + line);
      }
    }

    if (!names.containsValue(line)) {
      names.put(name, line);
    }
  }

  private static <T> void exceptionLog(Class<T> type, String msg, Throwable cause) {
    LOGGER.error(type.getName() + ": " + msg, cause);
  }

  private static <T> void exceptionThrows(Class<T> type, String msg, Throwable cause) {
    throw new ExtensionLoaderException(type.getName() + ": " + msg, cause);
  }

  private static <T> void exceptionThrows(Class<T> type, String msg) {
    throw new ExtensionLoaderException(type.getName() + ": " + msg);
  }

  private static <T> void exceptionThrows(Class<T> type, URL url, int line, String msg)
      throws ServiceConfigurationError {
    exceptionThrows(type, url + ":" + line + ": " + msg);
  }

}
