package com.fzy.core.controller.console.report;

import com.fzy.core.base.BaseController;
import com.fzy.core.base.ResponseResult;
import com.fzy.core.config.ErrorsMsg;
import com.fzy.core.service.report.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 报表controller
 *
 * @author fucai
 * Date: 2019-07-22
 */
@RestController
@RequestMapping("${custom.console}/report")
public class ReportController extends BaseController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private TableConfigService tableConfigService;

    @Autowired
    private ExportConfigService exportConfigService;

    @Autowired
    private LineChartConfigService lineChartConfigService;

    /**
     * 路由页面
     *
     * @param shortUrl
     * @return
     */
    @PostMapping(value = "/{shortUrl}")
    public ResponseResult show(@PathVariable("shortUrl") String shortUrl) {
        return responseEntity(ErrorsMsg.SUCC_0, reportService.getReportVo(shortUrl));
    }


    /**
     * 加载折线图
     *
     * @param id
     * @param conditionDataStr 条件string
     * @return
     */
    @PostMapping(value = "/line_chart/{id}")
    public ResponseResult lineChartLoad(@PathVariable("id") Long id, @RequestParam(value = "conditionDataStr") String conditionDataStr) {
        return responseEntity(ErrorsMsg.SUCC_0, lineChartConfigService.getChartLineData(id, conditionDataStr));
    }

    /**
     * 加载图表
     *
     * @param id
     * @param pageNo
     * @param pageSize
     * @param conditionDataStr
     * @return
     */
    @PostMapping(value = "/table/{id}")
    public ResponseResult load(@PathVariable("id") Long id,
                               @RequestParam(value = "pageNo") Integer pageNo,
                               @RequestParam(value = "pageSize") Integer pageSize,
                               @RequestParam(value = "conditionDataStr") String conditionDataStr) {
        return responseEntity(ErrorsMsg.SUCC_0, tableConfigService.getPage(id, pageNo, pageSize, conditionDataStr));
    }


    /**
     * 导出数据
     *
     * @param id
     * @param conditionDataStr
     * @return
     */
    @GetMapping(value = "/table/{id}/export")
    public void export(@PathVariable("id") Long id, @RequestParam("conditionDataStr") String conditionDataStr) {
        if (conditionDataStr != null) {
            conditionDataStr = "{" + conditionDataStr + "}";
        }
        exportConfigService.exportStatList(id, conditionDataStr);
    }
}
