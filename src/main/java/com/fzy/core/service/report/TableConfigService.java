package com.fzy.core.service.report;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fzy.core.base.PageEntity;
import com.fzy.core.entity.report.ReportConfig;
import com.fzy.core.vo.report.TheadVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *  表格模板serviceImpl
 * @author: fucai
 * @Date: 2018/10/18
 */
@Service
public class TableConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TableConfigService.class);

    /**
     * 表头定义
     */
    private final static String TABLE_GROUP="tableGroup";

    @Autowired
    private ReportConfigService reportConfigService;

    @Autowired
    private SqlPackageExecuteService sqlPackageExecuteService;

    /**
     * 获取表头
     * @param config
     * @return
     */
    public List<TheadVo> getTheadList(String config){
        Object tableGourp= reportConfigService.analysisConfig(config).get(TABLE_GROUP);
        if (tableGourp==null){
            return new ArrayList<>();
        }
        return parseObj2TheadVoList(tableGourp);
    }


    /**
     * 数据解析为TheadVo 列表
     * @param theadVoJson
     * @return
     */
    private List<TheadVo> parseObj2TheadVoList(Object theadVoJson){
        List<TheadVo> returnList;
        if (theadVoJson instanceof  String){
            returnList=JSONArray.parseArray((String)theadVoJson,TheadVo.class);
        }else {
            returnList=JSONArray.parseArray(JSONObject.toJSONString(theadVoJson),TheadVo.class);
        }
        //排序
        Collections.sort(returnList);

        return returnList;
    }


    /**
     * 获取表格
     * @param id
     * @param pageNo 页码
     * @param pageSize 页面大小
     * @param conditionDataStr 条件
     * @return
     */
    public PageEntity<Map<String,Object>> getPage(Long id, Integer pageNo, Integer pageSize, String conditionDataStr){
        ReportConfig reportConfig = reportConfigService.get(id);
        if (reportConfig == null || reportConfig.getSql() == null || reportConfig.getDataSource()==null) {
            return null;
        }
        return sqlPackageExecuteService.findPage(reportConfig.getSql(),conditionDataStr,pageNo,pageSize,reportConfig.getDataSource());
    }

}
