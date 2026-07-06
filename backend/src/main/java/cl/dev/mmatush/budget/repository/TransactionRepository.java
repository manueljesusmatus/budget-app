package cl.dev.mmatush.budget.repository;

import cl.dev.mmatush.budget.model.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    List<TransactionEntity> findByCreatedAtBetween(LocalDateTime createdAtAfter, LocalDateTime createdAtBefore);

}