package net.madand.conferences.db.web;

import net.madand.conferences.db.util.QueryBuilder;

public class Pagination {
    private int itemsPerPage;
    private int totalItemsCount;

    private int currentPage;
    private int totalPages;

    public Pagination(int currentPage, int itemsPerPage) {
        this.currentPage = currentPage;
        this.itemsPerPage = itemsPerPage;
    }

    public void applyTo(QueryBuilder queryBuilder) {
        queryBuilder.limitAndOffset(itemsPerPage, itemsPerPage * (currentPage - 1));
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public int getTotalItemsCount() {
        return totalItemsCount;
    }

    public void setTotalItemsCount(int totalItemsCount) {
        this.totalItemsCount = totalItemsCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        if (totalPages == 0) {
            totalPages = (int) Math.ceil((double) totalItemsCount / itemsPerPage);
        }
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
