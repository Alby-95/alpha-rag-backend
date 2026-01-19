package com.example.alpha.api;

import jakarta.validation.constraints.NotBlank;

public record SendMessageRequest(@NotBlank String content) {}