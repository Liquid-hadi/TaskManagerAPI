package com.example.taskmanager.enums;

public enum TaskStatus {
    TODO("Todo"),
    IN_PROGRESS("In progress"),
    DONE("Done");

    private final String value;

    TaskStatus(String value){
        this.value = value;
    }

    @Override
    public String toString(){return String.valueOf(value);}
}
