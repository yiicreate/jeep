package com.jeeplus.modules.ureport;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.web.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
public class ReportController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(ReportController.class);
    private final ReportService reportService;
    ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/list")
    public AjaxJson getReports() {
        logger.debug("ReportController.getReports");
        return AjaxJson.success().put("reports", reportService.getReportFiles()).put("prefix", "file:");
    }

    @DeleteMapping("/delete")
    public AjaxJson delete(@RequestParam("id") String id) {
        logger.debug("ReportController.delete {}", id);
        reportService.deleteReport(id);
        return AjaxJson.success("删除报表成功!");
    }
}
