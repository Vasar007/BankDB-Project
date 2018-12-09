package dbgui;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;


public interface Account {
	//Setters
	boolean setName(String name);
    boolean setLastName(String lastName);
    boolean setBirthDate(String birthDate);
    boolean setSIN(String SIN);
    boolean setID(String ID); //admin
    boolean setBalance(BigDecimal balance, String currencyAccountID);
    boolean setCurrency(String currency, String currencyAccountID);
    boolean setLastActivity(String lastActivity);//auto

	// Getters
	String getName();
	String getLastName();
	Date getBirthDate();
	String getSIN();
	String getID();
	List<String> getCurrencyAccountIDs();
	List<BigDecimal> getAllBalances();
	List<String> getAllCurrencies();
	BigDecimal getBalance(String currencyAccountID);
	String getCurrency(String currencyAccountID);
    String getLastActivity();

	boolean withdraw(BigDecimal amount, String currencyAccountID);
	boolean deposit(BigDecimal amount, String currencyAccountID);
	
	boolean createAccount();
	boolean deleteAccount();

	boolean createBankAccount(String newCurrencyAccountID, BigDecimal balance, String currency);
	void updateBankAccount();
	boolean deleteBankAccount(String currencyAccountID);

	List<String> getActions();
}
