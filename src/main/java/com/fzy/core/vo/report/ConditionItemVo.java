package com.fzy.core.vo.report;

import lombok.Getter;
import lombok.Setter;

/**
 * 条件vo类
 * @author: fucai
 * @Date: 2018/9/26
 */
@Getter
@Setter
public class ConditionItemVo {

    /**
     * 类型：有text , time ,sql 和 list 这几种数值
     */
    private String type;


    /**
     * 字段名称
     */
    private String name;


    /**
     * 文案
     */
    private String label;

    /**
     * 值
     */
    private Object value;

    /**
     * 数据类型，在搜索框的时候指定数据类型(_string, _int,_number) ,默认为_string
     */
    private String dataType;

    /**
     * 数据源， sql类型的时候需要使用
     */
    private String dataSource;

    /**
     * 时间格式，在time的时候使用
     */
    private String dateFormat;
}
