package org.tendons.core.serialization;

public interface RpcSerialization {

  /**
   * 编码
   * 
   * @param
   * @return void
   */
  void encode();

  /**
   * 解码
   * 
   * @param
   * @return void
   */
  void decode();
}
