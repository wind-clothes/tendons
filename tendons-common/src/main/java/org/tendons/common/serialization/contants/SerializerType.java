package org.tendons.common.serialization.contants;

/**
 * @ClassName: SerializerType
 * @Description: TODO
 * @author: xiongchengwei
 * @date:2017年5月18日 上午10:59:40
 */
public enum SerializerType {
  PROTO_STUFF((byte) 0x01),
  HESSIAN((byte) 0x02),
  KRYO((byte) 0x03),
  JAVA((byte) 0x04),
  PROTO_BUF((byte) 0x05)// ...
  ;

  SerializerType(byte value) {
    if (0x00 < value && value < 0x10) {
      this.value = value;
    } else {
      throw new IllegalArgumentException("out of range(0x01 ~ 0x0f): " + value);
    }
  }

  private final byte value;

  public byte value() {
    return value;
  }

  public static SerializerType parse(Byte value) {
    if (value == null) {
      return null;
    }
    switch (value) {
      case 0x01: return PROTO_STUFF;
      case 0x02: return HESSIAN;
      case 0x03: return KRYO;
      case 0x04: return JAVA;
      case 0x05: return PROTO_BUF;

      default: return null;
    }
  }

  public static SerializerType getDefault() {
    return PROTO_STUFF;
  }
}
