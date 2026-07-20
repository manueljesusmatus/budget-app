package cl.dev.mmatush.budget.repository;

import cl.dev.mmatush.budget.model.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Long>  {

    Optional<AccountEntity> findByName(String name);

}
