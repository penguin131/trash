package com.example.bank_api.controller;

import com.example.bank_api.dao.BankAPIDao;
import com.example.bank_api.exceptions.BusinessException;
import com.example.bank_api.model.BankAccount;
import com.example.bank_api.model.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class BankAPIController {
    @Autowired
    private BankAPIDao bankAPIDao;

    //Проверка баланса
    @GetMapping(value = "/getAccountForNumber/{number}")
    public ResponseEntity<BankAccount> getAccountForNumber(@PathVariable("number") String accountNumber) {
        BankAccount bankAccount = bankAPIDao.findAccountForNumber(accountNumber);
        if (bankAccount != null) {
            return ResponseEntity.ok(bankAccount);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/createAccount")
    public ResponseEntity<?> createAccount(@RequestBody BankAccount bankAccount) {
        bankAPIDao.saveAccount(bankAccount);
        return ResponseEntity.ok().build();
    }

    //Выпуск новой карты по счету
    @GetMapping("/createNewCard/{accountNumber}")
    public ResponseEntity<?> createNewCard(@PathVariable("accountNumber") String accountNumber) {
        try {
            bankAPIDao.createNewCard(accountNumber);
        } catch (BusinessException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    //Проcмотр списка карт
    @GetMapping("getAllCards/{accountNumber}")
    public ResponseEntity<List<Card>> getAllCards(@PathVariable("accountNumber") String accountNumber) {
        try {
            List<Card> cards = bankAPIDao.getAllCardsByAccount(accountNumber);
            return ResponseEntity.ok(cards);
        } catch (BusinessException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    //Внесение вредств
    @GetMapping("/addMoney/{accountNumber}&{sum}")
    public ResponseEntity<?> addMoney(@PathVariable("accountNumber") String accountNumber,
                                      @PathVariable("sum") BigDecimal sum) {
        try {
            bankAPIDao.addSum(accountNumber, sum);
        } catch (BusinessException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}
