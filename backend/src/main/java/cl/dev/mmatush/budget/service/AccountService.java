package cl.dev.mmatush.budget.service;

import cl.dev.mmatush.budget.model.entity.AccountEntity;
import cl.dev.mmatush.budget.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountEntity create(String nameAccount) {
        final var accountEntity = AccountEntity.builder()
                .name(nameAccount)
                .build();
        return accountRepository.save(accountEntity);
    }

    public AccountEntity read(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    public AccountEntity update(Long id, String newAccountName) {
        return accountRepository.findById(id)
                .map(account -> {
                    account.setName(newAccountName);
                    return accountRepository.save(account);
                })
                .orElse(null);
    }

    public void delete(Long id) {
        accountRepository.deleteById(id);
    }

}
