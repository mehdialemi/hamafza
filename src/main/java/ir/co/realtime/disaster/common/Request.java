package ir.co.realtime.disaster.common;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class Request {
    private int pageNumber;
    private int pageSize;
    private String sortField = "created_at";
    private String order = "DESC";

    public int getPageNumber() {
        return pageNumber;
    }


    public Pageable createPageabel() {
        if (pageNumber < 0 || pageSize < 1) {
            return null;
        }

        Sort.Direction direction = order.equals("ASC") ? Sort.Direction.ASC  : Sort.Direction.DESC;

        return PageRequest.of(this.getPageNumber() - 1,
                this.getPageSize(), direction, this.getSortField());
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }
}
