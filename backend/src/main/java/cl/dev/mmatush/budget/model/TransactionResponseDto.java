package cl.dev.mmatush.budget.model;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TransactionResponseDto(
        Long id,
        String account,
        Double amount,
        LocalDateTime createdAt,
        String category,
        String subcategory
) {
}
