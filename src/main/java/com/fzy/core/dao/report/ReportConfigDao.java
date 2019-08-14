package com.fzy.core.dao.report;

import com.fzy.core.base.BaseDao;
import com.fzy.core.entity.report.ReportConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 报表模板daoImpl
 * @author: fucai
 * @Date: 2018/9/26
 */
@Mapper
public interface ReportConfigDao extends BaseDao<ReportConfig> {

    /**
     * 根据url查询
     * @param shortUrl
     * @return
     */
    ReportConfig getByShortUrl(String shortUrl);

    /**
     * 自定义sql查询一个结果
     * @param sql
     * @return
     */
    Object customSelectOne(@Param("sql") String sql);

    /**
     * 自定义sql，查询list
     * @param sql
     * @return
     */
    List<Map<String,Object>> customSelectList(@Param("sql") String sql);
}
