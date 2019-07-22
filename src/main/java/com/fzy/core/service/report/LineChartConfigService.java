package com.fzy.core.service.report;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fzy.core.entity.report.ReportConfig;
import com.fzy.core.util.DateUtil;
import com.fzy.core.vo.report.TypeConfigVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;

/**
 * 折线图模板serviceImpl
 * @author: fucai
 * @Date: 2018/10/18
 */
@Service
public class LineChartConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LineChartConfigService.class);


    /**
     * 折线图group
     */
    private final static String LINE_CHART_GROUP="lineChartGroup";

    /**
     * legend组 key值
     */
    private final static String LEGEND_GROUP="legendGroup";

    /**
     * 时间字段 key值 （x轴，折线图中使用）
     */
    private final static String DATE_FIELD="dateField";

    /**
     * legend字段 key值 （折线图中使用）
     */
    private final static String LEGEND_FIELD="legendField";

    /**
     * 被统计字段 key值  折线图中使用
     */
    private final static String COUNT_FIELD="countField";




    @Autowired
    private ReportConfigService reportConfigService;

    @Autowired
    private SqlPackageExecuteService sqlPackageExecuteService;


    /**
     * 数据解析为TypeConfigVo 列表
     * @param typeConfigJson
     * @return
     */
    private List<TypeConfigVo> parseObj2TypeConfigVoList(Object typeConfigJson){
        if (typeConfigJson instanceof String){
            return JSONArray.parseArray((String) typeConfigJson,TypeConfigVo.class);
        }else{
            return JSONArray.parseArray(JSONObject.toJSONString(typeConfigJson),TypeConfigVo.class);
        }
    }


    /**
     * 获取折线图
     * @param id
     * @param conditionDataStr
     * @return
     */
    public Map<String, Object>getChartLineData(Long id,String conditionDataStr) {

        Map<String, Object> chartMap = new HashMap<>();

        ReportConfig reportConfig = reportConfigService.get(id);
        if (reportConfig == null || reportConfig.getSql() == null) {
            return new HashMap<>();
        }
        //查询数据
        List<Map<String, Object>> statList = sqlPackageExecuteService.findList(reportConfig.getSql(),conditionDataStr,reportConfig.getDataSource());

        //legend值 列表
        List<String> legendValueList=new ArrayList<>();
        //legend值代表的含义 列表
        List<String> legendLabelList=new ArrayList<>();
        //获取legend列表
        getLegendList(reportConfig,statList,legendValueList,legendLabelList);
        //获取x轴数据和其在列表中索引 的对应关系map
        Map<String,Integer> xAxisNamesPositionMap=getXAxisNames(getDateField(reportConfig.getConfig()),statList);

        chartMap.put("legendList",legendLabelList);
        chartMap.put("seriesDataArray", getSeriesDataArray(getDateField(reportConfig.getConfig()),getLegendField(reportConfig.getConfig()),getCountField(reportConfig.getConfig()),statList,xAxisNamesPositionMap,legendValueList));
        chartMap.put("XAxisNames", xAxisNamesPositionMap.keySet());

        return chartMap;
    }



    /**
     * 获取legendList(除了配置中legend数据还需要添加数据中存在但配置不存在的数据legend()
     * @param reportConfig
     * @return
     */
    private void getLegendList(ReportConfig reportConfig, List<Map<String, Object>> statList,List<String> legendValueList,List<String> legendLabelList){
        List<TypeConfigVo> typeConfigVoList=getLegendConfig(reportConfig.getConfig());
        //添加除配置以外的legend
        addUndefineLegend(typeConfigVoList,getLegendField(reportConfig.getConfig()),statList);

        for (TypeConfigVo unit:typeConfigVoList){
            legendValueList.add(unit.getType());
            legendLabelList.add(unit.getValue());
        }
    }


    /**
     * 获取legend配置
     * @param config
     * @return
     */
    private List<TypeConfigVo> getLegendConfig(String config){
        Object legendGroup=getLineChartGroup(config).get(LEGEND_GROUP);
        if (legendGroup==null){
            return new ArrayList<>();
        }
        return parseObj2TypeConfigVoList(legendGroup);
    }

    /**
     * 获取legend字段（折线图中使用）
     * @param config
     * @return
     */
    private String getLegendField(String config){
        Object legendField=getLineChartGroup(config).get(LEGEND_FIELD);
        if (legendField instanceof String){
            return (String)legendField;
        }
        // TODO: 2018/10/18 legendField配置异常
        return null;
    }

    /**
     * 获取折线图配置
     * @param config
     * @return
     */
    private Map<String,Object> getLineChartGroup(String config){
        Object lineChartGroup= reportConfigService.analysisConfig(config).get(LINE_CHART_GROUP);
        if (lineChartGroup==null || !(lineChartGroup instanceof Map)){
            return new HashMap<>();
        }
        return (Map)lineChartGroup;
    }

    /**
     * 添加除配置以外的legend（配置中不存在，但数据中存在的legend）
     * @param typeConfigVoList 配置中的legend列表
     * @param legendField legend字段
     * @param statList 查询出的数据列表
     */
    private void addUndefineLegend(List<TypeConfigVo> typeConfigVoList,String legendField,List<Map<String,Object>> statList){
        Set<String> legendSet=new HashSet<>();
        for (TypeConfigVo unit:typeConfigVoList){
            legendSet.add(unit.getType());
        }
        for (Map<String,Object> unit:statList){
            String legend=String.valueOf(unit.get(legendField));
            if (legendSet.add(legend)){
                typeConfigVoList.add(new TypeConfigVo(legend,legend));
            }
        }
    }


    /**
     * 获取时间列表，并指定该时间的数据在列表中位置
     * @param statList
     * @return
     */
    private Map<String,Integer> getXAxisNames(String dateField,List<Map<String,Object>> statList){
        Map<String,Integer> returnMap=new TreeMap<>();

        for (Map<String,Object> unit:statList){
            String date=String.valueOf(unit.get(dateField));
            if (date==null){
                continue;
            }
            //注册天没有在列表中则加入到map中
            if ((!returnMap.containsKey(date))){
                returnMap.put(date,0);
            }
        }

        //为天数添加相应的位置
        int i=0;
        for (String dayStr:returnMap.keySet()){
            returnMap.put(dayStr,i++);
        }

        return returnMap;
    }

    /**
     * 获取时间字段（x轴，折线图中使用）
     * @param config
     * @return
     */
    private String getDateField(String config){
        Object dataField=getLineChartGroup(config).get(DATE_FIELD);
        if (dataField instanceof String){
            return (String)dataField;
        }
        // TODO: 2018/10/18 dataField配置异常
        return null;
    }

    /**
     * 获取被统计字段（折线图中使用）
     * @param config
     * @return
     */
    private String getCountField(String config){
        Object countField=getLineChartGroup(config).get(COUNT_FIELD);
        if (countField instanceof String){
            return (String)countField;
        }
        // TODO: 2018/10/18 dataField配置异常
        return null;
    }


    /**
     * 封装数据list
     * @param legendField legend字段名
     * @param countField 统计字段名
     * @param statList 数据列表
     * @param xAxisNamesPositionMap  时间和时间在列表中索引对应关系
     * @param legendValueList  legend值列表
     * @return
     */
    private List<List<Number>> getSeriesDataArray(String dateField,String legendField,String countField,List<Map<String,Object>> statList,Map<String,Integer> xAxisNamesPositionMap,List<String> legendValueList){

        //map(Map<legend,一组数据>) 用于数据对齐，防止中间有某日期没数据
        Map<String,List<Number>> legendDataListMap=initLegendDataListMap(xAxisNamesPositionMap.size(),legendValueList);

        for (Map<String,Object> unit:statList){
            //获取数据legend类型
            String legend=String.valueOf(unit.get(legendField));
            //获取数据时间
            String dateStr=getDateStr(unit.get(dateField));
            //获取数据值
            BigDecimal count=change2BigDeciaml(unit.get(countField));
            //因为legendDataListMap是linkedHashMap所以必须判断legend不能为null
            if (legend==null || dateStr==null){
                continue;
            }
            List<Number> dataList=legendDataListMap.get(legend);
            if (dataList==null){
                continue;
            }

            Integer datePosition=xAxisNamesPositionMap.get(dateStr);
            if (datePosition==null){
                continue;
            }
            dataList.set(datePosition,count);
        }

        return change2List(legendDataListMap);
    }


    /**
     * 初始化map(结构：Map<legend,一组数据>)
     * @return
     */
    private Map<String,List<Number>> initLegendDataListMap(Integer arraySize,List<String> legendList){
        Map<String,List<Number>> returnMap=new LinkedHashMap<>();

        for (String unit:legendList){
            returnMap.put(unit,Arrays.asList(initArray(arraySize)));
        }
        return returnMap;
    }


    /**
     * 初始化array，保证legend中一组数据个数和x轴时间个数相同
     * @param size
     * @return
     */
    private Number[] initArray(Integer size){
        if (size==null || size==0){
            return new Number[0];
        }
        Number[] returnArray=new Number[size];
        //0填充
        Arrays.fill(returnArray,0);
        return returnArray;
    }

    /**
     * 获取date字符串，这边obj可能本身就为string ,也可能为date
     * @param obj
     * @return
     */
    private String getDateStr(Object obj){
        if (obj instanceof String){
            return String.valueOf(obj);
        }else if (obj instanceof Date){
            return DateUtil.formatDate((Date) obj);
        }
        return null;
    }

    /**
     * 转换数据类型为
     * @param obj
     * @return
     */
    private BigDecimal change2BigDeciaml(Object obj){
        try {
            return new BigDecimal(String.valueOf(obj));
        }catch (NumberFormatException e){
            return new BigDecimal(0);
        }
    }

    /**
     * 将map数据转成list
     * @param dataMap
     * @return
     */
    private List<List<Number>> change2List(Map<String,List<Number>> dataMap){
        List<List<Number>> returnList=new ArrayList<>();
        for (String key:dataMap.keySet()){
            returnList.add(dataMap.get(key));
        }
        return returnList;
    }

}
