package dbgui;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class BankAccountImpl implements BankAccount {
    private List<String> currencyAccountIDs = new ArrayList<>();
    private List<BigDecimal> balances = new ArrayList<>();
    private List<String> currencies = new ArrayList<>();

    private Connection con;


    BankAccountImpl(Connection con) {
        this.con = con;
    }

    private int getIndex(String currencyAccountID) {
        return currencyAccountIDs.indexOf(currencyAccountID);
    }

    private void clearData() {
        currencyAccountIDs.clear();
        balances.clear();
        currencies.clear();
    }

    private BigDecimal checkSmallSize(BigDecimal bigDecimal) {
        if (bigDecimal.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return bigDecimal;
    }

    @Override
    public boolean setBalance(BigDecimal balance, String currencyAccountID) {
        balance = checkSmallSize(balance);

        if (balance.signum() < 0) {
            JOptionPane.showMessageDialog(null,"Error --> Negative amount!");
            return false;
        }

        this.balances.set(getIndex(currencyAccountID), balance);
        boolean isSuccess;
        String sql = "UPDATE bankaccount SET balance=? WHERE currencyaccountID=?";

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setBigDecimal(1, balance);
            statement.setString(2, currencyAccountID);
            statement.executeUpdate();
            isSuccess = true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Error --> Cannot Update Balance!");
            isSuccess = false;
        }
        return isSuccess;
    }

    @Override
    public boolean setCurrency(String currency, String currencyAccountID) {
        this.currencies.set(getIndex(currencyAccountID), currency);
        boolean isSuccess;
        String sql = "UPDATE bankaccount SET currency=? WHERE currencyaccountID=?";

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, currency);
            statement.setString(2, currencyAccountID);
            statement.executeUpdate();
            isSuccess = true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Error --> Cannot Update Currency!");
            isSuccess = false;
        }
        return isSuccess;
    }

    @Override
    public List<String> getCurrencyAccountIDs() {
        return currencyAccountIDs;
    }

    @Override
    public List<BigDecimal> getAllBalances() {
        return balances;
    }

    @Override
    public List<String> getAllCurrencies() {
        return currencies;
    }

    @Override
    public BigDecimal getBalance(String currencyAccountID) {
        BigDecimal balance = balances.get(getIndex(currencyAccountID));
        balance = checkSmallSize(balance);
        return balance;
    }

    @Override
    public String getCurrency(String currencyAccountID) {
        return currencies.get(getIndex(currencyAccountID));
    }

    @Override
    public boolean withdraw(BigDecimal amount, String accountID, String currencyAccountID) {
        BigDecimal balance = getBalance(currencyAccountID);

        if (amount.signum() <= 0) {
            JOptionPane.showMessageDialog(null,"Error --> Non-positive amount!");
            return false;
        }

        if (balance.compareTo(amount) < 0) {
            JOptionPane.showMessageDialog(null,"Error --> Amount exceeds the balance!");
            return false;
        }

        setBalance(balance.subtract(amount), currencyAccountID);
        addAction(accountID, "withdraw " + amount + " c.u. from " + currencyAccountID);

        return true;
    }

    @Override
    public boolean deposit(BigDecimal amount, String accountID, String currencyAccountID) {
        if (amount.signum() <= 0) {
            JOptionPane.showMessageDialog(null,"Error --> Non-positive amount!");
            return false;
        }

        BigDecimal balance = getBalance(currencyAccountID);
        setBalance(balance.add(amount), currencyAccountID);
        addAction(accountID, "deposit " + amount + " c.u. from " + currencyAccountID);

        return true;
    }

    @Override
    public boolean loadData(String accountID) {
        boolean isSuccess;
        clearData();
        String sql = "SELECT currencyaccountID, balance, currency FROM bankaccount WHERE accountID=? ORDER BY currencyaccountID";

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, accountID);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    currencyAccountIDs.add(rs.getString("currencyaccountID"));
                    balances.add(rs.getBigDecimal("balance"));
                    currencies.add(rs.getString("currency"));
                }
            }
            isSuccess = true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Load Bank Account Unsuccessful!");
            isSuccess = false;
        }
        return isSuccess;
    }

    @Override
    public boolean addAction(String accountID, String note) {
        boolean isSuccess;
        String sql = "INSERT INTO activity(accountID, actionID, action, actiondatetime) VALUES(?, ?, ?, ?)";

        List<String> actionIDs = getActionIDs(accountID);
        if (actionIDs.isEmpty()) {
            actionIDs.add(accountID + "0000");
        }
        String actionID = Admin.createID(actionIDs);

        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, accountID);
            statement.setString(2, actionID);
            statement.setString(3, note);
            statement.setString(4, currentTime);
            statement.executeUpdate();
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Error --> Cannot Create an Action Note!");
            isSuccess = false;
        }
        return isSuccess;
    }

    @Override
    public List<String> getActions(String accountID) {
        List<String> result = new ArrayList<>();
        String sql = "SELECT actionID, action, actiondatetime FROM activity WHERE accountID=? ORDER BY actiondatetime";

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, accountID);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    result.add(rs.getString("actionID") + " - " + rs.getString("action") +
                               " - " + rs.getString("actiondatetime"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Error --> Cannot Get Actions Data!");
        }
        return result;
    }

    @Override
    public List<String> getActionIDs(String accountID) {
        List<String> result = new ArrayList<>();
        String sql = "SELECT actionID FROM activity WHERE accountID=?";

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, accountID);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    result.add(rs.getString("actionID"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Error --> Cannot Get Action IDs!");
        }
        return result;
    }

    /**
     * Creates an account with the given information, writes it to the database
     */
    @Override
    public boolean createAccount(String accountID, String newCurrencyAccountID, BigDecimal balance, String currency) {
        balance = checkSmallSize(balance);

        if (balance.signum() < 0) {
            JOptionPane.showMessageDialog(null,"Error --> Negative amount!");
            return false;
        }

        boolean isSuccess;
        String sql = "INSERT INTO bankaccount(accountID, currencyaccountID, balance, currency) " +
                "VALUES(?, ?, ?, ?)";

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, accountID);
            statement.setString(2, newCurrencyAccountID);
            statement.setBigDecimal(3, balance);
            statement.setString(4, currency);
            statement.executeUpdate();
            isSuccess = true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Error --> Cannot Create a New Account!");
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
        String sql = "DELETE FROM bankaccount WHERE currencyaccountID=?";

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, currencyAccountID);
            statement.executeUpdate();
            isSuccess = true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Error --> Cannot Delete Account!");
            isSuccess = false;
        }
        clearData();
        return isSuccess;
    }
}
