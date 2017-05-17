package org.tendons.common.serialization;

public class DeserializeParam {
  private String className;
  private byte[] bytes;

  public DeserializeParam(String className, byte[] bytes) {
    this.className = className;
    this.bytes = bytes;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public byte[] getBytes() {
    return bytes;
  }

  public void setBytes(byte[] bytes) {
    this.bytes = bytes;
  }
}