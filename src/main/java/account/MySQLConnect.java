package account;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


class MySQLConnect {
    static boolean status;
    private static Connection con;

    /**
     * Creates a connection and returns for further use
     */
    static Connection ConnectDB() {
        try {
            // The newInstance() call is a work around for some
            // broken Java implementations.
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
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Returns the list of customers from the database
     * @return list of type ArrayList
     */
    static ArrayList<String> getCustomers(){
        ArrayList<String> list = new ArrayList<>();

        // Get the accountIDs
        try {
            Statement statement = con.createStatement();
            String sql = "SELECT accountID FROM bankdb";
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                list.add(rs.getString("accountID"));
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
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

        // Get the customer currencyaccountIDs
        try {
            Statement statement = con.createStatement();
            String sql = "SELECT currencyaccountID FROM bankaccount WHERE accountID='" + ID + "'";
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                list.add(rs.getString("currencyaccountID"));
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"Load Bank Accounts Unsuccessful!");
        }

        return list;
    }
}
