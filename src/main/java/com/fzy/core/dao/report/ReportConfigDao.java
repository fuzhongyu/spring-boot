package com.fzy.core.dao.report;

import com.fzy.core.base.BaseDao;
import com.fzy.core.entity.report.ReportConfig;
import org.apache.ibatis.annotations.Mapper;

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
     * 自定义sql查询
     * @param sql
     * @return
     */
    Object customSelect(String sql);

}
