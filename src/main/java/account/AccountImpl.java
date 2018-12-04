package account;

import javax.swing.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.List;


/**
 * Connects to the database and realizes important functionalities
 * findAccountID(), loadUserData(),createAccount(), deleteAccount()...
 */

class AccountImpl implements Account{
	public String ID;
	public String SIN;
	public String lastActivity;
	public String name;
	public String lastName;
	public Date birthDate;
	public BankAccount bankAccount;

	private Connection con;
	private Statement statement;

	/**
	 * Constructor Method - connects to the database and loads the admin
	 */
	AccountImpl(String ID, boolean flag) {
		// Create a connection to the Database
		con = MySQLConnect.ConnectDB();
		bankAccount = new BankAccountImpl(con);

		// load user data.
		findUserName(ID);
		loadUserData(ID);
	}

	/**
	 * Constructor Method - connects to the database and loads the user data
	 */
	AccountImpl(String username) {
		// Create a connection to the Database
		con = MySQLConnect.ConnectDB();
		bankAccount = new BankAccountImpl(con);

		// load user data
		findAccountID(username);
		loadUserData(ID);
	}

	/**
	 * Finds the accountID of the given username
	 * @param username of type String
     */
	private void findAccountID(String username) {
		// Get the accountID matching the username
		try {
			statement = con.createStatement();
			String sql = "SELECT accountID FROM account WHERE username='" + username + "'";
			ResultSet rs = statement.executeQuery(sql);

			while (rs.next()) {
				ID = rs.getString("accountID");
			}
			rs.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
			JOptionPane.showMessageDialog(null,"Load Account Data by Username Unsuccessful!");
		}
	}

	/**
	 * Loads data from the database for the given ID
	 * @param ID of type int
     */
	private void loadUserData(String ID) {
		try {
			statement = con.createStatement();
			String sql = "SELECT SIN, lastactivity FROM bankdb WHERE accountID='" + ID + "'";
			ResultSet rs = statement.executeQuery(sql);

			while (rs.next()) {
				//Retrieve by column name
				SIN = rs.getString("SIN");
				lastActivity = rs.getString("lastactivity");
			}
			rs.close();

			String sql2 = "SELECT name, lastname, birthdate FROM clientinfo WHERE SIN='" + SIN + "'";
			rs = statement.executeQuery(sql2);
			while (rs.next()) {
				//Retrieve by column name
				name = rs.getString("name");
				lastName = rs.getString("lastname");
				birthDate = rs.getDate("birthdate");
			}
			rs.close();

			bankAccount.loadData(ID);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			JOptionPane.showMessageDialog(null,"Load Data by Customer ID Unsuccessful!");
		}
	}

	/**
	 * Gets the accountID matching the username
	 */
	 String findUserName(String ID) {
		String username = null;
		try {
			statement = con.createStatement();
			String sql = "SELECT username FROM account WHERE accountID='" + ID + "'";
			ResultSet rs = statement.executeQuery(sql);

			while(rs.next()){
				username = rs.getString("username");
			}
			rs.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			JOptionPane.showMessageDialog(null,"Load Data by Customer Name Unsuccessful!");
		}
		return username;
	}

	@Override
	public boolean setName(String name) {
		 this.name = name;
		 boolean isSuccess;

		// Name must be alphabetical
		 try {
			 if (!name.matches("[A-Za-z]+")) {
				 throw new Exception();
			 }
			 else {

				 statement = con.createStatement();
				 String sql = "UPDATE clientinfo SET name='" + name + "' WHERE SIN='" + SIN + "'";
				 statement.executeUpdate(sql);
				 isSuccess = true;
			 }
		 } catch (Exception e) {
			 System.out.println(e.getMessage());
			 JOptionPane.showMessageDialog(null,"Error --> Cannot Update Name");

			 isSuccess = false;
		 }

		return isSuccess;
	 }

	@Override
	public boolean setLastName(String lastName) {
		 this.lastName = lastName;
		 boolean isSuccess;

		 try {
			 if (!lastName.matches("[A-Za-z]+")) {
				 throw new Exception();
			 }
			 else {
				 statement = con.createStatement();
				 String sql = "UPDATE clientinfo SET lastname='" + lastName + "' WHERE SIN='" + SIN + "'";
				 statement.executeUpdate(sql);
				 isSuccess = true;
			 }
		 } catch (Exception e) {
			 System.out.println(e.getMessage());
			 JOptionPane.showMessageDialog(null,"Error --> Cannot Update Last Name");
			 isSuccess = false;
		 }

		 return isSuccess;
	 }

	@Override
	public boolean setBirthDate(String birthDate) {
		boolean isSuccess;

		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date parsed = format.parse(birthDate);
			this.birthDate = new java.sql.Date(parsed.getTime());

			statement = con.createStatement();
			String sql = "UPDATE clientinfo SET birthdate='" + birthDate + "' WHERE SIN='" + SIN + "'";
			statement.executeUpdate(sql);
			isSuccess = true;

		} catch (Exception e) {
			System.out.println(e.getMessage());
			JOptionPane.showMessageDialog(null,"Error --> Cannot Update Birth Date");

			isSuccess = false;
		}

