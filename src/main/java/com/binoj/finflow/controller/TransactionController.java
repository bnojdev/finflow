package com.binoj.finflow.controller;

import com.binoj.finflow.dto.TransferRequest;
import com.binoj.finflow.entity.Transaction;
import com.binoj.finflow.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    public String transfer(@RequestBody TransferRequest request) {
        return transactionService.transfer(request);
    }

    @GetMapping("/transactions/{userId}")
    public List<Transaction> getTransactions(@PathVariable Long userId) {
        return transactionService.getTransactions(userId);
    }
}
