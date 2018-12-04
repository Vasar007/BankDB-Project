package account;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class BankAccountImpl implements BankAccount {
    private List<String> currencyAccountIDs = new ArrayList<>();
    private List<Integer> balLeft = new ArrayList<>();
    private List<Integer> balRight = new ArrayList<>();
    private List<String> currencies = new ArrayList<>();

    private Statement statement;
    private Connection con;

    BankAccountImpl(Connection con) {
        this.con = con;
    }

    private int getIndex(String currencyAccountID) {
        return currencyAccountIDs.indexOf(currencyAccountID);
    }

    @Override
    public void loadData(String accountID) {
        try {
            statement = con.createStatement();
            String sql = "SELECT currencyaccountID, balleft, balright, currency FROM bankaccount " +
                    "WHERE accountID='" + accountID + "'";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                currencyAccountIDs.add(rs.getString("currencyaccountID"));
                balLeft.add(rs.getInt("balleft"));
                balRight.add(rs.getInt("balright"));
                currencies.add(rs.getString("currency"));
            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Load Bank Account Unsuccessful!");
            e.printStackTrace();
        }
    }

    @Override
    public boolean setBalLeft(int balLeft, String currencyAccountID) {
        this.balLeft.set(getIndex(currencyAccountID), balLeft);
        boolean isSuccess;

        try {
            statement = con.createStatement();
            String sql = "UPDATE bankaccount SET balleft=" + balLeft + " WHERE currencyaccountID='" + currencyAccountID + "'";
            statement.executeUpdate(sql);
            isSuccess = true;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Error --> Cannot Update Balance");
            e.printStackTrace();
            isSuccess = false;
        }

        return isSuccess;
    }

    @Override
    public boolean setBalRight(int balRight, String currencyAccountID) {
        this.balRight.set(getIndex(currencyAccountID), balRight);
        boolean isSuccess;

        try {
            statement = con.createStatement();
            String sql = "UPDATE bankaccount SET balright=" + balRight + " WHERE currencyaccountID='" + currencyAccountID + "'";
            statement.executeUpdate(sql);
            isSuccess = true;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Error --> Cannot Update Balance");
            e.printStackTrace();
            isSuccess = false;
        }

        return isSuccess;
    }

    @Override
    public boolean setCurrency(String currency, String currencyAccountID) {
        this.currencies.set(getIndex(currencyAccountID), currency);
        boolean isSuccess;

        try {
            statement = con.createStatement();
            String sql = "UPDATE bankaccount SET currency='" + currency + "' WHERE currencyaccountID='" + currencyAccountID + "'";
            statement.executeUpdate(sql);
            isSuccess = true;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Error --> Cannot Update Currency");
            e.printStackTrace();
            isSuccess = false;
        }

        return isSuccess;
    }

    @Override
    public List<String> getCurrencyAccountIDs() {
        return currencyAccountIDs;
    }

    @Override
    public List<Integer> getAllBalLeft() {
        return balLeft;
    }

    @Override
    public List<Integer> getAllBalRight() {
        return balRight;
    }

    @Override
    public List<String> getAllCurrencies() {
        return currencies;
    }

    @Override
    public int getBalLeft(String currencyAccountID) {
        return balLeft.get(getIndex(currencyAccountID));
    }

    @Override
    public int getBalRight(String currencyAccountID) {
        return balRight.get(getIndex(currencyAccountID));
    }

    @Override
    public String getCurrency(String currencyAccountID) {
        return currencies.get(getIndex(currencyAccountID));
    }

    @Override
    public boolean withdraw(int amount, String currencyAccountID) {
        int temp = getBalLeft(currencyAccountID);
        if (temp < amount) {
            JOptionPane.showMessageDialog(null,"Error --> Amount exceeds the balance!");
            return false;
        }
        setBalLeft(temp - amount, currencyAccountID);

        return true;
    }

    @Override
    public boolean deposit(int amount, String currencyAccountID) {
        if (amount < 0) {
            return false;
        }
        int temp = getBalLeft(currencyAccountID);
        setBalLeft(temp + amount, currencyAccountID);

        return true;
    }

    /**
     * Creates an account with the given information, writes it to the database
     */
    @Override
    public boolean createAccount(String accountID, String newCurrencyAccountID, int balleft, int balright,
                                 String currency) {
        boolean isSuccess;

        try {
            statement = con.createStatement();
            String sql = "INSERT INTO bankaccount(accountID, currencyaccountID, balleft, balright, currency) " +
                    " VALUES('" + accountID + "', '" + newCurrencyAccountID + "', '" + balleft + "', '" +
                    balright + "', '" + currency + "');";

            statement.executeUpdate(sql);
            isSuccess = true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Error --> Cannot Create a New Account!");
            e.printStackTrace();
            isSuccess = false;
        }

        return isSuccess;
    }

    /**
     * Deletes an account
     */
    @Override
    public boolean deleteAccount(String currencyAccountID) {
        boolean isSuccess;

        try {
            statement = con.createStatement();
            String sql = "DELETE FROM bankaccount WHERE currencyaccountID='" + currencyAccountID + "'";
            statement.executeUpdate(sql);
            isSuccess = true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Error --> Cannot Delete Account!");
            e.printStackTrace();
            isSuccess = false;
        }
        return isSuccess;
    }
}
