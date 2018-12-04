package account;

import java.sql.Date;
import java.util.List;


public interface Account {
	//Setters
	boolean setName(String name);
    boolean setLastName(String lastName);
    boolean setBirthDate(String birthDate);
    boolean setSIN(String SIN);
    boolean setID(String ID); //admin
    boolean setBalLeft(int balLeft, String currencyAccountID);
    boolean setBalRight(int balRight, String currencyAccountID); //admin
    boolean setCurrency(String currency, String currencyAccountID); //admin
    boolean setLastActivity(String lastActivity);//auto

	// Getters
	String getName();
	String getLastName();
	Date getBirthDate();
	String getSIN();
	String getID();
	List<String> getCurrencyAccountIDs();
	List<Integer> getAllBalLeft();
	List<Integer> getAllBalRight();
	List<String> getAllCurrencies();
	int getBalLeft(String currencyAccountID);
	int getBalRight(String currencyAccountID);
	String getCurrency(String currencyAccountID);
    String getLastActivity();

	boolean withdraw(int amount, String currencyAccountID);
	boolean deposit(int amount, String currencyAccountID);
	
	boolean createAccount(String ID);
	boolean deleteAccount(String accountID);

	boolean createBankAccount(String accountID, String newCurrencyAccountID, int balleft, int balright,
						      String currency);
	boolean deleteBankAccount(String currencyAccountID);
}
