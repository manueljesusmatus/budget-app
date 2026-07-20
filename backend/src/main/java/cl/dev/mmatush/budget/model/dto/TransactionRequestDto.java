package cl.dev.mmatush.budget.model.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record TransactionRequestDto(
    @NotNull String account,
    double amount,
    @NotNull String date,
    @NotNull String category,
    @Nullable String subcategory
) {
}
