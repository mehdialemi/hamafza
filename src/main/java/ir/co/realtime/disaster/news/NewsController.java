package ir.co.realtime.disaster.news;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController("api/news/")
public class NewsController {

    @Autowired
    private NewsService newsService;


    @PostMapping("reports")
    public ReportResponse getReports(@Valid @RequestBody ReportRequest request) {
        if (request.isWarning()) {
            return newsService.warnings();
        }

        if (request.isRecent()) {
            return newsService.recentNews();
        }

        return newsService.reports(request);
    }

    @GetMapping("recent")
    public ReportResponse recentNews() {
        return newsService.recentNews();
    }

    @GetMapping("warnings")
    public ReportResponse warnings() {
        return newsService.warnings();
    }
}
