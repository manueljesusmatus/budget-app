package cl.dev.mmatush.budget.repository;

import cl.dev.mmatush.budget.model.entity.SubCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubCategoryRepository extends JpaRepository<SubCategoryEntity, Long> {

    Optional<SubCategoryEntity> findByName(String name);

}
