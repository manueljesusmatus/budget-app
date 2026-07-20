package cl.dev.mmatush.budget.repository;

import cl.dev.mmatush.budget.model.entity.PeriodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PeriodRepository extends JpaRepository<PeriodEntity, Long> {

    @Query(value =
            "SELECT * FROM app_period p " +
            "WHERE p.start_date <= :date AND p.end_date >= :date",
            nativeQuery = true)
    List<PeriodEntity> findByDate(@Param("date") String date);

}
