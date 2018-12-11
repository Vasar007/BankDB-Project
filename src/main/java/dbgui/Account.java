package dbgui;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;


public interface Account {
    boolean setName(String name, boolean makeNote);
    boolean setLastName(String lastName, boolean makeNote);
    boolean setBirthDate(String birthDate, boolean makeNote);
    boolean setBirthDate(Date birthDate, boolean makeNote);
    boolean setSIN(String SIN, boolean makeNote);
    boolean setID(String ID);
    boolean setBalance(BigDecimal balance, String currencyAccountID);
    boolean setCurrency(String currency, String currencyAccountID);
    boolean setLastActivity(java.util.Date lastActivity); // auto

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
