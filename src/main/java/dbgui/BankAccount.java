package dbgui;

import java.math.BigDecimal;
import java.util.List;


public interface BankAccount {
    boolean setBalance(BigDecimal balance, String currencyAccountID);
    boolean setCurrency(String currency, String currencyAccountID);

    List<String> getCurrencyAccountIDs();
    List<BigDecimal> getAllBalances();
    List<String> getAllCurrencies();
    BigDecimal getBalance(String currencyAccountID);
    String getCurrency(String currencyAccountID);

    boolean withdraw(BigDecimal amount, String accountID, String currencyAccountID);
    boolean deposit(BigDecimal amount, String accountID, String currencyAccountID);

    boolean loadData(String accountID);

    boolean addAction(String accountID, String action,String note, String currencyAccountID);
    List<String> getActions(String accountID);
    List<String> getActionIDs(String accountID);

    boolean createAccount(String accountID, String newCurrencyAccountID, BigDecimal balance, String currency);
    boolean deleteAccount(String currencyAccountID);
}