		return isSuccess;
	 }

	@Override
	public boolean setSIN(String SIN) {
		 this.SIN = SIN;
		 boolean isSuccess;

		 try {
			 if (SIN.length() != 7) {
				 throw new Exception();
			 }
			 else {
				 statement = con.createStatement();
				 String sql = "UPDATE bankdb SET SIN='" + SIN + "' WHERE accountID='" + ID + "'";
				 statement.executeUpdate(sql);
				 isSuccess = true;
			 }

		 } catch (Exception e) {
			 System.out.println(e.getMessage());
			 JOptionPane.showMessageDialog(null,"Error --> Cannot Update SIN");

			 isSuccess = false;
		 }

		 return isSuccess;
	 }

	@Override
	public boolean setID(String ID) {
 		 this.ID = ID;
		 loadUserData(ID);

		 return true;
	 }

	@Override
	public boolean setBalLeft(int balLeft, String currencyAccountID) {
	 	return bankAccount.setBalLeft(balLeft, currencyAccountID);
	 }

	@Override
	public boolean setBalRight(int balRight, String currencyAccountID) {
		return bankAccount.setBalRight(balRight, currencyAccountID);
	 }

	@Override
	public boolean setCurrency(String currency, String currencyAccountID) {
		return bankAccount.setCurrency(currency, currencyAccountID);
	 }

	@Override
	public boolean setLastActivity(String lastActivity) {
		 this.lastActivity = lastActivity;
		 boolean isSuccess;

		 try {
			 statement = con.createStatement();
			 String sql = "UPDATE bankdb SET lastactivity='" + lastActivity + "' WHERE accountID='" + ID + "'";
			 statement.executeUpdate(sql);
			 isSuccess = true;

		 } catch (Exception e) {
			 System.out.println(e.getMessage());
			 JOptionPane.showMessageDialog(null,"Error --> Cannot Update Last Activity");

			 isSuccess = false;
		 }

		 return isSuccess;
	 }

	@Override
	public String getName() {
		 return name;
	 }

	@Override
	public String getLastName() {
		 return lastName;
	 }

	@Override
	public Date getBirthDate() {
		 return birthDate;
	 }

	@Override
	public String getSIN() {
		 return SIN;
	 }

	@Override
	public String getID() {
		 return ID;
	 }

	@Override
	public List<String> getCurrencyAccountIDs() {
		return bankAccount.getCurrencyAccountIDs();
	}

	@Override
	public List<Integer> getAllBalLeft() {
		return bankAccount.getAllBalLeft();
	}

	@Override
	public List<Integer> getAllBalRight() {
		return bankAccount.getAllBalRight();
	}

	@Override
	public List<String> getAllCurrencies() {
		return bankAccount.getAllCurrencies();
	}

	@Override
	public int getBalLeft(String currencyAccountID) {
		 return bankAccount.getBalLeft(currencyAccountID);
	 }

	@Override
	public int getBalRight(String currencyAccountID) {
		return bankAccount.getBalRight(currencyAccountID);
	 }

	@Override
	public String getCurrency(String currencyAccountID) {
		return bankAccount.getCurrency(currencyAccountID);
	}

	@Override
	public String getLastActivity() {
	 	return lastActivity;
	}

	/**
	 * Withdraw method is used to withdraw value from the balance
	 */
	@Override
	public boolean withdraw(int amount, String currencyAccountID) {
		return bankAccount.withdraw(amount, currencyAccountID);
	}

	/**
	 * Deposit method used to deposit some amount to the account
	 */
	@Override
	public boolean deposit(int amount, String currencyAccountID) {
		return bankAccount.deposit(amount, currencyAccountID);
	}

	/**
	 * Creates an account with the given information, writes it to the database
	 */
	@Override
	public boolean createAccount(String newID) {
		boolean isSuccess;

		try {
			statement = con.createStatement();
			String sql = "INSERT INTO bankdb(accountID, SIN, lastactivity) " +
					"VALUES('" + newID + "', '" + SIN + "', 'None');";

			String sql2 = "INSERT INTO account(accountID, username, password) " +
					"VALUES('" + newID + "', '" + name + "', '" + SIN + "'); ";

			String sql3 = "INSERT INTO clientinfo(name, lastname, birthdate, SIN) " +
					"VALUES('" + name + "', '" + lastName + "', '" + birthDate + "', '" + SIN + "'); ";

			statement.executeUpdate(sql);
			statement.executeUpdate(sql2);
			statement.executeUpdate(sql3);
			isSuccess = true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Error --> Cannot Create a New User!");
			e.printStackTrace();
			isSuccess = false;
		}

		return isSuccess;
	}

	/**
	 * Deletes an account
	 */
	@Override
	public boolean deleteAccount(String accountID) {
		boolean isSuccess;

		try {
			statement = con.createStatement();
			String sql = "DELETE FROM bankdb WHERE accountID='" + accountID + "'";
			String sql2 = "DELETE FROM account WHERE accountID='" + accountID + "'";
			String sql3 = "DELETE FROM bankaccount WHERE accountID='" + accountID + "'";
			String sql4 = "DELETE FROM clientinfo WHERE SIN='" + SIN + "'";
			statement.executeUpdate(sql);
			statement.executeUpdate(sql2);
			statement.executeUpdate(sql3);
			statement.executeUpdate(sql4);
			isSuccess = true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Error --> Cannot Delete!");
			e.printStackTrace();
			isSuccess = false;
		}
		return isSuccess;
	}

	@Override
	public boolean createBankAccount(String accountID, String newCurrencyAccountID, int balleft, int balright,
							         String currency) {
		return bankAccount.createAccount(accountID, newCurrencyAccountID, balleft, balright, currency);
	}

	@Override
	public boolean deleteBankAccount(String currencyAccountID) {
		return bankAccount.deleteAccount(currencyAccountID);
	}
}
