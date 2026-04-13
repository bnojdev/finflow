package com.binoj.finflow.service;

import com.binoj.finflow.dto.TransferRequest;
import com.binoj.finflow.entity.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

    String transfer(TransferRequest request);

    List<Transaction> getTransactions(Long userId);
}
