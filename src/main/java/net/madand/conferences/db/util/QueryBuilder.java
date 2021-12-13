package net.madand.conferences.db.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QueryBuilder {
    private String select = "*";
    private String table;
    private final List<String> joins = new ArrayList<>();
    private final List<String> where = new ArrayList<>();
    private String orderBy = "";
    private String limitAndOffset = "";

    public QueryBuilder(String table) {
        this.table = table;
    }

    public QueryBuilder select(String select) {
        this.select = select;
        return this;
    }

    public QueryBuilder leftJoin(String leftJoin) {
        joins.add(" LEFT JOIN " + leftJoin);
        return this;
    }

    public QueryBuilder innerJoin(String innerJoin) {
        joins.add(" INNER JOIN " + innerJoin);
        return this;
    }

    public QueryBuilder where(String... wheres) {
        Collections.addAll(where, wheres);
        return this;
    }

    public QueryBuilder orderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public QueryBuilder limitAndOffset(int limit, int offset) {
        limitAndOffset = String.format(" LIMIT %d OFFSET %d", limit, offset);
        return this;
    }

    public String buildSelect() {
        StringBuilder sb = new StringBuilder(String.format("SELECT %s FROM %s", select, table));
        buildCommonParts(sb);

        if (!orderBy.isEmpty()) {
            sb.append(" ORDER BY ").append(orderBy);
        }
        sb.append(limitAndOffset);

        return sb.toString();
    }

    /**
     * Build the SQL SELECT COUNT(*) query, that returns a total number of entries (disregarding any LIMIT and OFFSET).
     *
     * @return the built SQL query.
     */
    public String buildCountTotal() {
        StringBuilder sb = new StringBuilder(String.format("SELECT COUNT(*) FROM %s", table));
        buildCommonParts(sb);
        return sb.toString();
    }

    private void buildCommonParts(StringBuilder sb) {
        if (!joins.isEmpty()) {
            sb.append(String.join(" ", joins));
        }

        if (!where.isEmpty()) {
            sb.append(" WHERE ").append(String.join(" AND ", where));
        }

    }
}
