package com.fzy.core.service.report;

import com.alibaba.fastjson.JSONObject;
import com.fzy.core.base.BaseService;
import com.fzy.core.dao.report.ReportConfigDao;
import com.fzy.core.entity.report.ReportConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Map;

/**
 * 报表模板serviceImpl
 * @author: fucai
 * @Date: 2018/9/26
 */
@Service
public class ReportConfigService extends BaseService<ReportConfigDao,ReportConfig> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportConfigService.class);


    /**
     * 根据短链接获取配置
     * @param shortUrl
     * @return
     */
    public ReportConfig getByShortUrl(String shortUrl){
        return dao.getByShortUrl(shortUrl);
    }



    /**
     * 解析config
     * @param config
     * @return
     */
    public Map<String,Object> analysisConfig(String config){
        return JSONObject.parseObject(config==null?"{}":config);
    }

}
