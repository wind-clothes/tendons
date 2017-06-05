//package org.tendons.core;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.jd.jim.cli.Cluster;
//import com.jd.soulland.common.utils.BeanUtils;
//import com.jd.soulland.common.utils.DESUtils;
//import com.jd.soulland.common.utils.MD5Utils;
//import com.jd.soulland.common.utils.RSAUtils;
//import com.jd.soulland.mobile.modules.security.service.SecurityKeyService;
//
//import org.apache.commons.codec.binary.Base64;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.security.PrivateKey;
//import java.security.PublicKey;
//import java.util.Properties;
//
//@Deprecated
//public class NewMobileResponse<T> {
//
//  private static final Logger LOG = LoggerFactory.getLogger(NewMobileResponse.class);
//
//  private String status;
//
//  private String message;
//
//  private T content;
//
//  private NewMobileResponse() {}
//
//  private NewMobileResponse(String status, String message) {
//    this(status, message, null);
//  }
//
//  private NewMobileResponse(String status, String message, T content) {
//    this.status = status;
//    this.message = message;
//    this.content = content;
//  }
//
//  public static <E> NewMobileResponse<E> getSuccessResponse() {
//    return getSuccessResponse(null);
//  }
//
//  public static <E> NewMobileResponse<E> getSuccessResponse(E content) {
//    return new NewMobileResponse<E>(MobileConstant.SUCCESS, "", content);
//  }
//
//  public static <E> NewMobileResponse<?> getSuccessResponseWithDES(E content) {
//    try {
//      SecurityKeyService securityKeyService = (SecurityKeyService) BeanUtils.getBean("securityKeyService");
//      String desSecretKey = securityKeyService.getDesSecretKeyByDeviceToken(getDeviceToken());
//
//      final String sourceContent = JSON.toJSONString(content);
//      String sign = MD5Utils.MD5Encode(sourceContent);
//      JSONObject _content = JSONObject.parseObject(sourceContent);
//      _content.put("sign", sign);
//      byte[] en = DESUtils.encrypt(_content.toJSONString().getBytes(), desSecretKey);
//      return new NewMobileResponse<String>(MobileConstant.SUCCESS, "", Base64.encodeBase64String(en));
//    } catch (Exception e) {
//      LOG.error("生成DES响应结果失败！", e);
//    }
//    return null;
//  }
//
//  public static <E> NewMobileResponse<?> getSuccessResponseWithRSA(E content) {
//    try {
//      Cluster jimClient = (Cluster) BeanUtils.getBean("jimClient");
//      SecurityKeyService securityKeyService = (SecurityKeyService) BeanUtils.getBean("securityKeyService");
//
//      PrivateKey serverPrivateKey = (PrivateKey) jimClient.getObject(MobileConstant.SERVER_PRIVATE_KEY_NAME);
//      PublicKey clientPublicKey = securityKeyService.getRsaPublicKeyByDeviceToken(getDeviceToken());
//
//      final String sourceContent = JSON.toJSONString(content);
//      JSONObject _content = JSONObject.parseObject(sourceContent);
//      byte[] sign = RSAUtils.sign(_content.toString().getBytes(), serverPrivateKey);
//      _content.put("sign", Base64.encodeBase64String(sign));
//      byte[] en = RSAUtils.encrypt(_content.toString().getBytes(), clientPublicKey);
//      return new NewMobileResponse<String>(MobileConstant.SUCCESS, "", Base64.encodeBase64String(en));
//    } catch (Exception e) {
//      LOG.error("生成RSA响应结果失败！", e);
//    }
//    return null;
//  }
//
//  public static <E> NewMobileResponse<E> getFailureResponse() {
//    return getFailureResponse(MobileConstant.FAILURE);
//  }
//
//  public static <E> NewMobileResponse<E> getFailureResponse(String status) {
//    try {
//      Properties prop = new Properties();
//      prop.load(new InputStreamReader(
//          NewMobileResponse.class.getClassLoader().getResourceAsStream("error-code.properties"), "UTF-8"));
//      String message = prop.getProperty(status);
//      return new NewMobileResponse<E>(status, message);
//    } catch (IOException e) {
//      LOG.error("获取错误响应失败！", e);
//    }
//    return null;
//  }
//
//  private static String getDeviceToken() {
//    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//    MobileRequest mobileRequest = (MobileRequest) request.getAttribute("mobileRequest");
//    MobileRequestHeader header = mobileRequest.getHeader();
//    return header.getDeviceToken();
//  }
//
//  public String getStatus() {
//    return status;
//  }
//
//  public void setStatus(String status) {
//    this.status = status;
//  }
//
//  public String getMessage() {
//    return message;
//  }
//
//  public void setMessage(String message) {
//    this.message = message;
//  }
//
//  public T getContent() {
//    return content;
//  }
//
//  public void setContent(T content) {
//    this.content = content;
//  }
//
//  @Override
//  public String toString() {
//    JSONObject jsonObject = (JSONObject) JSONObject.toJSON(this);
//    return jsonObject.toString();
//  }
//}
