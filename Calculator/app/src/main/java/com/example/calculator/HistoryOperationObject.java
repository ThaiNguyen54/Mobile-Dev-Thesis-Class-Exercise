package com.example.calculator;

public class HistoryOperationObject {
    private int order;
    private HistoryOperation historyOperation;

    public HistoryOperationObject(int order, HistoryOperation historyOperation) {
        this.order = order;
        this.historyOperation = historyOperation;
    }

    public int getOrder() {
        return this.order;
    }
    public HistoryOperation getHistoryOperation() {
        return this.historyOperation;
    }
}
