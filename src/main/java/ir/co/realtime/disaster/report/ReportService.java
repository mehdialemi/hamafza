package ir.co.realtime.disaster.report;

import ir.co.realtime.disaster.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepository repository;

    @Value("${report.recent.period.millis}")
    private long delay;

    @Value("${report.warning.threshold}")
    private int warningThreshold;

    @Value("${report.warning.limit}")
    private int warningLimit;

    public ReportResponse recentNews() {
        List <Report> result = repository.findRecents(Instant.now().minusMillis(delay));
        return new ReportResponse(result);
    }

    public ReportResponse warnings() {
        List <Report> result = repository.findWarnings(warningThreshold, warningLimit);
        return new ReportResponse(result);
    }

    public ReportResponse reports(ReportRequest request) {
        Pageable pagable = request.createPageabel();
        if (pagable == null)
            throw new BadRequestException("report list");

        Page <Report> reports = repository.reportBySource(request.getSource(), pagable);
        return new ReportResponse(reports.getContent(), reports.getTotalPages());
    }
}
