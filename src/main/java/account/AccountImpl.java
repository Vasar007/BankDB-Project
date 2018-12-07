package account;

import javax.swing.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONValue;


/**
 * Connects to the database and realizes important functionalities
 * findAccountID(), loadUserData(),createAccount(), deleteAccount()...
 */

class AccountImpl implements Account{
	private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	private String ID;
	private String SIN;
	private String lastActivity;
	private String name;
	private String lastName;
	private Date birthDate;
	private BankAccount bankAccount;

	private Connection con;


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

		// Load user data
		findAccountID(username);
		loadUserData(ID);
	}

	/**
	 * Finds the accountID of the given username
	 * @param username of type String
     */
	private void findAccountID(String username) {
		String sql = "SELECT accountID FROM account WHERE username=?";

		try (PreparedStatement statement = con.prepareStatement(sql)) {
			statement.setString(1, username);

			try (ResultSet rs = statement.executeQuery()) {
				while (rs.next()) {
					ID = rs.getString("accountID");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"Load Account Data by Username Unsuccessful!");
		}
	}

	/**
	 * Loads data from the database for the given ID
	 * @param ID of type int
     */
	private void loadUserData(String ID) {
		String sql1 = "SELECT SIN, lastactivity FROM bankdb WHERE accountID=?";
		String sql2 = "SELECT name, lastname, birthdate FROM clientinfo WHERE SIN=?";

		try (PreparedStatement statement1 = con.prepareStatement(sql1)) {
			con.setAutoCommit(false);
			statement1.setString(1, ID);

			try (ResultSet rs = statement1.executeQuery()) {
				while (rs.next()) {
					// Retrieve by column name
					SIN = rs.getString("SIN");
					lastActivity = rs.getString("lastactivity");
				}
			}

			try (PreparedStatement statement2 = con.prepareStatement(sql2)) {
				statement2.setString(1, SIN);

				try (ResultSet rs = statement2.executeQuery()) {
					while (rs.next()) {
						// Retrieve by column name
						name = rs.getString("name");
						lastName = rs.getString("lastname");
						birthDate = rs.getDate("birthdate");
					}
				}

				bankAccount.loadData(ID);
			}

			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				System.err.print("Transaction is being rolled back");
				con.rollback();
			} catch(SQLException ex) {
				ex.printStackTrace();
			}

			JOptionPane.showMessageDialog(null,"Load Data by Customer ID Unsuccessful!");
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Gets the accountID matching the username
	 */
	 public String findUserName(String ID) {
		String username = null;
		String sql = "SELECT username FROM account WHERE accountID=?";

		try (PreparedStatement statement = con.prepareStatement(sql)) {
			statement.setString(1, ID);

			try (ResultSet rs = statement.executeQuery()) {
				while (rs.next()) {
					username = rs.getString("username");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"Load Data by Customer Name Unsuccessful!");
		}
		return username;
	}

	public void setUserData(String name, String lastName, String SIN, String ID, String lastActivity,
                            String birthDate) throws Exception {
		if (!name.matches("[A-Za-z]+")) {
			throw new Exception("Name contains forbidden symbols!");
		}
		this.name = name;

		if (!lastName.matches("[A-Za-z]+")) {
			throw new Exception("Name contains forbidden symbols!");
		}
        this.lastName = lastName;

		if (!SIN.matches("[0-9]{7}")) {
			throw new Exception();
		}
        this.SIN = SIN;

        this.ID = ID;
        this.lastActivity = lastActivity;

        try {
            java.util.Date parsed = format.parse(birthDate);
            this.birthDate = new java.sql.Date(parsed.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            throw new Exception("Wrong date format!");
        }
    }

	public static Map<String, Object> getJSONMapFromResultSet(ResultSet rs, String keyName) {
	 	Map<String, Object> json = new HashMap<>();
		List<Map<String, Object>> list = new ArrayList<>();
		if (rs != null) {
			try {
				ResultSetMetaData metaData = rs.getMetaData();
				while (rs.next()) {
					Map<String,Object> columnMap = new HashMap<>();
					for (int columnIndex = 1; columnIndex <= metaData.getColumnCount(); columnIndex++) {
						if (rs.getString(metaData.getColumnName(columnIndex)) != null)
							columnMap.put(metaData.getColumnLabel(columnIndex),
										  rs.getString(metaData.getColumnName(columnIndex)));
						else {
							columnMap.put(metaData.getColumnLabel(columnIndex), "null");
						}
					}
					list.add(columnMap);
				}
				json.put(keyName, list);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return json;
	}

	public String getJSON() {
		Map<String, Object> resultJson = new HashMap<>();

	 	String sql1 = "SELECT * FROM account WHERE accountID=?";
	 	String sql2 = "SELECT * FROM bankaccount WHERE accountID=?";
	 	String sql3 = "SELECT * FROM bankdb WHERE accountID=?";
	 	String sql4 = "SELECT * FROM clientinfo WHERE SIN=?";

		try (PreparedStatement statement1 = con.prepareStatement(sql1)) {
			statement1.setString(1, ID);

			try (ResultSet rs = statement1.executeQuery()) {
				resultJson.putAll(getJSONMapFromResultSet(rs, "account"));
			}

			try (PreparedStatement statement2 = con.prepareStatement(sql2)) {
				statement2.setString(1, ID);

				try (ResultSet rs = statement2.executeQuery()) {
					resultJson.putAll(getJSONMapFromResultSet(rs, "bankaccount"));
				}
			}

			try (PreparedStatement statement3 = con.prepareStatement(sql3)) {
				statement3.setString(1, ID);

				try (ResultSet rs = statement3.executeQuery()) {
					resultJson.putAll(getJSONMapFromResultSet(rs, "bankdb"));
				}
			}

			try (PreparedStatement statement4 = con.prepareStatement(sql4)) {
				statement4.setString(1, SIN);

				try (ResultSet rs = statement4.executeQuery()) {
					resultJson.putAll(getJSONMapFromResultSet(rs, "clientinfo"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"Error --> Cannot Get Data from DB!");
		}

		return JSONValue.toJSONString(resultJson);
	}

	@Override
	public boolean setName(String name) {
	 	boolean isSuccess;
		String sql = "UPDATE clientinfo SET name=? WHERE SIN=?";

		// Name must be alphabetical
		 try (PreparedStatement statement = con.prepareStatement(sql)) {
			 if (!name.matches("[A-Za-z]+")) {
				 throw new Exception("Name contains forbidden symbols!");
			 } else {
				 this.name = name;

				 statement.setString(1, name);
				 statement.setString(2, SIN);
				 statement.executeUpdate();

				 isSuccess = true;
			 }
		 } catch (Exception e) {
             e.printStackTrace();
			 JOptionPane.showMessageDialog(null,"Error --> Cannot Update Name!");
			 isSuccess = false;
		 }

		return isSuccess;
	 }

	@Override
	public boolean setLastName(String lastName) {
	 	boolean isSuccess;
		String sql = "UPDATE clientinfo SET lastname=? WHERE SIN=?";

		 try (PreparedStatement statement = con.prepareStatement(sql)) {
			 if (!lastName.matches("[A-Za-z]+")) {
				 throw new Exception("Last name contains forbidden symbols!");
			 } else {
				 this.lastName = lastName;

				 statement.setString(1, lastName);
				 statement.setString(2, SIN);
				 statement.executeUpdate();

				 isSuccess = true;
			 }
		 } catch (Exception e) {
             e.printStackTrace();
			 JOptionPane.showMessageDialog(null,"Error --> Cannot Update Last Name!");
			 isSuccess = false;
		 }

		 return isSuccess;
	 }

	@Override
	public boolean setBirthDate(String birthDate) {
		boolean isSuccess;
		String sql = "UPDATE clientinfo SET birthdate=? WHERE SIN=?";

		try (PreparedStatement statement = con.prepareStatement(sql)) {
			java.util.Date parsed = format.parse(birthDate);
			this.birthDate = new java.sql.Date(parsed.getTime());

			statement.setDate(1, this.birthDate);
			statement.setString(2, SIN);
			statement.executeUpdate();
			isSuccess = true;
		} catch (Exception e) {
            e.printStackTrace();
			JOptionPane.showMessageDialog(null,"Error --> Cannot Update Birth Date!");
			isSuccess = false;
		}

		return isSuccess;
	 }

	@Override
	public boolean setSIN(String SIN) {
		 boolean isSuccess;
		String sql = "UPDATE bankdb SET SIN=? WHERE accountID=?";

		 try (PreparedStatement statement = con.prepareStatement(sql)) {
			 if (!SIN.matches("[0-9]{7}")) {
				 throw new Exception();
			 } else {
				 statement.setString(1, SIN);
				 statement.setString(2, ID);
				 statement.executeUpdate();

				 this.SIN = SIN;
				 isSuccess = true;
			 }
		 } catch (Exception e) {
             e.printStackTrace();
			 JOptionPane.showMessageDialog(null,"Error --> Cannot Update SIN!");
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
		String sql = "UPDATE bankdb SET lastactivity=? WHERE accountID=?";

		 try (PreparedStatement statement = con.prepareStatement(sql)) {
			 statement.setString(1, lastActivity);
			 statement.setString(2, ID);
			 statement.executeUpdate();
			 isSuccess = true;

		 } catch (SQLException e) {
             e.printStackTrace();
			 JOptionPane.showMessageDialog(null,"Error --> Cannot Update Last Activity!");
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
	public boolean createAccount() {
		boolean isSuccess;
		String sql1 = "INSERT INTO bankdb(accountID, SIN, lastactivity) VALUES(?, ?, ?);";
		String sql2 = "INSERT INTO account(accountID, username, password) VALUES(?, ?, ?); ";
		String sql3 = "INSERT INTO clientinfo(name, lastname, birthdate, SIN) VALUES(?, ?, ?, ?); ";

		try (PreparedStatement statement1 = con.prepareStatement(sql1)) {
			con.setAutoCommit(false);
			statement1.setString(1, ID);
			statement1.setString(2, SIN);
			statement1.setString(3, "None");
			statement1.executeUpdate();

			try (PreparedStatement statement2 = con.prepareStatement(sql2)) {
				statement2.setString(1, ID);
				statement2.setString(2, name);
				statement2.setString(3, SIN);
				statement2.executeUpdate();
			}

			try (PreparedStatement statement3 = con.prepareStatement(sql3)) {
				statement3.setString(1, name);
				statement3.setString(2, lastName);
				statement3.setDate(3, birthDate);
				statement3.setString(4, SIN);
				statement3.executeUpdate();
			}

			con.commit();
			isSuccess = true;
		} catch (SQLException e) {
            e.printStackTrace();
			try {
				System.err.print("Transaction is being rolled back.");
				con.rollback();
			} catch(SQLException ex) {
				ex.printStackTrace();
			}

			JOptionPane.showMessageDialog(null,"Error --> Cannot Create a New User!");
			isSuccess = false;
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return isSuccess;
	}

	/**
	 * Deletes an account
	 */
	@Override
	public boolean deleteAccount(String accountID) {
		boolean isSuccess;
		String sql1 = "DELETE FROM bankdb WHERE accountID=?";
		String sql2 = "DELETE FROM account WHERE accountID=?";
		String sql3 = "DELETE FROM bankaccount WHERE accountID=?";
		String sql4 = "DELETE FROM clientinfo WHERE SIN=?";

		try (PreparedStatement statement1 = con.prepareStatement(sql1)) {
			con.setAutoCommit(false);
			statement1.setString(1, accountID);
			statement1.executeUpdate();

			try (PreparedStatement statement2 = con.prepareStatement(sql2)) {
				statement2.setString(1, accountID);
				statement2.executeUpdate();
			}

			try (PreparedStatement statement3 = con.prepareStatement(sql3)) {
				statement3.setString(1, accountID);
				statement3.executeUpdate();
			}

			try (PreparedStatement statement4 = con.prepareStatement(sql4)) {
				statement4.setString(1, SIN);
				statement4.executeUpdate();
			}

			con.commit();
			isSuccess = true;
		} catch (Exception e) {
            e.printStackTrace();
			try {
				System.err.print("Transaction is being rolled back.");
				con.rollback();
			} catch(SQLException ex) {
				ex.printStackTrace();
			}

            JOptionPane.showMessageDialog(null,"Error --> Cannot Delete!");
            isSuccess = false;
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return isSuccess;
	}

	@Override
	public boolean createBankAccount(String newCurrencyAccountID, int balleft, int balright,
							         String currency) {
		return bankAccount.createAccount(ID, newCurrencyAccountID, balleft, balright, currency);
	}

	@Override
	public void updateBankAccount() {
		bankAccount.loadData(ID);
	}

	@Override
	public boolean deleteBankAccount(String currencyAccountID) {
		return bankAccount.deleteAccount(currencyAccountID);
	}
}
