package com.example.taskmanager.exceptions;

import java.time.Instant;

public record ApiError(
                       int status,
                       String message) {}
