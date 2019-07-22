package com.fzy.core.service.report;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fzy.core.entity.report.ReportConfig;
import com.fzy.core.vo.report.TheadVo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * 导出模板serviceImpl
 * @author: fucai
 * @Date: 2018/10/18
 */
@Service
public class ExportConfigService{

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportConfigService.class);

    /**
     * 导出模块定义
     */
    private final static String EXPORT_GROUP="exportGroup";

    /**
     * 表头定义
     */
    private final static String THEAD_GROUP="theadGroup";

    /**
     * 导出的文件名
     */
    private final static String EXPORT_FILE_NAME="fileName";


    @Autowired
    private ReportConfigService reportConfigService;

    @Autowired
    private SqlPackageExecuteService sqlPackageExecuteService;



    /**
     * 获取是否导出选项： true-导出，false-不导出
     * @param config
     * @return
     */
    public Boolean getExportFlag(String config){
        Map<String,Object> exportGroup=getExportGroup(config);
        if (exportGroup==null || exportGroup.size()==0){
            return false;
        }
        return true;
    }


    /**
     * 导出数据
     * @param id
     * @param conditionDataStr
     */
    public void exportStatList(Long id,String conditionDataStr) {
        ReportConfig reportConfig = reportConfigService.get(id);
        if (reportConfig == null || reportConfig.getSql() == null || reportConfig.getDataSource()==null) {
            return;
        }

        //查询数据
        List<Map<String, Object>> statList = sqlPackageExecuteService.findList(reportConfig.getSql(),conditionDataStr,reportConfig.getDataSource());

        List<TheadVo> theadVoList=getTheadList(reportConfig.getConfig());
        Collections.sort(theadVoList);

        String csvName = getExportFileName(reportConfig.getConfig()) +"_"+ (new Date().getTime()) ;


        List<String> fieldList=new ArrayList<>();
        List<Object> headCells = new ArrayList<>();
        for (TheadVo unit:theadVoList){
            fieldList.add(unit.getName());
            headCells.add(unit.getThead());
        }

        if (CollectionUtils.isNotEmpty(statList)) {
            for (Map<String,Object> unit : statList) {
                List<Object> values = new ArrayList<>();
                for (Object key:fieldList){
                    values.add(unit.get(String.valueOf(key)));
                }
            }

        }

    }


    /**
     * 获取导出文件名称
     * @param config
     * @return
     */
    private String getExportFileName(String config){
        Object exportFileName=getExportGroup(config).get(EXPORT_FILE_NAME);
        if (exportFileName instanceof String){
            return (String)exportFileName;
        }
        return "";
    }


    /**
     * 获取表头
     * @param config
     * @return
     */
    private List<TheadVo> getTheadList(String config){
        Object theadGourp=getExportGroup(config).get(THEAD_GROUP);
        if (theadGourp==null){
            return new ArrayList<>();
        }
        return parseObj2TheadVoList(theadGourp);
    }


    /**
     * 获取导出分组配置
     * @param config
     * @return
     */
    private Map<String,Object> getExportGroup(String config){
        Object exportGroup= reportConfigService.analysisConfig(config).get(EXPORT_GROUP);
        if (exportGroup==null || !(exportGroup instanceof Map)){
            return new HashMap<>();
        }
        return (Map)exportGroup;
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

}
