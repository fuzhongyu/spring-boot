package com.fzy.core.interceptor;

import com.fzy.core.entity.system.Log;
import com.fzy.core.filter.BodyReaderFilter;
import com.fzy.core.service.system.LogService;
import com.fzy.core.util.DateUtil;
import com.fzy.core.util.ParamUtil;
import com.fzy.core.util.StringUtil;
import com.fzy.core.util.thread.ExcutorProcessPool;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 日志拦截器
 * @author Fucai
 * @date 2018/3/19
 */
public class LogInterceptor implements HandlerInterceptor {

	private final static Logger logger= LoggerFactory.getLogger(LogInterceptor.class);

//	private LogService logService= SpringContextHolder.getBean(LogService.class);
	@Autowired
	private LogService logService;

	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) throws Exception {
		logger.debug("开始计时: {}  URI: {}  请求参数:{}", new SimpleDateFormat("hh:mm:ss.SSS")
				.format(ParamUtil.LongParam(BodyReaderFilter.getValue4MyThreadLocal("requestBeginTime"))), request.getRequestURI(),ParamUtil.StringParam(BodyReaderFilter.getValue4MyThreadLocal("requestParamters")));
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
			Object handler, Exception ex) throws Exception {

		if ("OPTIONS".equalsIgnoreCase(request.getMethod())){
			return;
		}

		Log log = new Log();
		log.setType(ex == null ? Log.TYPE_ACCESS : Log.TYPE_EXCEPTION);
		log.setRemoteAddr(StringUtil.getRemoteAddr(request));
		log.setUserAgent(request.getHeader("user-agent"));
		log.setRequestUri(request.getRequestURI());
		log.setParams(ParamUtil.StringParam(BodyReaderFilter.getValue4MyThreadLocal("requestParamters")));
		log.setMethod(request.getMethod());
		//异步保存日志
		ExcutorProcessPool.getInstance().excute(new SaveLogThread(log));

		//得到线程绑定的局部变量（开始时间）
			Long beginTime = ParamUtil.LongParam(BodyReaderFilter.getValue4MyThreadLocal("requestBeginTime"));
			//获得返回参数
			String responseParamsString=ParamUtil.StringParam(BodyReaderFilter.getValue4MyThreadLocal("responseParamters"));
		//2、结束时间
			Long endTime = System.currentTimeMillis();
		// 打印JVM信息。
			logger.debug("计时结束：{}  耗时：{}  URI: {}  最大内存: {}m  已分配内存: {}m  已分配内存中的剩余空间: {}m  最大可用内存: {}m  返回参数: {} ",
					new SimpleDateFormat("hh:mm:ss.SSS").format(endTime), DateUtil
							.formatDateTime(endTime - beginTime),
			request.getRequestURI(), Runtime.getRuntime().maxMemory()/1024/1024, Runtime.getRuntime().totalMemory()/1024/1024, Runtime.getRuntime().freeMemory()/1024/1024,
			(Runtime.getRuntime().maxMemory()-Runtime.getRuntime().totalMemory()+Runtime.getRuntime().freeMemory())/1024/1024,responseParamsString==null?"":responseParamsString);

	}


	/**
	 * 保存日志线程
	 */
	private class SaveLogThread implements Runnable{

		private Log log;

		public SaveLogThread(Log log){
			this.log = log;
		}

		@Override
		public void run() {
			// 保存日志信息
			logService.save(log);
		}
	}

}
