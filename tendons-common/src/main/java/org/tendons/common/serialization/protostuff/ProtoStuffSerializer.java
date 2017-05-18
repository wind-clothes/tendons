package org.tendons.common.serialization.protostuff;

import org.tendons.common.serialization.DeserializeParam;
import org.tendons.common.serialization.RpcSerializer;

/**
 * @author: xiongchengwei
 * @date:2017年5月18日 下午7:48:22 
 */
public class ProtoStuffSerializer<T> implements RpcSerializer<T> {

  @Override
  public byte[] serialize(T object) throws Exception {
    return null;
  }

  @Override
  public T deserialize(DeserializeParam<T> param) throws Exception {
    return null;
  }

}
