package com.fzy.core.vo.report;

import lombok.Getter;
import lombok.Setter;

/**
 * 类型配置vo
 * @author: fucai
 * @Date: 2018/10/10
 */
@Getter
@Setter
public class TypeConfigVo {

    /**
     * 类型值
     */
    private String type;

    /**
     * 类型代表的含义
     */
    private String value;

    public TypeConfigVo(){}

    public TypeConfigVo(String type,String value){
        this.type=type;
        this.value=value;
    }
}
