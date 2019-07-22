package com.fzy.core.vo.report;

import lombok.Getter;
import lombok.Setter;

/**
 * 表头配置vo ,实现了Comparable接口进行sort排序
 * @author: fucai
 * @Date: 2018/10/18
 */
@Getter
@Setter
public class TheadVo implements Comparable<TheadVo>{

    /**
     * 字段名
     */
    private String name;

    /**
     * 表头名字
     */
    private String thead;

    /**
     * 排序
     */
    private Integer sort;


    public TheadVo(){}

    public TheadVo(String name, String thead, Integer sort) {
        this.name = name;
        this.thead = thead;
        this.sort = sort;
    }


    @Override
    public int compareTo(TheadVo o) {
        if (this.sort==null && o.sort==null){
            return 0;
        }else if (this.sort==null){
            return 1;
        }else if (o.sort==null){
            return -1;
        }else {
            return this.sort-o.sort;
        }
    }


}
