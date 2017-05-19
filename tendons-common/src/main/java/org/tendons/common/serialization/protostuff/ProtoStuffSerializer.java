package org.tendons.common.serialization.protostuff;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.tendons.common.serialization.DeserializeParam;
import org.tendons.common.serialization.RpcSerializer;
import org.tendons.common.serialization.contants.Contants;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.runtime.RuntimeSchema;

/**
 * @author: xiongchengwei
 * @date:2017年5月18日 下午7:48:22
 */
public class ProtoStuffSerializer<T> implements RpcSerializer<T> {

  private static Map<Class<?>, RuntimeSchema<?>> cachedSchema =
      new ConcurrentHashMap<Class<?>, RuntimeSchema<?>>();

  @Override
  @SuppressWarnings("unchecked")
  public byte[] serialize(T object) throws Exception {
    final RuntimeSchema<T> schema = (RuntimeSchema<T>) getSchema(object.getClass());
    final LinkedBuffer buffer = LinkedBuffer.allocate(Contants.SERIALIZER_BUFF_MAX_SIZE);
    try {
      return ProtostuffIOUtil.toByteArray(object, schema, buffer);
    } finally {
      buffer.clear();
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public T deserialize(DeserializeParam<T> param) throws Exception {
    RuntimeSchema<T> schema = (RuntimeSchema<T>) getSchema(param.getClazz());
    T message = null;
    try {
      message = param.getClazz().newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw e;
    }
    ProtostuffIOUtil.mergeFrom(param.getBytes(), message, schema);
    return message;
  }

  private static RuntimeSchema<?> getSchema(Class<?> cls) {
    RuntimeSchema<?> schema = cachedSchema.get(cls);
    if (schema == null) {
      schema = RuntimeSchema.createFrom(cls);
      if (schema != null) {
        cachedSchema.put(cls, schema);
      }
    }
    return schema;
  }
}
