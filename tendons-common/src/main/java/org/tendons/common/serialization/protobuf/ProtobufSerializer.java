package org.tendons.common.serialization.protobuf;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.tendons.common.serialization.DeserializeParam;
import org.tendons.common.serialization.RpcSerializer;

/**
 * @ClassName: ProtobufSerializer
 * @Description: TODO
 * @author: xiongchengwei
 * @date:2017年5月17日 下午12:59:22
 */
public class ProtobufSerializer<T> implements RpcSerializer<T> {

  private static final Map<String, Object> Map = new ConcurrentHashMap<>();

  @Override
  public byte[] serialize(T object) {
    return null;
  }

  @Override
  public T deserialize(DeserializeParam<T> param) {
    return null;
  }

}
