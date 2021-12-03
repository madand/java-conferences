package net.madand.conferences.web.util;

import net.madand.conferences.db.web.QueryOptions;
import net.madand.conferences.db.web.Sorting;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PaginationSortingSupport {
    private static final int ITEMS_PER_PAGE_DEFAULT = 2;

    private boolean hasPagination = false;
    private boolean hasSorting = false;

    // Pagination parameters.
    private String itemsPerPageSessionKey;
    private int itemsPerPageDefault;

    // Sorting parameters.
    private List<String> sortableFields;
    private String sortDirectionDefault = Sorting.DESC;

    public PaginationSortingSupport withPagination(String sessionKey) {
        return withPagination(sessionKey, ITEMS_PER_PAGE_DEFAULT);
    }

    public PaginationSortingSupport withPagination(String sessionKey, int itemsPerPageDefault) {
        hasPagination = true;
        itemsPerPageSessionKey = sessionKey;
        this.itemsPerPageDefault = itemsPerPageDefault;
        return this;
    }

    public PaginationSortingSupport withSorting(String sortDirectionDefault, String... sortableFields) {
        hasSorting = true;
        this.sortDirectionDefault = sortDirectionDefault;

        this.sortableFields = new ArrayList<>(sortableFields.length);
        Collections.addAll(this.sortableFields, sortableFields);

        return this;
    }

    public QueryOptions buildAndApplyTo(HttpServletRequest request) {
        QueryOptions queryOptions = new QueryOptions();

        request.setAttribute("queryOptions", queryOptions);

        if (hasPagination) {
            final HttpSession session = request.getSession();
            Optional.ofNullable(request.getParameter("itemsPerPage"))
                    .map(Integer::parseInt)
                    .ifPresent(itemsPerPage -> session.setAttribute(itemsPerPageSessionKey, itemsPerPage));
            final int itemsPerPage = Optional.ofNullable((Integer) session.getAttribute(itemsPerPageSessionKey))
                    .orElse(itemsPerPageDefault);

            final int currentPage = Optional.ofNullable(request.getParameter("page"))
                    .map(Integer::parseInt)
                    .orElse(1);

            queryOptions.withPagination(currentPage, itemsPerPage);
        }

        if (hasSorting) {
            request.setAttribute("sortableFields", sortableFields);

            final String sortBy = request.getParameter("sortBy");
            final String sortDirection = request.getParameter("sortDirection");
            queryOptions.withSorting(sortBy, sortDirection, sortableFields.get(0), sortDirectionDefault, sortableFields);
        }

        return queryOptions;
    }
}
