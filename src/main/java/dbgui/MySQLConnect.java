package dbgui;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;


class MySQLConnect {
    public static boolean status;
    private static Connection con;


    /**
     * Creates a connection and returns for further use
     */
    static Connection ConnectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost/mydb" +
                    "?verifyServerCertificate=false" +
                    "&useSSL=false" +
                    "&requireSSL=false" +
                    "&useUnicode=true" +
                    "&useJDBCCompliantTimezoneShift=true" +
                    "&useLegacyDatetimeCode=false" +
                    "&serverTimezone=UTC";
            String username = "root"; //username
            String password = ""; // There is no password
            con = DriverManager.getConnection(url, username, password);

           // Successfully Connected to the Database
            status = true;

            return con;
        } catch (Exception e) {
            // Connection error
            status = false;
            System.out.println("Error occurred during connection to database!");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns the list of customers from the database
     * @return list of type ArrayList
     */
    static ArrayList<String> getCustomers(){
        ArrayList<String> list = new ArrayList<>();
        String sql = "SELECT accountID FROM bankdb";

        // Get the accountIDs
        try (Statement statement = con.createStatement()) {
            try (ResultSet rs = statement.executeQuery(sql)) {
                while (rs.next()) {
                    list.add(rs.getString("accountID"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Load Customers Unsuccessful!");
        }

        return list;
    }

    /**
     * Returns the list of customer bank accounts from the database
     * @return list of type ArrayList
     */
    static ArrayList<String> getCustomersBankAccounts(String ID){
        ArrayList<String> list = new ArrayList<>();
        String sql = "SELECT currencyaccountID FROM bankaccount WHERE accountID=?";

        // Get the customer currencyaccountIDs
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, ID);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    list.add(rs.getString("currencyaccountID"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Load Bank Accounts Unsuccessful!");
        }

        return list;
    }
}
