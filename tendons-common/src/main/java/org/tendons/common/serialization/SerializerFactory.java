package org.tendons.common.serialization;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tendons.common.serialization.contants.SerializerType;

/**
 * @ClassName: SerializerFactory
 * @author: xiongchengwei
 * @date:2017年5月18日 上午11:32:08
 */
public final class SerializerFactory {

  private final static Logger LOGGER = LoggerFactory.getLogger(SerializerFactory.class);

  private final static Map<Byte, RpcSerializer> SERIALIZERS = new HashMap<>();

  static {
    // TODO
    final ServiceLoader<RpcSerializer> serializers =
        ServiceLoader.load(RpcSerializer.class, SerializerFactory.class.getClassLoader());
    for (RpcSerializer rpcSerializer : serializers) {
      SERIALIZERS.put(rpcSerializer.serializerType().value(), rpcSerializer);
    }
    LOGGER.info("load serializers is end,serializers is {}", SERIALIZERS.toString());
  }

  private SerializerFactory() {}

  public static RpcSerializer loadSerializer(SerializerType serializerType) {
    final RpcSerializer rpcSerializer = SERIALIZERS.get(serializerType.value());
    Objects.requireNonNull(rpcSerializer, "load rpcSerializer is error,rpcSerializer is not found");
    return rpcSerializer;
  }

  public static void main(String[] args) {
    System.out.println(SerializerFactory.loadSerializer(SerializerType.HESSIAN));
  }
}
