package com.fzy.core.service.report;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fzy.core.base.BaseService;
import com.fzy.core.util.DateUtil;
import com.fzy.core.vo.report.ConditionItemVo;
import com.fzy.core.vo.report.TypeConfigVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 条件项配置serviceImpl
 *
 * @author: fucai
 * @Date: 2018/10/18
 */
@Service
public class ConditionItemsConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConditionItemsConfigService.class);


    /**
     * 条件选项组 key值
     */
    private final static String CONDITION_GROUP = "conditionGroup";


    /**
     * 条件项，type为text时默认输入框可输入字符类型 （字符类型：_string, 整数：_int , 数值：_number）
     */
    private final static String DEFAULT_TEXT_DATA_TYPE = "_string";

    /**
     * 搜索框默认值
     */
    private final static String DEFAULT_TEXT_VALUE = "";

    /**
     * 当条件项为sql的时候，需要指定的key
     */
    private final static String SQL_CONTIDITION_KEY = "key";

    /**
     * 当条件项为sql的时候，需要指定的key代表的含义
     */
    private final static String SQL_CONDITION_VALUE = "value";


    @Autowired
    private ReportConfigService reportConfigService;


    @Autowired
    private SqlPackageExecuteService sqlPackageExecuteService;

    /**
     * 条件项类型枚举类
     */
    private enum ConditionItemTypeEnum {

        TEXT("text", "输入框"),
        TIME("time", "时间"),
        LIST("list", "列表"),
        SQL("sql", "数据库查询列表");


        private String type;
        private String value;

        ConditionItemTypeEnum(String type, String value) {
            this.type = type;
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public String getValue() {
            return value;
        }

    }


    /**
     * 获取条件项
     *
     * @param config
     * @return
     */
    public List<ConditionItemVo> getConditionItems(String config) {
        if (config == null) {
            return new ArrayList<>();
        }
        //条件项列表
        List<ConditionItemVo> conditionItemVoList = getConditionConfig(config);
        for (ConditionItemVo vo : conditionItemVoList) {
            if (ConditionItemTypeEnum.TEXT.getType().equals(vo.getType())) {
                //当type为text时，value有如下几种形式，_string:字符 ，_int:整数；_number：数值；
                if (vo.getDataType() == null) {
                    vo.setDataType(DEFAULT_TEXT_DATA_TYPE);
                } else {
                    vo.setDataType(String.valueOf(vo.getDataType()).toLowerCase());
                }

                //设置默认值
                if (vo.getValue() == null) {
                    vo.setValue(DEFAULT_TEXT_VALUE);
                }

            } else if (ConditionItemTypeEnum.TIME.getType().equals(vo.getType())) {
                //当为时间类型的时候，默认显示的日期是当天
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                //当设置了时间格式的时候使用设置的时间格式
                if (StringUtils.isNotBlank(vo.getDateFormat())) {
                    //使用设置的格式
                    sdf = new SimpleDateFormat(vo.getDateFormat());
                } else {
                    //设置默认的日期格式
                    vo.setDateFormat("yyyy-MM-dd");
                }
                if (vo.getValue() == null) {
                    vo.setValue(sdf.format(new Date()));
                } else {
                    vo.setValue(sdf.format(DateUtil.addDay(new Date(), Integer.valueOf(vo.getValue().toString()))));
                }
            } else if (ConditionItemTypeEnum.LIST.getType().equals(vo.getType())) {
                vo.setValue(parseObj2TypeConfigVoList(vo.getValue()));
            } else if (ConditionItemTypeEnum.SQL.getType().equals(vo.getType())) {
                // TODO: 2018/10/22 dataSource 不能为空
                vo.setValue(getConditionItems4Sql(String.valueOf(vo.getValue()), vo.getDataSource()));
            }
        }

        return conditionItemVoList;
    }


    /**
     * 获取条件配置
     *
     * @param config
     * @return
     */
    private List<ConditionItemVo> getConditionConfig(String config) {
        Object conditionObj = reportConfigService.analysisConfig(config).get(CONDITION_GROUP);
        if (conditionObj == null) {
            return new ArrayList<>();
        }
        return JSONArray.parseArray(JSONObject.toJSONString(conditionObj), ConditionItemVo.class);
    }


    /**
     * 数据解析为TypeConfigVo 列表
     *
     * @param typeConfigJson
     * @return
     */
    private List<TypeConfigVo> parseObj2TypeConfigVoList(Object typeConfigJson) {
        if (typeConfigJson instanceof String) {
            return JSONArray.parseArray((String) typeConfigJson, TypeConfigVo.class);
        } else {
            return JSONArray.parseArray(JSONObject.toJSONString(typeConfigJson), TypeConfigVo.class);
        }
    }


    /**
     * 从数据查询条件列表,如果是一个字段则key和value相同，如果是两个字段则第一个字段作为key，第二个字段作为value
     *
     * @param sql
     * @return
     */
    private List<TypeConfigVo> getConditionItems4Sql(String sql, String dataSource) {
        List<TypeConfigVo> returnList = new ArrayList<>();

        List<Map<String, Object>> list = sqlPackageExecuteService.findList(sql, null, dataSource);
        if (CollectionUtils.isEmpty(list)) {
            return returnList;
        }
        for (Map<String, Object> unit : list) {
            Object key = unit.get(SQL_CONTIDITION_KEY);
            Object value = unit.get(SQL_CONDITION_VALUE);
            if (key == null) {
                continue;
            }
            if (value == null) {
                //如果只有一个字段
                returnList.add(new TypeConfigVo(String.valueOf(key), String.valueOf(key)));
            } else {
                //如果有两个字段
                returnList.add(new TypeConfigVo(String.valueOf(key), String.valueOf(value)));
            }
        }
        return returnList;
    }


}
