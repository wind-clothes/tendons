package org.tendons.common.serialization;

/**
 * <pre>
 *  通过SPI动态的切换序列化的协议
 * </pre>
 * @author: xiongchengwei
 * @date:2017年5月17日 下午12:51:55
 */
public interface RpcSerializer<T> {

  byte[] serialize(T object) throws Exception;

  T deserialize(DeserializeParam<T> param) throws Exception;

}
