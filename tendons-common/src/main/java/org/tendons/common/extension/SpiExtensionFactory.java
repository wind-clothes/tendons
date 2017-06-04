package org.tendons.common.extension;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年6月4日 下午10:32:08
 */
public class SpiExtensionFactory implements ExtensionFactory {

  @Override
  public <T> T getExtension(Class<T> claz, String name) {
    if (claz.isInterface() && claz.isAnnotationPresent(SPI.class)) {
      final ExtensionLoader<T> loader = ExtensionLoader.getExtensionLoader(claz);
      return loader.getExtension(name);
    }
    return null;
  }

}
