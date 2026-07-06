package cl.dev.mmatush.budget.model;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TransactionRequestDto(
    @NotNull String account,
    double amount,
    @NotNull LocalDateTime createdAt,
    @NotNull String category,
    @Nullable String subcategory
) {
}
