package com.fzy.core.filter;

import com.fzy.core.base.ServiceException;
import com.fzy.core.config.CustomConfigProperties;
import com.fzy.core.config.ErrorsMsg;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 拦截请求，添加自定义请求逻辑(获取请求参数)
 *
 * @author Fucai
 * @date 2018/3/20
 */
public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final static Logger logger= LoggerFactory.getLogger(BodyReaderHttpServletRequestWrapper.class);


    private byte[] body=new byte[]{};

    public BodyReaderHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        //获取请求参数值
        String requestParam=getBodyString(request);
        if (requestParam==null){
            BodyReaderFilter.addValueToMyThreadLocal("requestParamters","{}");
            return;
        }
        this.body = requestParam.getBytes(Charset.forName("utf-8"));
        //将值写入线程变量中
        try {
            String paramters=new String(body,"utf-8");
            //获取请求参数
           BodyReaderFilter.addValueToMyThreadLocal("requestParamters",paramters);

        } catch (UnsupportedEncodingException e) {
            BodyReaderFilter.addValueToMyThreadLocal("requestParamters","{}");
        }

    }

    /**
     *  继承实现getReader()重写逻辑，自定义的HttpServletRequestWrapper将原始的HttpServletRequest对象进行再次封装
     * @return
     * @throws IOException
     */
    @Override
    public BufferedReader getReader() throws IOException{
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }


    /**
     * 将body体中的字符串转换为字节流
     * @return
     * @throws IOException
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {

        final ByteArrayInputStream bais = new ByteArrayInputStream(body);

        return new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
    }


    /**
     * 获取httpServletRequest流数据方法
     * @param request
     * @return
     */
    private static String getBodyString(ServletRequest request){
        StringBuilder strReturnVal = new StringBuilder("");
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(request.getInputStream(), "UTF-8");
            while (true) {
                char[] c = new char[1024];
                int ilen = isr.read(c);
                if (ilen <= 0) {
                    break;
                }
                strReturnVal = strReturnVal.append(c, 0, ilen);
                if(strReturnVal.length() > CustomConfigProperties.MAX_CONTEXT_LENGTH){
                    throw new ServiceException(ErrorsMsg.ERR_1002,"报文超过最大限制");
                }
            }
            return strReturnVal.toString();
        } catch (Exception e) {
            logger.error("读取http请求内容出错" , e);
            return null;
        } finally {
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    logger.error("关闭request输入流出错" , e);
                }
            }
        }
    }
}
