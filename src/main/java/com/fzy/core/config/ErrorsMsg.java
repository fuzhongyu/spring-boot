package com.fzy.core.config;

import com.fzy.core.util.PropertiesLoader;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * 错误码基类
 *
 * @author Fucai
 * @date 2018/3/19
 */
public class ErrorsMsg {

    private static ErrorsMsg errorsMsg=new ErrorsMsg();

    /**
     * 存储错误码
     */
    private static Map<Integer,String> errorsMap=new HashMap<>();

    /**
     * 属性文件加载对象
     */
    private static PropertiesLoader propertiesLoader = new PropertiesLoader("errors.properties");

    /**
     * 获取当前对象实例
     */
    public static ErrorsMsg getInstance() {
        return errorsMsg;
    }


    /**
     * 获取配置
     * @param erroCode 错误码
     * @return
     */
    public static String getConfig(Integer erroCode){
        String value = errorsMap.get(erroCode);
        if (value == null) {
            try {
                value = propertiesLoader.getString(erroCode.toString());
            } catch (Exception e) {
                return StringUtils.EMPTY;
            }
            errorsMap.put(erroCode, value != null ? value : StringUtils.EMPTY);
        }
        return value;
    }

    /**
     * 操作成功
     */
    public final static Integer SUCC_0=0;
    /**
     * 系统异常
     */
    public final static Integer ERR_1=1;
    /**
     * url非法篡改
     */
    public final static Integer ERR_2=2;


    /**
     * 登录失效，请重新登录
     */
    public final static Integer ERR_1001=1001;
    /**
     * 参数异常
     */
    public final static Integer ERR_1002=1002;

    /**
     * 登录密码错误
     */
    public final static Integer ERR_1003=1003;

    /**
     * 密码修改成功，请重新登录
     */
    public final static Integer ERR_1004=1004;







}
