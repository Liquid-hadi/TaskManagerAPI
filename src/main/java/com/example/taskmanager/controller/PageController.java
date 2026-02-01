package com.example.taskmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/tasks")
    public String tasksPage(){
        return "tasks";
    }
}
