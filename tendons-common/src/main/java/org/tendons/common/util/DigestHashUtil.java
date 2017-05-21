package org.tendons.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月21日 下午11:20:09
 */
public final class DigestHashUtil {

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
}
