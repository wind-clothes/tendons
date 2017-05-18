package org.tendons.common.serialization.kryo;


import org.objenesis.strategy.StdInstantiatorStrategy;
import org.tendons.common.serialization.DeserializeParam;
import org.tendons.common.serialization.RpcSerializer;
import org.tendons.common.serialization.contants.Contants;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * @author: xiongchengwei
 * @date:2017年5月17日 下午1:05:37
 */
public class KryoSerializer<T> implements RpcSerializer<T> {

  private final static ThreadLocal<Kryo> kryos = new ThreadLocal<Kryo>() {
    protected Kryo initialValue() {
      final Kryo kryo = new Kryo();
      /**
       * 如果为true，则在遇到未注册的类时抛出异常。 默认值为false。 如果为false，当遇到未注册的类时，
       * 其完全限定的类名称将被序列化，并且使用默认序列化器对对象进行序列化，同一对象的类的后续出现将被序列化为int id 当你设置为true您可以使用
       * <p>
       * kryo.register(type, serializer, id)
       * </p>
       * 方法来设置 所有类型数据在序列化时期对应的序列化器和指定的ID
       * 
       */
      kryo.setRegistrationRequired(false);
      kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
      /**
       * 要注意的是关掉了对存在循环引用的类型的支持, 如果一定要序列化/反序列化循环引用的类型, 可以通过 kryo.addDefaultSerializer(type,
       * serializer) 设置该类型使用Java的序列化/反序列化机制,
       * <p>
       * kryo.addDefaultSerializer(type, serializer)
       * </p>
       * 
       */
      kryo.setReferences(false);
      return kryo;
    };
  };

  @Override
  public byte[] serialize(T object) {
    final Kryo kryo = buildNewKryo();
    try (final Output output = new Output(Contants.SERIALIZER_BUFF_MAX_SIZE);) {
      kryo.writeObject(output, object);
      return output.toBytes();
    }
  }

  @Override
  public T deserialize(DeserializeParam<T> param) {
    final Kryo kryo = buildNewKryo();
    try (final Input input = new Input(param.getBytes())) {
      return kryo.readObject(input, param.getClazz());
    }
  }

  private Kryo buildNewKryo() {
    return kryos.get();
  }
}
