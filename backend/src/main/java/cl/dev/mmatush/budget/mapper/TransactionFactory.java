package cl.dev.mmatush.budget.mapper;

import cl.dev.mmatush.budget.model.TransactionCsv;
import cl.dev.mmatush.budget.model.TransactionEntity;
import cl.dev.mmatush.budget.model.TransactionRequestDto;
import cl.dev.mmatush.budget.model.TransactionResponseDto;

public final class TransactionFactory {

    private TransactionFactory() {
    }

    public static TransactionEntity createEntity(TransactionRequestDto dto) {
        return TransactionEntity.builder()
                .account(dto.account())
                .amount(dto.amount())
                .currency("CLP")
                .income(dto.amount() >= 0)
                .createdAt(dto.createdAt())
                .category(dto.category())
                .subcategory(dto.subcategory())
                .build();
    }

    public static TransactionEntity createEntity(TransactionCsv csv) {
        return TransactionEntity.builder()
                .account(csv.getAccount())
                .amount(csv.getAmount())
                .currency(csv.getCurrency())
                .income(csv.isIncome())
                .createdAt(csv.getDate())
                .category(csv.getCategoryName())
                .subcategory(csv.getSubcategoryName())
                .build();
    }

    public static TransactionResponseDto createResponse(TransactionEntity entity) {
        return TransactionResponseDto.builder()
                .id(entity.getId())
                .account(entity.getAccount())
                .amount(entity.getAmount())
                .createdAt(entity.getCreatedAt())
                .category(entity.getCategory())
                .subcategory(entity.getSubcategory())
                .build();

    }

}
