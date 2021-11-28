package net.madand.conferences.db.web;

import net.madand.conferences.db.util.QueryBuilder;

import java.util.Set;

public class Sorting {
    public static final String ASC = "asc";
    public static final String DESC = "desc";

    private String field;
    private String direction;

    public Sorting(String field, String direction, String defaultField, String defaultDirection, Set<String> allowedFields) {
        this.field = allowedFields.contains(field) ? field : defaultField;
        this.direction = isValidDirection(direction) ? direction : defaultDirection;
    }

    public void applyTo(QueryBuilder queryBuilder) {
        queryBuilder.orderBy(field + " " + direction);
    }

    private boolean isValidDirection(String direction) {
        return direction.equals(ASC) || direction.equals(DESC);
    }

    public String getField() {
        return field;
    }

    public String getDirection() {
        return direction;
    }
}
