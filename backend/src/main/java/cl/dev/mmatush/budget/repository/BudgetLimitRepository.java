package cl.dev.mmatush.budget.repository;

import cl.dev.mmatush.budget.model.entity.SubCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetLimitRepository extends JpaRepository<SubCategoryEntity, Long> {
}
