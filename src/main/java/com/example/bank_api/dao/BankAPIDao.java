package com.example.bank_api.dao;

import com.example.bank_api.model.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Service
public class BankAPIDao {
    @Autowired
    private DataSource dataSource;

    private static final String SELECT_ACCOUNT_QUERY = "select * from BANK_ACCOUNT where NUMBER=?";
    private static final String INSERT_ACCOUNT_QUERY = "insert into BANK_ACCOUNT (NUMBER) values ( ? )";

    public BankAccount findAccountForNumber(String number) {
        BankAccount account = null;
        try (Connection connection = dataSource.getConnection()) {
             PreparedStatement statement = connection.prepareStatement(SELECT_ACCOUNT_QUERY);
             statement.setString(1, number);
             ResultSet rs = statement.executeQuery();
             if (rs.next()) {
                 account = new BankAccount();
                 account.setId(rs.getInt(1));
                 account.setNumber(rs.getString(2));
             }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return account;
    }

    public BankAccount saveAccount(BankAccount account) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(INSERT_ACCOUNT_QUERY);
            statement.setString(1, account.getNumber());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return account;
    }
}
