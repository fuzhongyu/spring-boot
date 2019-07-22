package com.fzy.core.vo.report;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 报表视图类
 * @author fucai
 * Date: 2019-07-22
 */
@Getter
@Setter
@NoArgsConstructor
public class ReportVo {

    /**
     * 报表类型： 1-折线图， 2-图表
     */
    private Integer reportType;

    /**
     * 菜单id
     */
    private Long menuId;

    /**
     * 标题
     */
    private String title;


    /**
     * 配置项
     */
    private List<ConditionItemVo> conditionItems;


    /**
     * 是否有解释
     */
    private Boolean instructionFlag;


    /**
     * 解释列表
     */
    private List<String> instructionList;


    /**
     * 导出按钮
     */
    private Boolean exportFlag;


    /**
     * 表头
     */
    private List<TheadVo> theadList;



}
