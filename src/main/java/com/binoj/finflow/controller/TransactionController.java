package com.binoj.finflow.controller;

import com.binoj.finflow.dto.ApiResponse;
import com.binoj.finflow.dto.TransferRequest;
import com.binoj.finflow.entity.Transaction;
import com.binoj.finflow.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@Tag(name = "Transaction Management", description = "APIs for managing money transfers and transaction history")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    @Operation(summary = "Transfer money between users")
    public ResponseEntity<ApiResponse<?>> transfer(@RequestBody TransferRequest request) {
        log.info("Transfer request received for sender {} to receiver {}", request.getSenderId(), request.getReceiverId());
        String msg = transactionService.transfer(request);
        return ResponseEntity.ok(
                new ApiResponse<>(200, msg, null)
        );
    }

    @GetMapping("/transactions/{userId}")
    @Operation(summary = "Get transaction history for a user")
    public ResponseEntity<ApiResponse<?>> getTransactions(@PathVariable Long userId) {
        log.info("Request to get transactions for user {}", userId);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Success",
                        transactionService.getTransactions(userId))
        );
    }
}
