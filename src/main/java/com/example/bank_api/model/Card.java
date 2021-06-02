package com.example.bank_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Card extends Entity {
    private String number;
    @JsonIgnore
    private BankAccount bankAccount;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }
}
