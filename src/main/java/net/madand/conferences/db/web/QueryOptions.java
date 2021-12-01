package net.madand.conferences.db.web;

import net.madand.conferences.db.util.QueryBuilder;

import java.util.List;

/**
 * This class provides a way to implement additional query features, such as pagination and sorting.
 */
public class QueryOptions {
    private Pagination pagination;
    private Sorting sorting;

    public QueryOptions() {}

    public QueryOptions withPagination(int currentPage, int itemsPerPage) {
        pagination = new Pagination(currentPage, itemsPerPage);
        return this;
    }

    public QueryOptions withSorting(String field, String direction, String defaultField, String defaultDirection, List<String> allowedFields) {
        sorting = new Sorting(field, direction, defaultField, defaultDirection, allowedFields);
        return this;
    }

    public void applyTo(QueryBuilder queryBuilder) {
        if (pagination != null) {
            pagination.applyTo(queryBuilder);
        }

        if (sorting != null) {
            sorting.applyTo(queryBuilder);
        }
    }

    public Pagination getPagination() {
        return pagination;
    }

    public Sorting getSorting() {
        return sorting;
    }
}
