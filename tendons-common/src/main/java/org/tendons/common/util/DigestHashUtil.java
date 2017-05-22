package org.tendons.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月21日 下午11:20:09
 */
public final class DigestHashUtil {

  private static final char[] DIGITS_LOWER =
      {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

  private static final char[] DIGITS_UPPER =
      {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

  private DigestHashUtil() {}

  public static byte[] md5(String value) {
    MessageDigest md5;
    try {
      md5 = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException(e.getMessage(), e);
    }
    md5.reset();
    byte[] bytes = null;
    try {
      bytes = value.getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new IllegalStateException(e.getMessage(), e);
    }
    md5.update(bytes);
    return md5.digest();
  }

  public static String byteArrayToHex(byte[] byteArray) {
    char[] resultCharArray = new char[byteArray.length << 1];
    // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
    int index = 0;
    for (byte b : byteArray) {
      resultCharArray[index++] = DIGITS_UPPER[b >>> 4 & 0xf];
      resultCharArray[index++] = DIGITS_UPPER[b & 0xf];
    }
    // 字符数组组合成字符串返回
    return new String(resultCharArray);
  }
}
