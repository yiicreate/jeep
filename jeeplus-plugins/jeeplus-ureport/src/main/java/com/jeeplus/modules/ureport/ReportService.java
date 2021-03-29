package com.jeeplus.modules.ureport;

import com.bstek.ureport.provider.report.ReportFile;
import com.bstek.ureport.provider.report.file.FileReportProvider;
import com.jeeplus.modules.ureport.cache.ErasableMemoryCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    private final ErasableMemoryCache erasableMemoryCache;

    private final Logger logger = LoggerFactory.getLogger(ReportService.class);
    private final FileReportProvider fileReportProvider;

    ReportService(ErasableMemoryCache erasableMemoryCache, FileReportProvider fileReportProvider) {
        this.erasableMemoryCache = erasableMemoryCache;
        this.fileReportProvider = fileReportProvider;
    }


    List<ReportFile> getReportFiles() {
        logger.debug("ReportService.getReportFiles");
        return fileReportProvider.getReportFiles();
    }

    void deleteReport(String file) {
        logger.debug("ReportService.deleteReport, file: {}", file);
        if (file.startsWith(fileReportProvider.getPrefix())) {
            fileReportProvider.deleteReport(file);
        }else {
            logger.error("ReportService.deleteReport, error when delete: {}", file);
        }
        // fix: delete report and cache.
        if (erasableMemoryCache != null) {
            erasableMemoryCache.clearCache();
        }
    }
}
