package net.madand.conferences.db.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QueryBuilder {
    private String select = "*";
    private String table;
    private final List<String> join = new ArrayList<>();
    private final List<String> where = new ArrayList<>();
    private final List<String> orderBy = new ArrayList<>();

    public QueryBuilder(String table) {
        this.table = table;
    }

    public QueryBuilder select(String select) {
        this.select = select;
        return this;
    }

    public QueryBuilder leftJoin(String leftJoin) {
        join.add(" LEFT JOIN " + leftJoin);
        return this;
    }

    public QueryBuilder where(String... wheres) {
        Collections.addAll(where, wheres);
        return this;
    }

    public QueryBuilder orderBy(String... orders) {
        Collections.addAll(orderBy, orders);
        return this;
    }

    public String buildSelect() {
        StringBuilder sb = new StringBuilder(String.format("SELECT %s FROM %s", select, table));

        if (!join.isEmpty()) {
            sb.append(String.join(" ", join));
        }

        if (!where.isEmpty()) {
            sb.append(" WHERE ").append(String.join(" AND ", where));
        }

        if (!orderBy.isEmpty()) {
            sb.append(" ORDER BY ").append(String.join(", ", orderBy));
        }

        return sb.toString();
    }
}
