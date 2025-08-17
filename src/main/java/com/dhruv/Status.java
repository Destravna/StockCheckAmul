package com.dhruv;

public enum Status {
    InStock("in stock"),
    OutOfStock("out of stock");

    private final String value;

    private Status(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
