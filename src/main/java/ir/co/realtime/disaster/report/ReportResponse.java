package ir.co.realtime.disaster.report;

import java.util.ArrayList;
import java.util.List;

public class ReportResponse {

    private List<Report> reports;
    private int total;

    public ReportResponse() {
        this(new ArrayList <>());
    }

    public ReportResponse(List<Report> reports) {
        this(reports, reports.size());
    }

    public ReportResponse(List<Report> reports, int total) {

        this.reports = reports;
        this.total = total;
    }

    public List <Report> getReports() {
        return reports;
    }

    public void setReports(List <Report> reports) {
        this.reports = reports;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
