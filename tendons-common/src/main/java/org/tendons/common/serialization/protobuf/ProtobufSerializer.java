package org.tendons.common.serialization.protobuf;

import java.util.concurrent.ConcurrentHashMap;

import org.tendons.common.serialization.DeserializeParam;
import org.tendons.common.serialization.RpcSerializer;
import org.tendons.common.serialization.contants.SerializerType;

import com.google.protobuf.Message;

/**
 * <pre>
 * ProtobufSerializer
 * </pre>
 * 
 * @author: xiongchengwei
 * @date:2017年5月17日 下午12:59:22
 */
public class ProtobufSerializer implements RpcSerializer {

  private static final ConcurrentHashMap<Class<?>, Message> messages = new ConcurrentHashMap<>();

  @Override
  public <T> byte[] serialize(T object) throws Exception {
    if (!(object instanceof Message)) {
      throw new IllegalArgumentException(
          "ProtobufSerializer serializer param object is not Message class");
    }
    return ((Message) object).toByteArray();
  }

  @Override
  public <T> T deserialize(DeserializeParam<T> param) throws Exception {
    final Message message = messages.get(param.getClazz());
    return param.getClazz().cast(message.newBuilderForType().mergeFrom(param.getBytes()).build());
  }

  public static void addMessage(Class<?> clazz, Message message) {
    messages.put(clazz, message);
  }

  @Override
  public SerializerType serializerType() {
    return SerializerType.PROTO_BUF;
  }
}
