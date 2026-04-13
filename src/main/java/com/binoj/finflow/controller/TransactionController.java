package com.binoj.finflow.controller;

import com.binoj.finflow.dto.TransferRequest;
import com.binoj.finflow.entity.Transaction;
import com.binoj.finflow.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    public String transfer(@RequestBody TransferRequest request) {
        log.info("Transfer request received for sender {} to receiver {}", request.getSenderId(), request.getReceiverId());
        return transactionService.transfer(request);
    }

    @GetMapping("/transactions/{userId}")
    public List<Transaction> getTransactions(@PathVariable Long userId) {
        log.info("Request to get transactions for user {}", userId);
        return transactionService.getTransactions(userId);
    }
}
