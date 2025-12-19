package com.seed.fund.web.dto;

public record ErrorModel(
        ErrorCategory category,
        String description
) {}
