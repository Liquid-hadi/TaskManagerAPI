package com.example.taskmanager.enums;


import lombok.ToString;

public enum TaskPriority {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High");

    private final String value;

    TaskPriority(String value){
        this.value = value;
    }

    @Override
    public String toString(){return String.valueOf(value);}
}
