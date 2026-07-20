package cl.dev.mmatush.budget.mapper;

import cl.dev.mmatush.budget.enums.TransactionType;
import cl.dev.mmatush.budget.model.dto.TransactionCsv;
import cl.dev.mmatush.budget.model.dto.TransactionRequestDto;
import cl.dev.mmatush.budget.model.dto.TransactionResponseDto;
import cl.dev.mmatush.budget.model.entity.AccountEntity;
import cl.dev.mmatush.budget.model.entity.CategoryEntity;
import cl.dev.mmatush.budget.model.entity.SubCategoryEntity;
import cl.dev.mmatush.budget.model.entity.TransactionEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public final class TransactionFactory {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private TransactionFactory() {
    }

    public static TransactionEntity createEntity(@Nonnull TransactionCsv csv,
                                                 @Nonnull AccountEntity account,
                                                 @Nonnull CategoryEntity category,
                                                 @Nullable SubCategoryEntity subcategory) {
        Objects.requireNonNull(csv, "csv cannot be null");
        Objects.requireNonNull(account, "account cannot be null");
        Objects.requireNonNull(category, "category cannot be null");
        return TransactionEntity.builder()
                .amount(csv.getAmount())
                .date(csv.getDate())
                .type(csv.getAmount() > 0 ? TransactionType.INCOME : TransactionType.EXPENSE)
                .account(account)
                .category(category)
                .subcategory(subcategory)
                .build();
    }

    public static TransactionEntity createEntity(@Nonnull TransactionRequestDto dto,
                                                 @Nonnull AccountEntity account,
                                                 @Nonnull CategoryEntity category,
                                                 @Nullable SubCategoryEntity subcategory) {
        Objects.requireNonNull(dto, "dto cannot be null");
        Objects.requireNonNull(account, "account cannot be null");
        Objects.requireNonNull(category, "category cannot be null");
        return TransactionEntity.builder()
                .amount(dto.amount())
                .date(LocalDate.parse(dto.date(), DATE_FORMATTER).atStartOfDay())
                .type(dto.amount() > 0 ? TransactionType.INCOME : TransactionType.EXPENSE)
                .account(account)
                .category(category)
                .subcategory(subcategory)
                .build();
    }

    public static TransactionResponseDto createResponseDto(@Nonnull TransactionEntity e) {
        Objects.requireNonNull(e, "entity cannot be null");
        return TransactionResponseDto.builder()
                .id(e.getId())
                .account(e.getAccount().getName())
                .amount(e.getAmount())
                .date(e.getDate())
                .category(e.getCategory().getName())
                .subcategory(Objects.nonNull(e.getSubcategory()) ? e.getSubcategory().getName() : null)
                .build();
    }

}
