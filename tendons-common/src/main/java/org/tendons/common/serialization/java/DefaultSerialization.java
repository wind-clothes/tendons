package org.tendons.common.serialization.java;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Objects;

import org.tendons.common.serialization.DeserializeParam;
import org.tendons.common.serialization.RpcSerializer;
import org.tendons.common.serialization.contants.SerializerType;

/**
 * <pre>
 * 默认的Java自带的序列化协议
 * </pre>
 * 
 * @ClassName: DefaultSerialization
 * @author: xiongchengwei
 * @date:2017年5月17日 下午1:04:10
 */
public class DefaultSerialization implements RpcSerializer {

  @Override
  public <T> byte[] serialize(T object) throws Exception {
    Objects.requireNonNull(object, "DefaultSerialization serialize error: object must is not NULL");
    try (final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ObjectOutputStream objectOutputStream =
            new ObjectOutputStream(byteArrayOutputStream)) {

      objectOutputStream.writeObject(object);
      objectOutputStream.flush();
      return byteArrayOutputStream.toByteArray();
    }

  }

  @Override
  public <T> T deserialize(DeserializeParam<T> param) throws Exception {
    Objects.requireNonNull(param, "DefaultSerialization deserialize error: param must is not NULL");
    try (
        final ByteArrayInputStream byteArrayInputStream =
            new ByteArrayInputStream(param.getBytes());
        final ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {

      return param.getClazz().cast(objectInputStream.readObject());
    }
  }

  @Override
  public SerializerType serializerType() {
    return SerializerType.JAVA;
  }

}
