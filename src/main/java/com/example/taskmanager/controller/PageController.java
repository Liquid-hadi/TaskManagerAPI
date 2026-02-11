package com.example.taskmanager.controller;

import com.example.taskmanager.enums.TaskPriority;
import com.example.taskmanager.enums.TaskStatus;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequestMapping("/tasks")
public class PageController {

    @GetMapping
    public String tasksPage(
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDateTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "CreatedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            Model model
    ) {
        model.addAttribute("priority", priority);
        model.addAttribute("status", status);
        model.addAttribute("q", q);
        model.addAttribute("dueDateFrom", dueDateFrom);
        model.addAttribute("dueDateTo", dueDateTo);

        return "tasks"; // tasks.jsp
    }
}
