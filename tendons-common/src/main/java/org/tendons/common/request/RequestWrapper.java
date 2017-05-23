package org.tendons.common.request;

/**
 * TODO
 * 
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月21日 下午4:51:51
 */
public class RequestWrapper {

  private String clazzName;
  private String methodNmae;
  private Object[] arguments;

  public String getClazzName() {
    return clazzName;
  }

  public void setClazzName(String clazzName) {
    this.clazzName = clazzName;
  }

  public String getMethodNmae() {
    return methodNmae;
  }

  public void setMethodNmae(String methodNmae) {
    this.methodNmae = methodNmae;
  }

  public void setArguments(Object[] arguments) {
    this.arguments = arguments;
  }

  public Object[] getArguments() {
    return arguments;
  }

}
