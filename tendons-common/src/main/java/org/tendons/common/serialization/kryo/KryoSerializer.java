package org.tendons.common.serialization.kryo;


import org.objenesis.strategy.StdInstantiatorStrategy;
import org.tendons.common.serialization.DeserializeParam;
import org.tendons.common.serialization.RpcSerializer;
import org.tendons.common.serialization.contants.Contants;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * @ClassName: KryoSerialization
 * @Description: TODO
 * @author: xiongchengwei
 * @date:2017年5月17日 下午1:05:37
 */
public class KryoSerializer<T> implements RpcSerializer<T> {

  @Override
  public byte[] serialize(T object) {
    final Kryo kryo = buildNewKryo();
    try(final Output output = new Output(Contants.SERIALIZER_BUFF_MAX_SIZE);){
      kryo.writeObject(output, object);
      return output.toBytes();
    }
  }

  @Override
  public T deserialize(DeserializeParam<T> param) {
    final Kryo kryo = buildNewKryo();
    try(final Input input = new Input(param.getBytes())){
      return kryo.readObject(input, param.getClazz());
    }
  }

  private Kryo buildNewKryo() {
    final Kryo kryo = new Kryo();
    kryo.setReferences(false);
    kryo.setRegistrationRequired(false);
    kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
    return kryo;
  }
}
