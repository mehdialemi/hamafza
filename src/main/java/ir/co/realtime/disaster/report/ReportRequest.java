package ir.co.realtime.disaster.report;

import ir.co.realtime.disaster.common.Request;

public class ReportRequest extends Request {
    private Report.Source source;
    private boolean warning;
    private boolean recent;

    public Report.Source getSource() {
        return source;
    }

    public void setSource(Report.Source source) {
        this.source = source;
    }

    public boolean isWarning() {
        return warning;
    }

    public void setWarning(boolean warning) {
        this.warning = warning;
    }

    public boolean isRecent() {
        return recent;
    }

    public void setRecent(boolean recent) {
        this.recent = recent;
    }
}
