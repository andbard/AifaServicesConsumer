package com.andreabardella.aifaservicesconsumer;

public class FooBar {

    private SearchType type;
    private String searchHint;
    private String filterHint;

    public FooBar() {}

    public FooBar(SearchType type, String searchHint, String filterHint) {
        this.type = type;
        this.searchHint = searchHint;
        this.filterHint = filterHint;
    }

    public SearchType getSearchType() {
        return type;
    }

    public void setSearchType(SearchType type) {
        this.type = type;
    }

    public String getSearchHint() {
        return searchHint;
    }

    public void setSearchHint(String searchHint) {
        this.searchHint = searchHint;
    }

    public String getFilterHint() {
        return filterHint;
    }

    public void setFilterHint(String filterHint) {
        this.filterHint = filterHint;
    }
}
