package com.jcs.javacommunitysite.pages.searchpage;

public class SearchForm {
    private String query = "";
    private String status = "";
    private String sortBy = "";
    private String sortDir = "";

    public SearchForm() { }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query != null ? query.trim() : "";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortDir() {
        return sortDir;
    }

    public void setSortDir(String sortDir) {
        this.sortDir = sortDir;
    }
}