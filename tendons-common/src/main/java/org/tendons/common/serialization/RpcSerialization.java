package org.tendons.common.serialization;

/**
 * @ClassName: RpcSerialization
 * @Description: TODO
 * @author: xiongchengwei
 * @date:2017年5月17日 下午12:51:55
 */
public interface RpcSerialization {

  byte[] serialize(Object object) throws Exception;

  Object deserialize(DeserializeParam param) throws Exception;
}
