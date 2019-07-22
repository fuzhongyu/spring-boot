package com.fzy.core.service.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 说明模板serviceImpl
 * @author: fucai
 * @Date: 2018/11/22
 */
@Service
public class InstructionConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InstructionConfigService.class);

    /**
     * 导出模块定义
     */
    private final static String INSTRUCTION_GROUP="instructionGroup";


    @Autowired
    private ReportConfigService reportConfigService;




    /**
     * 获取是否导出选项： true-导出，false-不导出
     * @param config
     * @return
     */
    public Boolean getInstructionFlag(String config){
        String instructionGroup=getInstructionGroup(config);
        if (instructionGroup==null){
            return false;
        }
        return true;
    }

    /**
     * 获取说明
     * @return
     */
    public List<String> findInstructionList(String config) {
        String instructionGroup=getInstructionGroup(config);
        if (instructionGroup==null){
            return new ArrayList<>();
        }
        String[] arr=instructionGroup.split("<br>");
        return Arrays.asList(arr);
    }


    /**
     * 获取说明配置
     * @param config
     * @return
     */
    private String getInstructionGroup(String config){
        Object instructionGroup= reportConfigService.analysisConfig(config).get(INSTRUCTION_GROUP);
        if (instructionGroup!=null){
            return instructionGroup.toString();
        }
        return null;
    }


}
