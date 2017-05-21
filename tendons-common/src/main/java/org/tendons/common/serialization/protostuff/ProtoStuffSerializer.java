package org.tendons.common.serialization.protostuff;

import java.util.concurrent.ConcurrentHashMap;

import org.tendons.common.serialization.DeserializeParam;
import org.tendons.common.serialization.RpcSerializer;
import org.tendons.common.serialization.contants.Contants;
import org.tendons.common.serialization.contants.SerializerType;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * @author: xiongchengwei
 * @date:2017年5月18日 下午7:48:22
 */
public class ProtoStuffSerializer implements RpcSerializer {

  private final static ConcurrentHashMap<Class<?>, Schema<?>> cachedSchema =
      new ConcurrentHashMap<Class<?>, Schema<?>>();

  @Override
  @SuppressWarnings("unchecked")
  public <T> byte[] serialize(T object) throws Exception {
    final Schema<T> schema = (Schema<T>) getSchema(object.getClass());
    final LinkedBuffer buffer = LinkedBuffer.allocate(Contants.SERIALIZER_BUFF_MAX_SIZE);
    try {
      return ProtostuffIOUtil.toByteArray(object, schema, buffer);
    } finally {
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T deserialize(DeserializeParam<T> param) throws Exception {
    final Schema<T> schema = (Schema<T>) getSchema(param.getClazz());
    T message = null;
    try {
      message = param.getClazz().newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw e;
    }
    ProtostuffIOUtil.mergeFrom(param.getBytes(), message, schema);
    return message;
  }

  private static Schema<?> getSchema(Class<?> cls) {
    Schema<?> schema = cachedSchema.get(cls);
    if (schema == null) {
      final Schema<?> newSchema = RuntimeSchema.createFrom(cls);
      schema = cachedSchema.putIfAbsent(cls, newSchema);
      // TODO
      if (schema == null) {
        schema = cachedSchema.put(cls, newSchema);
      }
    }
    return schema;
  }

  @Override
  public SerializerType serializerType() {
    return SerializerType.PROTO_STUFF;
  }
}
