package cl.dev.mmatush.budget.model.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TransactionResponseDto(
        Long id,
        String account,
        Double amount,
        LocalDateTime date,
        String category,
        String subcategory
) {
}
