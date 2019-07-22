package com.fzy.core.entity.report;

import com.fzy.core.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 报表配置实体
 * @author: fucai
 * @Date: 2018/9/26
 */
@Getter
@Setter
public class ReportConfig extends BaseEntity<ReportConfig> {

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 短链接
     */
    private String shortUrl;

    /**
     * 报表标题
     */
    private String title;

    /**
     * 报表类型，1-折线图,2-图表
     */
    private Integer chartType;


    /**
     * 配置选项，具体见文档
     */
    private String config;

    /**
     * 查询sql: SELECT * FROM table_name WHERE `startDate` >= #startDate# AND `endDate` <= #endDate#
     */
    private String sql;

    /**
     * 数据源
     */
    private String dataSource;
}
