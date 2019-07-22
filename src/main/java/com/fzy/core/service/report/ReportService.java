package com.fzy.core.service.report;

import com.fzy.core.base.ServiceException;
import com.fzy.core.config.ErrorsMsg;
import com.fzy.core.entity.report.ReportConfig;
import com.fzy.core.vo.report.ConditionItemVo;
import com.fzy.core.vo.report.ReportVo;
import com.fzy.core.vo.report.TheadVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 报表service
 * @author fucai
 * Date: 2019-07-22
 */
@Service
public class ReportService {


    @Autowired
    private ReportConfigService reportConfigService;

    @Autowired
    private ConditionItemsConfigService conditionItemsConfigService;

    @Autowired
    private TableConfigService tableConfigService;

    @Autowired
    private ExportConfigService exportConfigService;

    @Autowired
    private InstructionConfigService instructionConfigService;

    /**
     * 获取报表vo
     * @param shortUrl  短路由
     * @return
     */
    public ReportVo getReportVo(String shortUrl){
        ReportConfig reportConfig=reportConfigService.getByShortUrl(shortUrl);
        if (reportConfig==null){
            throw new ServiceException(ErrorsMsg.ERR_1002,"配置不存在");
        }

        if (reportConfig.getChartType()==null){
            throw new ServiceException(ErrorsMsg.ERR_1002,"图表类型不存在");
        }

        ReportVo vo = new ReportVo();
        //设置图表类型，1-折线图，2-表格
        vo.setReportType(reportConfig.getChartType());

        //获取菜单id
        Long menuId = reportConfig.getId();
        vo.setMenuId(menuId);
        //获取标题
        String title = reportConfig.getTitle();
        vo.setTitle(title);
        //获取条件项
        List<ConditionItemVo> conditionItemVoList = conditionItemsConfigService.getConditionItems(reportConfig.getConfig());
        vo.setConditionItems(conditionItemVoList);
        //获取介绍flag
        Boolean instructionFlag=instructionConfigService.getInstructionFlag(reportConfig.getConfig());
        if (instructionFlag){
            List<String> instructionList = instructionConfigService.findInstructionList(reportConfig.getConfig());
            vo.setInstructionList(instructionList);
        }

        //如果是表格，添加表头，配置导出按钮
        if (reportConfig.getChartType()==2){
            //导出按钮设置
            Boolean exportFlag = exportConfigService.getExportFlag(reportConfig.getConfig());
            vo.setExportFlag(exportFlag);
            //表头设置
            List<TheadVo> theadList = tableConfigService.getTheadList(reportConfig.getConfig());
            vo.setTheadList(theadList);

        }

        return vo;
    }
}
