package com.focuslearning.example.kafka.database;

public enum RecordStatus {

    NEW("N"),REPLAY("R"),PROCESSED("P"),EXPIRED("E");
    private String action;

    RecordStatus(String action) {
        this.action = action;
    }


    public String getAction() {
        return action;
    }
}
