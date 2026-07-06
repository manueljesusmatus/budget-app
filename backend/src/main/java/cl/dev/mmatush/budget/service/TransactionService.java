package cl.dev.mmatush.budget.service;

import cl.dev.mmatush.budget.exception.CrudException;
import cl.dev.mmatush.budget.mapper.TransactionFactory;
import cl.dev.mmatush.budget.model.TransactionCsv;
import cl.dev.mmatush.budget.model.TransactionEntity;
import cl.dev.mmatush.budget.model.TransactionRequestDto;
import cl.dev.mmatush.budget.model.TransactionResponseDto;
import cl.dev.mmatush.budget.repository.TransactionRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionResponseDto readById(@NonNull Long id) {
        try {
            log.info("Reading transaction by id {}", id);
            return transactionRepository.findById(id)
                    .map(TransactionFactory::createResponse)
                    .orElseThrow();
        } catch (NoSuchElementException e) {
            throw new CrudException("No such transaction with id: " + id);
        } catch (DataAccessException e) {
            throw CrudException.fromDataAccess(e);
        } catch (RuntimeException e)  {
            throw CrudException.readException(id.toString(), e);
        }
    }

    public List<TransactionResponseDto> readByDate(@NonNull LocalDateTime createdAtAfter,
                                                   @NonNull LocalDateTime createdAtBefore) {
        try {
            log.info("Reading transactions by date {}", createdAtAfter);
            return transactionRepository.findByCreatedAtBetween(createdAtAfter, createdAtBefore)
                    .stream()
                    .map(TransactionFactory::createResponse)
                    .toList();
        } catch (DataAccessException e) {
            throw CrudException.fromDataAccess(e);
        } catch (RuntimeException e)  {
            throw CrudException.readCollectionException(e);
        }
    }

    public List<TransactionResponseDto> readAll() {
        try {
            log.info("Reading all transactions");
            return transactionRepository.findAll()
                    .stream()
                    .map(TransactionFactory::createResponse)
                    .toList();
        } catch (DataAccessException e) {
            throw CrudException.fromDataAccess(e);
        } catch (RuntimeException e) {
            throw CrudException.readCollectionException(e);
        }
    }

    public void delete(@NonNull Long id) {
        try {
            if(!transactionRepository.existsById(id)) {
                log.warn("No such transaction with id: {}", id);
                return;
            }
            log.info("Deleting transaction by id {}", id);
            transactionRepository.deleteById(id);
        } catch (DataAccessException e) {
            throw CrudException.fromDataAccess(e);
        } catch (RuntimeException e) {
            throw CrudException.deleteException(id.toString(), e);
        }
    }

    public TransactionResponseDto create(@NonNull TransactionRequestDto dto) {
        try {
            log.info("Creating transaction {}", dto);
            final var entity = TransactionFactory.createEntity(dto);
            return TransactionFactory.createResponse(transactionRepository.save(entity));
        } catch (DataAccessException e) {
            throw CrudException.fromDataAccess(e);
        } catch (RuntimeException e) {
            throw CrudException.createException(e);
        }
    }

    public List<TransactionResponseDto> createAllFromRequest(List<TransactionRequestDto> dtos) {
        return createAllFrom(dtos, TransactionFactory::createEntity);
    }

    public List<TransactionResponseDto> createAllFromCsv(List<TransactionCsv> csvs) {
        return createAllFrom(csvs, TransactionFactory::createEntity);
    }

    private <T> List<TransactionResponseDto> createAllFrom(@NonNull List<T> items, Function<T, TransactionEntity> m) {
        try {
            log.info("Creating all transactions from csv {}", items.size());
            final var entities = items.stream().map(m).toList();
            return transactionRepository.saveAll(entities)
                    .stream()
                    .map(TransactionFactory::createResponse)
                    .toList();
        } catch (DataAccessException e) {
            throw CrudException.fromDataAccess(e);
        } catch (RuntimeException e) {
            throw CrudException.createCollectionException(e);
        }
    }

}
