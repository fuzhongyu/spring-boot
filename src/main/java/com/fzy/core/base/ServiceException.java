package com.fzy.core.base;


/**
 * Service层公用的Exception, 从由Spring管理事务的函数中抛出时会触发事务回滚.
 * @author Fucai
 * @date 2018/3/19
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private Integer errCode;
	private String errMsg;


	public ServiceException() {
		super();
	}

	public ServiceException(Integer errCode) {
		super();
		this.errCode = errCode;
	}

	public ServiceException(Integer errCode, String message) {
		super(message);
		this.errCode = errCode;
		this.errMsg = message;
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(Integer errCode, String message, Throwable cause) {
		super(message, cause);
		this.errCode = errCode;
		this.errMsg = message;
	}
	public ServiceException(Integer errCode, Throwable cause) {
		super("", cause);
		this.errCode = errCode;
	}
	
	public Integer getErrCode() {
		return errCode;
	}

	public void setErrCode(Integer errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

}
