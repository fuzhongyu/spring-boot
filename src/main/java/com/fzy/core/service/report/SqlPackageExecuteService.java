package com.fzy.core.service.report;

import com.alibaba.fastjson.JSONObject;
import com.fzy.core.base.BaseService;
import com.fzy.core.base.PageEntity;
import com.fzy.core.base.ServiceException;
import com.fzy.core.config.CustomConfigProperties;
import com.fzy.core.config.ErrorsMsg;
import com.fzy.core.dao.report.ReportConfigDao;
import com.fzy.core.entity.report.ReportConfig;
import com.fzy.core.util.RagularUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.regex.Matcher;

/**
 * sql 封装执行serviceImpl
 * @author: fucai
 * @Date: 2018/10/18
 */
@Service
public class SqlPackageExecuteService extends BaseService<ReportConfigDao,ReportConfig> {


    /**
     * 全部字段，即当传的是""值的时候，不对该字段做条件（也就是查该字段的所有数据）
     */
    private final static String ALL="";


    @Autowired
    private ReportConfigDao reportConfigDao;

    /**
     * 获取list
     * @param sql sql语句
     * @param conditionDataJsonStr  条件数据Json String
     * @param dataSource 数据源
     * @return
     */
    public List<Map<String, Object>> findList(String sql, String conditionDataJsonStr,String dataSource) {
        //生成实际执行的sql
        String actuallyExecuteSql = buildSql(sql,JSONObject.parseObject(conditionDataJsonStr==null?"{}":conditionDataJsonStr));
        //执行sql
        return (List<Map<String, Object>>) dao.customSelect(actuallyExecuteSql);
    }



    /**
     * 获取一条数据
     * @param sql sql语句
     * @param conditionDataJsonStr 条件数据Json String
     * @param dataSource 数据源
     * @return
     */
    public Map<String, Object> findOne(String sql, String conditionDataJsonStr,String dataSource) {
        //生成实际执行的sql
        String actuallyExecuteSql = buildSql(sql,JSONObject.parseObject(conditionDataJsonStr==null?"{}":conditionDataJsonStr));
        //执行sql
        return (Map<String, Object>) reportConfigDao.customSelect(actuallyExecuteSql);
    }

    /**
     * 获取单个查询数据中的value值
     * @param sql
     * @param conditionDataJsonStr
     * @param dataSource 数据源
     * @return
     */
    public Object findOneValue(String sql,String conditionDataJsonStr,String dataSource){
        Map<String,Object> obj=findOne(sql,conditionDataJsonStr,dataSource);
        if (obj.size()==0){
            return null;
        }
        String key=obj.keySet().iterator().next();
        return obj.get(key);
    }


    /**
     * 获取分页数据
     * @param sql  sql语句
     * @param conditionDataJsonStr 条件数据Json String
     * @param pageNo
     * @param pageSize   获取条数
     * @param dataSource 数据源
     * @return
     */
    public PageEntity<Map<String,Object>> findPage(String sql, String conditionDataJsonStr, Integer pageNo, Integer pageSize, String dataSource){

        //设置默认页码和页数
        if (pageNo == null || pageSize == null) {
            pageNo = CustomConfigProperties.PAGE_NO;
            pageSize = CustomConfigProperties.PAGE_SIZE;
        }

        //生成实际执行的sql
        String actuallyExecuteSql = buildSql(sql,JSONObject.parseObject(conditionDataJsonStr==null?"{}":conditionDataJsonStr));

        Page<Map<String,Object>> page = PageHelper.startPage(pageNo, pageSize);
        //查询数据
        List<Map<String, Object>> dataList = (List<Map<String, Object>>) dao.customSelect(actuallyExecuteSql);


        return new PageEntity<>(pageNo, pageSize, page.getTotal(), dataList);
    }




    /**
     * 将sql中的占位符替换为实际值
     * @param sql 查询语句
     * @param obj sql中的实际参数
     * @return
     */
    private String buildSql(String sql,JSONObject obj ){
        return buildSql(sql,obj,getPlaceholderSet(sql));
    }


    /**
     * 获取占位字段集合，含#号
     * @param sql
     * @return
     */
    private Set<String> getPlaceholderSet(String sql){
        Set<String> returnSet=new HashSet<>();
        //获取占位符（#字段#），含#号
        List<String> placeholderList=RagularUtil.regularFindWriteList(sql,"(#)(.*?)(\\1)");
        //获取占位符($字段$),含$号
        List<String> placeholderList2=RagularUtil.regularFindWriteList(sql,"(\\$)(.*?)(\\1)");
        placeholderList.addAll(placeholderList2);

        for (String unit:placeholderList){
            returnSet.add(unit);
        }
        return returnSet;
    }

