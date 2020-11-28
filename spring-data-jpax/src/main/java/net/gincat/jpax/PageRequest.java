package net.gincat.jpax;

import net.gincat.jpax.annotation.QueryIgnore;
import org.springframework.data.domain.Sort;

import javax.ws.rs.DefaultValue;

/**
 * Base Page Request
 */
public class PageRequest {
    @QueryIgnore
    @DefaultValue("15")
    private int rows = 15;

    @QueryIgnore
    @DefaultValue("1")
    private int page = 1;

    @QueryIgnore
    @DefaultValue("id")
    private String sortProperty = "id";

    @QueryIgnore
    private Sort.Direction sortDirection = Sort.Direction.DESC;

    public PageRequest() {
    }

    public PageRequest(int rows, int page) {
        this.rows = rows;
        this.page = page;
    }

    public PageRequest(int rows, int page, String sortProperty, Sort.Direction sortDirection) {
        this.rows = rows;
        this.page = page;
        this.sortProperty = sortProperty;
        this.sortDirection = sortDirection;
    }

    @QueryIgnore
    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    @QueryIgnore
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @QueryIgnore
    public String getSortProperty() {
        return sortProperty;
    }

    public void setSortProperty(String sortProperty) {
        this.sortProperty = sortProperty;
    }

    @QueryIgnore
    public Sort.Direction getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(Sort.Direction sortDirection) {
        this.sortDirection = sortDirection;
    }

    public org.springframework.data.domain.PageRequest toPageable() {
        if (this.rows <= 0) {
            this.rows = 15;
        }

        if (this.page <= 0) {
            this.page = 1;
        }

        this.sortProperty = "id";
        this.sortDirection = Sort.Direction.DESC;

        return org.springframework.data.domain.PageRequest.of(this.page - 1, this.rows, this.sortDirection, this.sortProperty);

    }
}
