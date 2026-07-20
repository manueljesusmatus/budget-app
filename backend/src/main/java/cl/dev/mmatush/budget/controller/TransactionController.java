package cl.dev.mmatush.budget.controller;

import cl.dev.mmatush.budget.model.dto.TransactionCsv;
import cl.dev.mmatush.budget.model.dto.TransactionRequestDto;
import cl.dev.mmatush.budget.model.dto.TransactionResponseDto;
import cl.dev.mmatush.budget.model.dto.TransactionUpdateable;
import cl.dev.mmatush.budget.service.CsvService;
import cl.dev.mmatush.budget.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final CsvService csvService;

    @GetMapping("/{id}")
    public TransactionResponseDto getById(@PathVariable long id) {
        return transactionService.readById(id);
    }

    @GetMapping
    public List<TransactionResponseDto> getAll() {
        return transactionService.readAll();
    }

    @GetMapping("/pageable")
    public Page<TransactionResponseDto> getAllPaginated(Pageable pageable) {
        return transactionService.readAll(pageable);
    }

    @PatchMapping("/{id}/amount")
    public TransactionResponseDto updateAmount(@PathVariable long id, @RequestBody TransactionUpdateable dto) {
        return transactionService.updateAmount(id, dto);
    }

    @PostMapping
    public TransactionResponseDto create(@RequestBody TransactionRequestDto dto) {
        return transactionService.createTransactionFromRequest(dto);
    }

    @PostMapping("/import")
    public List<TransactionResponseDto> importFromCsv(@RequestPart("file") MultipartFile file) {
        return csvService.loadObjectListFromUpload(TransactionCsv.class, file)
                .stream().map(transactionService::createTransactionFromCsv)
                .toList();
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable long id) {
        transactionService.deleteTransaction(id);
    }

}
