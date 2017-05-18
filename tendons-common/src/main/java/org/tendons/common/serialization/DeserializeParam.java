package org.tendons.common.serialization;

public class DeserializeParam<T> {
  private String className;
  private Class<T> clazz;
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

  public Class<T> getClazz() {
    return clazz;
  }

  public void setClazz(Class<T> clazz) {
    this.clazz = clazz;
  }
}