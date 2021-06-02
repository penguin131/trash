package com.example.bank_api.controller;

import com.example.bank_api.dao.BankAPIDao;
import com.example.bank_api.model.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BankAPIController {
    @Autowired
    private BankAPIDao bankAPIDao;

    @GetMapping(value = "/getAccountForNumber/{number}")
    public ResponseEntity<BankAccount> getAccountForNumber(@PathVariable("number") String accountNumber) {
        BankAccount bankAccount = bankAPIDao.findAccountForNumber(accountNumber);
        if (bankAccount != null) {
            return ResponseEntity.ok(bankAccount);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/createAccount")
    public ResponseEntity<?> createAccount(@RequestBody BankAccount bankAccount) {
        bankAPIDao.saveAccount(bankAccount);
        return ResponseEntity.ok().build();
    }
}
