package ir.co.realtime.disaster.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private ReportService reportService;


    @PostMapping("reports")
    public ReportResponse getReports(@Valid @RequestBody ReportRequest request) {
        if (request.isWarning()) {
            return reportService.warnings();
        }

        if (request.isRecent()) {
            return reportService.recentNews();
        }

        return reportService.reports(request);
    }

    @GetMapping("recent")
    public ReportResponse recentNews() {
        return reportService.recentNews();
    }

    @GetMapping("warning")
    public ReportResponse warnings() {
        return reportService.warnings();
    }
}
