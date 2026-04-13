package com.binoj.finflow.service.impl;

import com.binoj.finflow.dto.TransferRequest;
import com.binoj.finflow.entity.Account;
import com.binoj.finflow.entity.Transaction;
import com.binoj.finflow.repository.AccountRepository;
import com.binoj.finflow.repository.TransactionRepository;
import com.binoj.finflow.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public String transfer(TransferRequest transferRequest) {
        log.info("Initiating transfer from sender {} to receiver {} for amount {}", transferRequest.getSenderId(), transferRequest.getReceiverId(), transferRequest.getAmount());

        Long senderId = transferRequest.getSenderId();
        Long receiverId = transferRequest.getReceiverId();
        BigDecimal amount = transferRequest.getAmount();

        Account sender = accountRepository.findByUserId(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        Account receiver = accountRepository.findByUserId(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));
        log.info("Sender and receiver accounts found");

        if (sender.getBalance().compareTo(amount) < 0) {
            log.warn("Insufficient balance for sender {}", senderId);
            throw new RuntimeException("Insufficient balance");
        }

        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        accountRepository.save(sender);
        accountRepository.save(receiver);
        log.info("Balances updated for transfer");

        Transaction txn = new Transaction();
        txn.setSenderId(senderId);
        txn.setReceiverId(receiverId);
        txn.setAmount(amount);

        transactionRepository.save(txn);
        log.info("Transaction recorded successfully");

        return "Transfer successful";
    }

    @Override
    public List<Transaction> getTransactions(Long userId) {
        log.info("Retrieving transactions for user {}", userId);
        return transactionRepository.findBySenderIdOrReceiverId(userId, userId);
    }
}