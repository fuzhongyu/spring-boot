package com.fzy.core.entity.system;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import com.fzy.core.base.BaseEntity;

/**
 * 日志实体类
 *
 * @author Fucai
 * @date 2018/3/19
 */
@Getter
@Setter
public class Log extends BaseEntity<Log> {

	private static final long serialVersionUID = 1L;

	/**
	 * 日志类型（1：接入日志；2：错误日志）
	 */
	private String type;
	/**
	 * 日志标题
	 */
	private String title;
	/**
	 * 操作用户的IP地址
	 */
	private String remoteAddr;
	/**
	 * 操作的URI
	 */
	private String requestUri;
	/**
	 * 操作的方式
	 */
	private String method;
	/**
	 * 操作提交的数据
	 */
	private String params;
	/**
	 * 操作用户代理信息
	 */
	private String userAgent;
	/**
	 * 异常信息
	 */
	private String exception;
	/**
	 * 开始时间
	 */
	private Long beginDate;
	/**
	 * 结束时间
	 */
	private Long endDate;

	/**
	 * 日志类型（1：接入日志；2：错误日志）
	 */

	public static final String TYPE_ACCESS = "1";
	public static final String TYPE_EXCEPTION = "2";


	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}