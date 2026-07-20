package cl.dev.mmatush.budget.service;

import cl.dev.mmatush.budget.enums.TransactionType;
import cl.dev.mmatush.budget.exception.CrudException;
import cl.dev.mmatush.budget.mapper.TransactionFactory;
import cl.dev.mmatush.budget.model.dto.TransactionCsv;
import cl.dev.mmatush.budget.model.dto.TransactionRequestDto;
import cl.dev.mmatush.budget.model.dto.TransactionResponseDto;
import cl.dev.mmatush.budget.model.dto.TransactionUpdateable;
import cl.dev.mmatush.budget.model.entity.AccountEntity;
import cl.dev.mmatush.budget.model.entity.CategoryEntity;
import cl.dev.mmatush.budget.model.entity.SubCategoryEntity;
import cl.dev.mmatush.budget.repository.AccountRepository;
import cl.dev.mmatush.budget.repository.CategoryRepository;
import cl.dev.mmatush.budget.repository.SubCategoryRepository;
import cl.dev.mmatush.budget.repository.TransactionRepository;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    public void deleteTransaction(long id) {
        try {
            log.debug("Deleting transaction with id {}", id);
            transactionRepository.deleteById(id);
        } catch (DataAccessException e) {
            throw CrudException.fromDataAccess(e);
        } catch (RuntimeException e) {
            throw CrudException.deleteException(id, e);
        }
    }

    public TransactionResponseDto updateAmount(long id, @Nonnull TransactionUpdateable dto) {
        try {
            log.info("Update amount of transaction with id {}", id);
            Objects.requireNonNull(dto, "dto cannot be null");

            log.debug("Updating transaction: {}", dto);
            final var e = transactionRepository.findById(id)
                    .orElseThrow(() -> new CrudException("Transaction with id " + id + " not found"));

            e.setAmount(dto.amount());
            e.setType(dto.amount() >= 0 ? TransactionType.INCOME : TransactionType.EXPENSE);

            return TransactionFactory.createResponseDto(transactionRepository.save(e));
        } catch (DataAccessException e) {
            throw CrudException.fromDataAccess(e);
        } catch (RuntimeException e) {
            throw CrudException.updateException(id);
        }
    }

    public TransactionResponseDto readById(long id) {
        try {
            log.info("Read transaction with id {}", id);
            return transactionRepository.findById(id)
                    .map(TransactionFactory::createResponseDto)
                    .orElse(null);
        } catch (DataAccessException e) {
            throw CrudException.fromDataAccess(e);
        } catch (RuntimeException e) {
            throw CrudException.readException(id, e);
        }
    }

    public List<TransactionResponseDto> readAll() {
        try {
            log.info("Read all transactions");
            return transactionRepository.findAll()
                    .stream().map(TransactionFactory::createResponseDto)
                    .toList();
        } catch (DataAccessException e) {
            throw CrudException.fromDataAccess(e);
        } catch (RuntimeException e) {
            throw CrudException.readCollectionException(e);
        }
    }

    public Page<TransactionResponseDto> readAll(Pageable pageable) {
        try {
            log.info("Read all transactions with pagination");
            return transactionRepository.findAll(pageable)
                    .map(TransactionFactory::createResponseDto);
        } catch (DataAccessException e) {
            throw CrudException.fromDataAccess(e);
        } catch (RuntimeException e) {
            throw CrudException.readCollectionException(e);
        }
    }

    public TransactionResponseDto createTransactionFromRequest(@Nonnull TransactionRequestDto dto) {
        try {
            log.info("Creating new transaction");
            Objects.requireNonNull(dto);

            log.debug("Creating transaction from dto: {}", dto);
            AccountEntity a = accountRepository.findByName(dto.account())
                    .orElseGet(() -> AccountEntity.fromName(dto.account()));
            CategoryEntity c = categoryRepository.findByName(dto.category())
                    .orElseGet(() -> CategoryEntity.fromNameAndAccount(dto.category(), a));
            SubCategoryEntity sc = subCategoryRepository.findByName(dto.subcategory())
                    .orElseGet(() -> StringUtils.hasText(dto.subcategory()) ?
                            SubCategoryEntity.fromNameAndCategory(dto.subcategory(), c) : null);
            final var e = TransactionFactory.createEntity(dto, a, c, sc);

            return TransactionFactory.createResponseDto(transactionRepository.save(e));
        } catch (DataAccessException e) {
            throw CrudException.fromDataAccess(e);
        } catch (RuntimeException e) {
            throw CrudException.createException(e);
        }
    }

    public TransactionResponseDto createTransactionFromCsv(@Nonnull TransactionCsv csv) {
        try {
            log.info("Creating transaction from CSV");
            Objects.requireNonNull(csv);

            log.debug("Creating transaction from CSV: {}", csv);
            AccountEntity a = accountRepository.findByName(csv.getAccount())
                    .orElseGet(() -> AccountEntity.fromName(csv.getAccount()));
            CategoryEntity c = categoryRepository.findByName(csv.getCategoryName())
                    .orElseGet(() -> CategoryEntity.fromNameAndAccount(csv.getCategoryName(), a));
            SubCategoryEntity sc = subCategoryRepository.findByName(csv.getSubcategoryName())
                    .orElseGet(() -> StringUtils.hasText(csv.getSubcategoryName()) ?
                            SubCategoryEntity.fromNameAndCategory(csv.getSubcategoryName(), c) : null);
            final var e = TransactionFactory.createEntity(csv, a, c, sc);

            return TransactionFactory.createResponseDto(transactionRepository.save(e));
        } catch (DataAccessException e) {
            throw CrudException.fromDataAccess(e);
        } catch (RuntimeException e) {
            throw CrudException.createException(e);
        }
    }

}
