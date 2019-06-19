package com.fzy.core.base;

import java.util.List;

import com.fzy.core.config.CustomConfigProperties;
import com.fzy.core.config.ErrorsMsg;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;

/**
 * controller基类
 *
 * @author Fucai
 * @date 2018/3/19
 */
public abstract class BaseController {


    private static Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * 参数验证异常处理
     *
     * @param bindingResult
     */
    protected void doValidateHandler(BindingResult bindingResult) {
        List<ObjectError> objectErrorList = bindingResult.getGlobalErrors();
        StringBuilder sb = new StringBuilder();
        for (ObjectError objectError : objectErrorList) {
            sb.append(objectError.getDefaultMessage()).append(";");
        }
        List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrorList) {
            sb.append(fieldError.getDefaultMessage()).append(";");
        }
        throw new ServiceException(ErrorsMsg.ERR_1002, sb.toString());
    }


    /**
     * 参数绑定异常
     * 注：当这个Controller中任何一个方法发生异常，会被这个方法拦截到
     */
    @ResponseBody
    @ExceptionHandler()
    public ResponseResult bindException(Exception ex) {
        ResponseResult responseEntity = null;
        if (ex instanceof ServiceException) {
            ServiceException serviceException = (ServiceException) ex;
            if (serviceException.getErrCode() != null) {
                responseEntity = new ResponseResult(serviceException.getErrCode(), serviceException.getErrMsg(), null);
            } else {
                logger.error(ex.getMessage(), ex);
                responseEntity = new ResponseResult(ErrorsMsg.ERR_1);
            }
        } else if (ex instanceof UnauthorizedException) {
            //无操作权限
            responseEntity = new ResponseResult(ErrorsMsg.ERR_1005);
        } else if (ex instanceof MultipartException && ex.getMessage() != null && ex.getMessage().contains("SizeLimitExceededException")) {
            //捕获文件上传超过限制异常
            responseEntity = new ResponseResult(ErrorsMsg.ERR_1002, "文件超过最大限制" + CustomConfigProperties.MAX_FILE_SIZE + "MB", null);
        } else {
            logger.error(ex.getMessage(), ex);
            responseEntity = new ResponseResult(ErrorsMsg.ERR_1);
        }
        return responseEntity;
    }


    /**
     * 响应错误码，无返回值
     */
    protected ResponseResult responseEntity(Integer errCode) {
        return new ResponseResult(errCode);
    }

    /**
     * 有额外补充msg，无返回值
     */
    public ResponseResult responseEntity(Integer errCode, String detailMsg) {
        return new ResponseResult(errCode, detailMsg);
    }

    /**
     * 响应错误码，有返回值
     */
    public ResponseResult responseEntity(Integer errCode, Object result) {
        return new ResponseResult(errCode, result);
    }

    /**
     * 自定义响应信息，有返回值
     */
    public ResponseResult responseEntity(Integer errCode, String errMsg, Object result) {
        return new ResponseResult(errCode, errMsg, result);
    }
}
