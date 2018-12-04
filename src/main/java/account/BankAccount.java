package account;

import java.util.List;


public interface BankAccount {
    //Setters
    boolean setBalLeft(int balLeft, String currencyAccountID);
    boolean setBalRight(int balRight, String currencyAccountID);
    boolean setCurrency(String currency, String currencyAccountID);

    // Getters
    List<String> getCurrencyAccountIDs();
    List<Integer> getAllBalLeft();
    List<Integer> getAllBalRight();
    List<String> getAllCurrencies();
    int getBalLeft(String currencyAccountID);
    int getBalRight(String currencyAccountID);
    String getCurrency(String currencyAccountID);

    boolean withdraw(int amount, String currencyAccountID);
    boolean deposit(int amount, String currencyAccountID);

    void loadData(String accountID);

    boolean createAccount(String accountID, String newCurrencyAccountID, int balleft, int balright,
                          String currency);
    boolean deleteAccount(String currencyAccountID);
}
