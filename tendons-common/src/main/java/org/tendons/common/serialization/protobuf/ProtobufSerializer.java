package org.tendons.common.serialization.protobuf;

import java.util.concurrent.ConcurrentHashMap;

import org.tendons.common.serialization.DeserializeParam;
import org.tendons.common.serialization.RpcSerializer;

import com.google.protobuf.Message;

/**
 * <pre>
 * ProtobufSerializer
 * </pre>
 * 
 * @author: xiongchengwei
 * @date:2017年5月17日 下午12:59:22
 */
public class ProtobufSerializer<T> implements RpcSerializer<T> {

  private static final ConcurrentHashMap<String, Message> messages = new ConcurrentHashMap<>();

  @Override
  public byte[] serialize(T object) throws Exception {
    if (!(object instanceof Message)) {
      throw new IllegalArgumentException(
          "ProtobufSerializer serializer param object is not Message class");
    }
    return ((Message) object).toByteArray();
  }

  @Override
  public T deserialize(DeserializeParam<T> param) throws Exception {
    // TODO KEY值
    final Message message = messages.get(param.getClassName());
    return param.getClazz().cast(message.newBuilderForType().mergeFrom(param.getBytes()).build());
  }

  public static void addMessage(String className, Message message) {
    messages.put(className, message);
  }
}
