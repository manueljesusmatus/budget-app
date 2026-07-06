package cl.dev.mmatush.budget.controller;

import cl.dev.mmatush.budget.model.TransactionCsv;
import cl.dev.mmatush.budget.model.TransactionRequestDto;
import cl.dev.mmatush.budget.model.TransactionResponseDto;
import cl.dev.mmatush.budget.service.CsvService;
import cl.dev.mmatush.budget.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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

    @PostMapping
    public List<TransactionResponseDto> createAll(@RequestBody List<TransactionRequestDto> dtos) {
        return transactionService.createAllFromRequest(dtos);
    }

    @GetMapping
    public List<TransactionResponseDto> findAll() {
        return transactionService.readAll();
    }

    @GetMapping("/csv")
    public List<TransactionCsv> readCsv() {
        return csvService.loadObjectListFromClasspath(TransactionCsv.class, "static/test.csv");
    }

    @PostMapping("/csv")
    public List<TransactionResponseDto> postCsv(@RequestPart("file") MultipartFile file) {
        final var dtos = csvService.loadObjectListFromUpload(TransactionCsv.class, file);
        return transactionService.createAllFromCsv(dtos);
    }

}
