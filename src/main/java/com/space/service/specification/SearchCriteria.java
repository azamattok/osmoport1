package com.space.service.specification;

public class SearchCriteria {
    private String operation;
    private Object value;
    private String key;

    public String getOperation() {
        return operation;
    }

    public Object getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }

    public SearchCriteria(String operation, Object value, String key) {
        this.operation = operation;
        this.value = value;
        this.key = key;
    }
}
