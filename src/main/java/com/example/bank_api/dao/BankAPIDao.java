package com.example.bank_api.dao;

import com.example.bank_api.exceptions.BusinessException;
import com.example.bank_api.model.BankAccount;
import com.example.bank_api.model.Card;
import com.example.bank_api.util.CardNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Service
public class BankAPIDao {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private CardNumberGenerator cardNumberGenerator;

    private static final String SELECT_ACCOUNT_QUERY = "select * from BANK_ACCOUNT where NUMBER=?";
    private static final String INSERT_ACCOUNT_QUERY = "insert into BANK_ACCOUNT (NUMBER, BALANCE) values ( ?, ? )";
    private static final String INSERT_CARD_QUERY = "insert into CARD (NUMBER, BANK_ACCOUNT) values ( ?, ? )";
    private static final String SELECT_CARD_QUERY = "select * from CARD where BANK_ACCOUNT=?";
    private static final String UPDATE_ACCOUNT_QUERY = "update BANK_ACCOUNT set BALANCE=? where NUMBER=?";

    public BankAccount findAccountForNumber(String number) {
        BankAccount account = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ACCOUNT_QUERY)) {
             statement.setString(1, number);
             ResultSet rs = statement.executeQuery();
             if (rs.next()) {
                 account = new BankAccount();
                 account.setId(rs.getInt("ACCOUNT_ID"));
                 account.setNumber(rs.getString("NUMBER"));
                 account.setBalance(rs.getBigDecimal("BALANCE"));
             }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return account;
    }

    public void saveAccount(BankAccount account) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_ACCOUNT_QUERY)) {
            statement.setString(1, account.getNumber());
            statement.setBigDecimal(2, account.getBalance() == null ? new BigDecimal(0) : account.getBalance());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createNewCard(String accountNumber) throws BusinessException {
        BankAccount account = findAccountForNumber(accountNumber);
        if (account == null) {
            throw new BusinessException("Not found account");
        }
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(INSERT_CARD_QUERY);
            statement.setString(1, cardNumberGenerator.generate());
            statement.setInt(2, account.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Card> getAllCardsByAccount(String accountNumber) throws BusinessException {
        BankAccount account = findAccountForNumber(accountNumber);
        if (account == null) {
            throw new BusinessException("Not found account");
        }
        List<Card> cards = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_CARD_QUERY);
            statement.setInt(1, account.getId());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Card card = new Card();
                card.setBankAccount(account);
                card.setId(rs.getInt("CARD_ID"));
                card.setNumber(rs.getString("NUMBER"));
                cards.add(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cards;
    }

    public void addSum(String accountNumber, BigDecimal sum) throws BusinessException {
        BankAccount account = findAccountForNumber(accountNumber);
        if (account == null) {
            throw new BusinessException("Not found account");
        }
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_ACCOUNT_QUERY);
            statement.setBigDecimal(1, account.getBalance().add(sum));
            statement.setString(2, accountNumber);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
