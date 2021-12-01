package net.madand.conferences.db.web;

import net.madand.conferences.db.util.QueryBuilder;

import java.util.List;

public class Sorting {
    public static final String ASC = "asc";
    public static final String DESC = "desc";

    private String field;
    private String direction;
    private String defaultField;
    private String defaultDirection;

    public Sorting(String field, String direction, String defaultField, String defaultDirection, List<String> allowedFields) {
        this.field = allowedFields.contains(field) ? field : defaultField;
        this.direction = isValidDirection(direction) ? direction : defaultDirection;
        this.defaultField = defaultField;
        this.defaultDirection = defaultDirection;
    }

    public void applyTo(QueryBuilder queryBuilder) {
        queryBuilder.orderBy(field + " " + direction);
    }

    private boolean isValidDirection(String direction) {
        return ASC.equals(direction) || DESC.equals(direction);
    }

    public String getField() {
        return field;
    }

    public String getDirection() {
        return direction;
    }

    public String getDefaultField() {
        return defaultField;
    }

    public void setDefaultField(String defaultField) {
        this.defaultField = defaultField;
    }

    public String getDefaultDirection() {
        return defaultDirection;
    }

    public void setDefaultDirection(String defaultDirection) {
        this.defaultDirection = defaultDirection;
    }
}
