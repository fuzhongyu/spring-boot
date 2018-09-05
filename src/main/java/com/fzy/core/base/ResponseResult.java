package com.fzy.core.base;

import com.fzy.core.config.ErrorsMsg;
import java.util.HashMap;

/**
 * 响应实体基类
 *
 * @author Fucai
 * @date 2018/3/19
 */
public class ResponseResult {

    private static final long serialVersionUID = 1L;

    /**
     * 默认响应成功，无返回值
     */
    public ResponseResult() {
        //默认响应成功
        this.code = ErrorsMsg.SUCC_0;
        this.msg = getMsgByCode(code);
        this.result = new HashMap<>();
    }

    /**
     * 响应错误码，无返回值
     *
     * @param errCode
     */
    public ResponseResult(Integer errCode) {
        this.code = errCode;
        this.msg = getMsgByCode(code);
        this.result = new HashMap<>();
    }

    /**
     * 有额外补充msg，无返回值
     * @param errCode
     * @param errMsg
     */
    public ResponseResult(Integer errCode,String errMsg) {
        this(errCode);
        this.msg = errMsg;
    }

    /**
     * 响应错误码，有返回值
     *
     * @param errCode
     * @param result
     */
    public ResponseResult(Integer errCode, Object result) {
        this.code = errCode;
        this.msg = getMsgByCode(code);
        if (result == null) {
            this.result = new HashMap<String, Object>();
        } else {
            this.result = result;
        }
    }

    /**
     * 自定义响应信息，有返回值
     *
     * @param errCode
     * @param errMsg
     * @param result
     */
    public ResponseResult(Integer errCode, String errMsg, Object result) {
        this.code = errCode;
        if (errMsg != null) {
            this.msg = errMsg;
        } else {
            this.msg = getMsgByCode(errCode);
        }

        if (result == null) {
            this.result = new HashMap<String, Object>();
        } else {
            this.result = result;
        }
    }

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应码说明
     */
    private String msg;

    /**
     * 返回结果, 需能正确转化成json串
     */
    private Object result;


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    /**
     * 实现根据code获取错误信息
     *
     * @param errCode
     * @return
     */
    private String getMsgByCode(Integer errCode) {
        return ErrorsMsg.getConfig(errCode);
    }
}
