package com.example.calculator;

import com.google.gson.JsonElement;

public class HistoryOperation {
    private String operation;
    private String result;

    public HistoryOperation(String operation, String result) {
        this.operation = operation;
        this.result = result;
    }

    public String getOperation() {
        return this.operation;
    }

    public String getResult() {
        return this.result;
    }

}
