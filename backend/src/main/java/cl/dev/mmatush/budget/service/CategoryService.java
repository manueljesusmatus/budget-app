package cl.dev.mmatush.budget.service;

import cl.dev.mmatush.budget.exception.CrudException;
import cl.dev.mmatush.budget.model.entity.CategoryEntity;
import cl.dev.mmatush.budget.repository.AccountRepository;
import cl.dev.mmatush.budget.repository.CategoryRepository;
import cl.dev.mmatush.budget.repository.SubCategoryRepository;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    public CategoryEntity create(@Nonnull String nameCategory, long accountId) {
        final var categoryEntity = CategoryEntity.builder()
                .name(nameCategory)
                .account(accountRepository.getReferenceById(accountId))
                .build();
        return categoryRepository.save(categoryEntity);
    }

    public CategoryEntity update(long categoryId, @Nonnull String newNameCategory) {
        return categoryRepository.findById(categoryId)
                .map(category -> {
                    category.setName(newNameCategory);
                    return categoryRepository.save(category);
                })
                .orElseThrow(() -> CrudException.updateException(categoryId));
    }

    public Optional<CategoryEntity> read(long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    public void delete(long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

}
