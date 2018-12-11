package dbgui;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.*;
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
    private String ID;
	private String lastActivity;
	private ClientInfo clientInfo;
	private BankAccount bankAccount;

	private Connection con;


	/**
	 * Constructor Method - connects to the database and loads the admin
	 */
	AccountImpl(String ID, boolean flag) {
		// Create a connection to the Database
        init();

		// Load user data
		findUserName(ID);
		loadUserData(ID);
	}

	/**
	 * Constructor Method - connects to the database and loads the user data
	 */
	AccountImpl(String username) {
		// Create a connection to the Database
        init();

		// Load user data
		findAccountID(username);
		loadUserData(ID);
	}

	private void init() {
        con = MySQLConnect.ConnectDB();
        clientInfo = new ClientInfoImpl(con);
        bankAccount = new BankAccountImpl(con);
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
					clientInfo.setSIN_NotLoadToDB(rs.getString("SIN"));
					lastActivity = rs.getString("lastactivity");
				}
			}

			try (PreparedStatement statement2 = con.prepareStatement(sql2)) {
				statement2.setString(1, getSIN());

				try (ResultSet rs = statement2.executeQuery()) {
					while (rs.next()) {
						// Retrieve by column name
                        setName(rs.getString("name"), false);
                        setLastName(rs.getString("lastname"), false);
                        setBirthDate(rs.getDate("birthdate"), false);
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
                            String birthDate) {
		setName(name, false);
		setLastName(lastName, false);
		setSIN(SIN, false);
        this.ID = ID;
        this.lastActivity = lastActivity;
        setBirthDate(birthDate, false);
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
						if (rs.getString(metaData.getColumnName(columnIndex)) != null) {
                            columnMap.put(metaData.getColumnLabel(columnIndex),
                                    rs.getString(metaData.getColumnName(columnIndex)));
                        } else {
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
	 	String sql5 = "SELECT * FROM activity WHERE accountID=?";

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
				statement4.setString(1, getSIN());

				try (ResultSet rs = statement4.executeQuery()) {
					resultJson.putAll(getJSONMapFromResultSet(rs, "clientinfo"));
				}
			}

			try (PreparedStatement statement5 = con.prepareStatement(sql5)) {
				statement5.setString(1, ID);

				try (ResultSet rs = statement5.executeQuery()) {
					resultJson.putAll(getJSONMapFromResultSet(rs, "activity"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"Error --> Cannot Get Data from DB!");
		}
		return JSONValue.toJSONString(resultJson);
	}

	@Override
	public boolean setName(String name, boolean makeNote) {
	 	boolean result = clientInfo.setName(name);
	 	if (result && makeNote) {
	 	    bankAccount.addAction(ID, "update", "name", null);
        }
	 	return result;
	 }

	@Override
	public boolean setLastName(String lastName, boolean makeNote) {
        boolean result = clientInfo.setLastName(lastName);
        if (result && makeNote) {
            bankAccount.addAction(ID, "update", "last name", null);
        }
        return result;
	 }

	@Override
	public boolean setBirthDate(String birthDate, boolean makeNote) {
        boolean result = clientInfo.setBirthDate(birthDate);
        if (result && makeNote) {
            bankAccount.addAction(ID, "update", "birth date", null);
        }
        return result;
	 }

    @Override
    public boolean setBirthDate(Date birthDate, boolean makeNote) {
        boolean result = clientInfo.setBirthDate(birthDate);
        if (result && makeNote) {
            bankAccount.addAction(ID, "update", "birth date", null);
        }
        return result;
    }

	@Override
	public boolean setSIN(String SIN, boolean makeNote) {
        boolean result = clientInfo.setSIN(SIN);
        if (result && makeNote) {
            bankAccount.addAction(ID, "update", "SIN", null);
        }
        return result;
	}

	@Override
	public boolean setID(String ID) {
 		this.ID = ID;
		loadUserData(ID);
		return true;
	}

	@Override
	public boolean setBalance(BigDecimal balance, String currencyAccountID) {
	 	return bankAccount.setBalance(balance, currencyAccountID);
	 }

	@Override
	public boolean setCurrency(String currency, String currencyAccountID) {
		return bankAccount.setCurrency(currency, currencyAccountID);
	 }

	@Override
	public boolean setLastActivity(java.util.Date lastActivity) {
		 boolean isSuccess;
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(lastActivity);
        this.lastActivity = currentTime;

        String sql = "UPDATE bankdb SET lastactivity=? WHERE accountID=?";

        try (PreparedStatement statement = con.prepareStatement(sql)) {

            statement.setString(1, currentTime);
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
		return clientInfo.getName();
	 }

	@Override
	public String getLastName() {
		return clientInfo.getLastName();
	 }

	@Override
	public Date getBirthDate() {
		return clientInfo.getBirthDate();
	 }

	@Override
	public String getSIN() {
		return clientInfo.getSIN();
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
	public List<BigDecimal> getAllBalances() {
		return bankAccount.getAllBalances();
	}

	@Override
	public List<String> getAllCurrencies() {
		return bankAccount.getAllCurrencies();
	}

	@Override
	public BigDecimal getBalance(String currencyAccountID) {
		return bankAccount.getBalance(currencyAccountID);
	 }

	@Override
	public String getCurrency(String currencyAccountID) {
        return bankAccount.getCurrency(currencyAccountID);
	}

	@Override
	public String getLastActivity() {
	    if (lastActivity == null) {
            return "None";
        }
	 	return lastActivity;
	}

	/**
	 * Withdraw method is used to withdraw value from the balance
	 */
	@Override
	public boolean withdraw(BigDecimal amount, String currencyAccountID) {
        return bankAccount.withdraw(amount, ID, currencyAccountID);
	}

	/**
	 * Deposit method used to deposit some amount to the account
	 */
	@Override
	public boolean deposit(BigDecimal amount, String currencyAccountID) {
        return bankAccount.deposit(amount, ID, currencyAccountID);
	}

	/**
	 * Creates an account with the given information, writes it to the database
	 */
	@Override
	public boolean createAccount() {
		boolean isSuccess;
		String sql1 = "INSERT INTO bankdb(accountID, SIN, lastactivity) VALUES(?, ?, ?)";
		String sql2 = "INSERT INTO account(accountID, username, password) VALUES(?, ?, ?)";
		String sql3 = "INSERT INTO clientinfo(name, lastname, birthdate, SIN) VALUES(?, ?, ?, ?)";

		try (PreparedStatement statement1 = con.prepareStatement(sql1)) {
			con.setAutoCommit(false);
			statement1.setString(1, ID);
			statement1.setString(2, getSIN());
			statement1.setString(3, null);
			statement1.executeUpdate();

			try (PreparedStatement statement2 = con.prepareStatement(sql2)) {
				statement2.setString(1, ID);
				statement2.setString(2, getName().replace(" ", ""));
				statement2.setString(3, SafetyPassword.hashPassword(getSIN()));
				statement2.executeUpdate();
			}

			try (PreparedStatement statement3 = con.prepareStatement(sql3)) {
				statement3.setString(1, getName());
				statement3.setString(2, getLastName());
				statement3.setDate(3, getBirthDate());
				statement3.setString(4, getSIN());
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
				isSuccess = false;
			}
		}
		return isSuccess;
	}

	/**
	 * Deletes an account
	 */
	@Override
	public boolean deleteAccount() {
		boolean isSuccess;
		String sql1 = "DELETE FROM bankdb WHERE accountID=?";
		String sql2 = "DELETE FROM account WHERE accountID=?";
		String sql3 = "DELETE FROM bankaccount WHERE accountID=?";
		String sql4 = "DELETE FROM clientinfo WHERE SIN=?";
		String sql5 = "DELETE FROM activity WHERE accountID=?";

		try (PreparedStatement statement1 = con.prepareStatement(sql1)) {
			con.setAutoCommit(false);
			statement1.setString(1, ID);
			statement1.executeUpdate();

			try (PreparedStatement statement2 = con.prepareStatement(sql2)) {
				statement2.setString(1, ID);
				statement2.executeUpdate();
			}

			try (PreparedStatement statement3 = con.prepareStatement(sql3)) {
				statement3.setString(1, ID);
				statement3.executeUpdate();
			}

			try (PreparedStatement statement4 = con.prepareStatement(sql4)) {
				statement4.setString(1, getSIN());
				statement4.executeUpdate();
			}

            try (PreparedStatement statement5 = con.prepareStatement(sql5)) {
                statement5.setString(1, ID);
                statement5.executeUpdate();
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
				isSuccess = false;
			}
		}
		return isSuccess;
	}

	@Override
	public boolean createBankAccount(String newCurrencyAccountID, BigDecimal balance,
							         String currency) {
		return bankAccount.createAccount(ID, newCurrencyAccountID, balance, currency);
	}

	@Override
	public void updateBankAccount() {
		bankAccount.loadData(ID);
	}

	@Override
	public boolean deleteBankAccount(String currencyAccountID) {
		return bankAccount.deleteAccount(currencyAccountID);
	}

	@Override
	public List<String> getActions() {
		return bankAccount.getActions(ID);
	}
}