    /**
     * 将sql中的占位符替换为实际值
     * @param sql 查询语句
     * @param obj sql中的实际参数
     * @param placeholderSet 占位字符
     * @return
     */
    private String buildSql(String sql,JSONObject obj,Set<String> placeholderSet){
        for (String unit:placeholderSet){
            //获取key的时候要切除前后的#号， 如：placeholder中存储的是#startDate#,这边切成startDate
            Object param=obj.get(unit.substring(1,unit.length()-1));
            if (param==null){
                throw new ServiceException(ErrorsMsg.ERR_1,"report配置错误，字段未定义");
            }

            List<String> list;

            String paramStr=(String)param;
            //如果是""（空字符），则去掉该条件项
            if (ALL.equals(paramStr)){
                sql=removeCondition(sql,unit);
            }else if (isNumber(paramStr)){
                //数字类型不需要加引号
                sql=sql.replaceAll(unit,paramStr);
            }else if ((list=toList(paramStr))!=null){
                //列表参数在sql 的in 中用到
                if (list.size()>0){
                    StringBuilder paramListBuilder=new StringBuilder("(");
                    for (String str:list){
                        paramListBuilder.append("\""+str+"\",");
                    }
                    //切除最后的,号以及拼接）号，最后的形式如：("1","2","3")
                    String paramList=paramListBuilder.substring(0,paramListBuilder.length()-1)+")";

                    sql=sql.replaceAll(unit,paramList);
                }else {
                    //如果list的大小是为0的，则去掉该条件项
                    sql=removeCondition(sql,unit);
                }
            }else if(unit.startsWith("$")){
                //$为正则中的特殊字符，在replace中要用Matcher.quoteReplacement()这种方式
                sql=sql.replaceAll(Matcher.quoteReplacement(unit),paramStr);
            }else {
                sql=sql.replaceAll(unit,"\""+paramStr+"\"");
            }

        }
        return sql;
    }

    /**
     * 去掉条件
     * @param sql
     * @param placeholder 占位符
     * @return
     */
    private String removeCondition(String sql,String placeholder){
        while (sql.indexOf(placeholder)>0){
            List<String> whereParamStrList=RagularUtil.regularFindWriteList(sql,"((?i) WHERE )(((?!(?i) WHERE ).)*?)("+placeholder+")");
            if (whereParamStrList.size()==0){
                return sql;
            }

            String whereParamStr=whereParamStrList.get(0);
            //判断该条件项是否是紧跟着where 还是and
            if (whereParamStr.toUpperCase().contains(" AND ")){
                List<String> and2ParamStrList=RagularUtil.regularFindWriteList(sql,"((?i) AND )(((?!(?i) AND ).)*?)("+placeholder+")");
                if (and2ParamStrList.size()==0){
                    return sql;
                }
                String and2ParamStr=and2ParamStrList.get(0);
                sql=sql.replace(and2ParamStr," ");
            }else {
                sql=sql.replace(whereParamStr," WHERE 1=1 ");
            }

        }
        return sql;
    }



    /**
     * 获取总数据条数
     * @param sql
     * @param dataSource 数据源
     * @return
     */
    private Integer getTotalCount(String sql,String dataSource){
        sql = "SELECT COUNT(1) FROM (" + sql + ") a";
        Object result=findOneValue(sql,null,dataSource);
        if (result instanceof Long){
            return (int)((long)result);
        }
        return 0;
    }

    /**
     * 判断是否是数值
     * @param obj
     * @return
     */
    private Boolean isNumber(String obj){
        return RagularUtil.regularMatching(obj,"^-?\\d+(\\.\\d+)?$");
    }

    /**
     * 将字符串转列表
     * @param obj
     * @return
     */
    private List<String> toList(String obj){
       if (RagularUtil.regularMatching(obj,"^\\[.*\\]$")){
           obj=obj.substring(1,obj.length()-1);
           if (!obj.contains(",")){
               return new ArrayList<>();
           }
           String[] array=obj.split(",");
           return Arrays.asList(array);
       }else {
           return null;
       }
    }
}
