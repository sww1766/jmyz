package com.sso.utils;

import com.sso.utils.enumutil.SysResponseEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * @Title: 返回信息基类
 */
@ApiModel(value = "请求输出参数基类")
public class ResponseData<T> implements Serializable {

  private int code = SysResponseEnum.SUCCESS.getCode();
  private String message = SysResponseEnum.SUCCESS.getMessage();
  private T data;

  public ResponseData(T data) {
    this.data = data;
  }

  public ResponseData() {
  }

  public ResponseData(Integer code) {
    this.code = code;
    this.message = SysResponseEnum.FAILED.getMessage();
    SysResponseEnum sysResponseEnum = Arrays.stream(SysResponseEnum.values())
        .filter(e -> e.getCode() == code).findFirst().orElse(null);
    if (Objects.nonNull(sysResponseEnum)) {
      this.message = sysResponseEnum.getMessage();
    }
  }

  @ApiModelProperty(value = "此编码只有0：成功，100：失败")
  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public ResponseData(int code, String message, T data) {
    this.code = code;
    this.data = data;
    this.message = message;
  }

  public ResponseData(int code, T data) {
    this.code = code;
    this.data = data;
  }

  public ResponseData(String message, T data) {
    this.data = data;
    this.message = message;
  }

  @ApiModelProperty(value = "返回数据")
  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }


  @ApiModelProperty(value = "提示消息")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }


  public ResponseData(int code, String message) {
    this.code = code;
    this.message = message;
  }

  /**
   * 自定义的封装 含data
   */
  public ResponseData result(SysResponseEnum sysResponseEnum, T data) {
    this.code = sysResponseEnum.getCode();
    this.message = sysResponseEnum.getMessage();
    this.data = data;
    return this;
  }

  /**
   * 自定义的封装
   */
  public ResponseData result(SysResponseEnum sysResponseEnum) {
    return result(sysResponseEnum, null);
  }

  /**
   * 一般性失败的封装
   */
  public ResponseData error() {
    return result(SysResponseEnum.FAILED, null);
  }

  public ResponseData ok() {
    return result(SysResponseEnum.SUCCESS, null);
  }

  /**
   * 是否成功
   */
  public boolean checkSucess() {
    if (this.code == SysResponseEnum.SUCCESS.getCode()) {
      return true;
    }
    return false;
  }


  /**
   * @description 返回验证不通过信息
   * @author: kinyer.yang
   * @date: 2019/3/5 13:08
   */
  public ResponseData resultFailedValidation(String msg) {
    this.result(SysResponseEnum.MODEL_FAILED_VALIDATION);
    this.setMessage(this.message + ":" + msg);
    return this;
  }

  /**
   * @description 返回指定msg的数据
   * @author: kinyer.yang
   * @date: 2019/3/5 13:08
   */
  public ResponseData resultDefinMsg(String msg) {
    return resultDefinMsg(msg, null);
  }

  public ResponseData resultDefinMsg(String msg, T data) {
    this.result(SysResponseEnum.DEFINE_ERROR, data);
    this.setMessage(msg);
    return this;
  }


}
