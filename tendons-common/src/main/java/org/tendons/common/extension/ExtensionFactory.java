package org.tendons.common.extension;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月31日 下午7:21:37 
 */
@SPI
public interface ExtensionFactory {

  <T> T getExtension(Class<T> claz, String name);

}
