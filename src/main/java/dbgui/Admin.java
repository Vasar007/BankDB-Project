package dbgui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;


/**
 * Creates the UI for Admin Panel and listens to the events on that panel.
 */
class Admin extends JFrame implements ActionListener {
    private static final Color backgroundColor = Color.WHITE;
    private static final Color foregroundColor = Color.BLACK;
    private static final Color regularColor = Color.BLUE;

    private static final Font sansSerifBoldBig = new Font("SansSerif", Font.BOLD, 16);
    private static final Font sansSerifItalic = new Font("SansSerif",  Font.ITALIC, 14);

    private static final List<String> availableCurrency = Arrays.asList("USD", "EUR", "RUB", "RUPI", "CAD", "GBP",
            "SNI", "GPI", "AUD");

    private JTextField nameTextField;
    private JTextField lastNameTextField;
    private JTextField SINTextField;
    private JTextField birthDateTextField;
    private JTextField balanceTextField;
    private JComboBox<String> currencyCB;
    private JButton saveButton;
    private JLabel lastActivityLabel;
    private JComboBox<String> customersListCB;
    private JComboBox<String> customerBankAccountsList;
    private JFrame parent;

    private String selectUserID = null;
    private String selectUserBankAccount = null;

    private ChequingAccount user;
    private ArrayList<String> customersList = new ArrayList<>();
    private ArrayList<String> customerBankAccounts = new ArrayList<>();


    Admin(JFrame parent) {
        super("**** Admin Console  ****");
        this.parent = parent;

        initialize();

        // create an ID for the new user
        user = new ChequingAccount(createID(customersList), true);
    }

    /**
     * Retrieves the user information
     */
    private void getUserData(String ID) {
        user.setID(ID);
        nameTextField.setText(user.getName());
        lastNameTextField.setText(user.getLastName());
        SINTextField.setText(user.getSIN());
        if (user.getBirthDate() != null) birthDateTextField.setText(user.getBirthDate().toString());
        else birthDateTextField.setText("");
        lastActivityLabel.setText(user.getLastActivity());
    }

    /**
     * Retrieves the account information
     */
    private void getAccountData(String bankAccount) {
        user.updateBankAccount();
        balanceTextField.setText(user.getBalance(bankAccount).toString());
        currencyCB.setSelectedIndex(availableCurrency.indexOf(user.getCurrency(bankAccount)));
    }

    /**
     * Initializes and creates all the visual elements required for this Frame
     */
    @SuppressWarnings("unchecked")
    private void initialize() {
        setSize(500, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel infoPanel = new JPanel();
        JPanel balancePanel = new JPanel();
        JPanel selectPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(3, 2,10,10));
        balancePanel.setLayout(new GridLayout(1, 3,10,10));
        selectPanel.setLayout(new GridLayout(2, 1,10,10));

        // Top image
        JPanel topPanel = new JPanel();
        JLabel logo = new JLabel();
        logo.setHorizontalAlignment(JLabel.LEFT);
        ImageIcon icon = UserInterface.createImageIcon("images/topAdmin.png");
        logo.setIcon(icon);

        logo.setBorder(BorderFactory.createEmptyBorder(10,20,0,20));
        topPanel.add(logo);
        topPanel.setBackground(backgroundColor);
        topPanel.setForeground(foregroundColor);

        // Logout Button
        JButton logout = new JButton();
        ImageIcon l1 = UserInterface.createImageIcon("images/logout.png");
        ImageIcon l2 = UserInterface.createImageIcon("images/logout2.png");
        logout.setIcon(l1);
        logout.setPressedIcon(l2);
        logout.setBackground(backgroundColor);
        logout.setBorderPainted(false);
        logout.setContentAreaFilled(false);
        logout.setFocusPainted(false);
        logout.setSize(new Dimension(10, 10));
        logout.setActionCommand("LOGOUT");
        logout.addActionListener(this);

        // Delete Button
        JButton deleteCustomer = new JButton();
        ImageIcon d1 = UserInterface.createImageIcon("images/delete.png");
        ImageIcon d2 = UserInterface.createImageIcon("images/delete2.png");
        deleteCustomer.setIcon(d1);
        deleteCustomer.setPressedIcon(d2);
        deleteCustomer.setBackground(backgroundColor);
        deleteCustomer.setBorderPainted(false);
        deleteCustomer.setContentAreaFilled(false);
        deleteCustomer.setFocusPainted(false);
        deleteCustomer.setSize(new Dimension(10, 10));
        deleteCustomer.setActionCommand("DELETECUSTOMER");
        deleteCustomer.addActionListener(this);

        JButton deleteAccount = new JButton();
        deleteAccount.setIcon(d1);
        deleteAccount.setPressedIcon(d2);
        deleteAccount.setBackground(backgroundColor);
        deleteAccount.setBorderPainted(false);
        deleteAccount.setContentAreaFilled(false);
        deleteAccount.setFocusPainted(false);
        deleteAccount.setSize(new Dimension(10, 10));
        deleteAccount.setActionCommand("DELETEACCOUNT");
        deleteAccount.addActionListener(this);

        // Remaining Time

        // List Selection of Accounts in the database
        JPanel selectionAccountsPanel = new JPanel();
        selectionAccountsPanel.add(logout);

        customersList = MySQLConnect.getCustomers();

        customersListCB = new JComboBox(customersList.toArray());
        customersListCB.addItem("-- Create New --");

        customersListCB.setBorder(BorderFactory.createTitledBorder(new TitledBorder("Please Select a Customer to Edit")));
        customersListCB.setPreferredSize(new Dimension(300,60));
        customersListCB.setSelectedIndex(customersList.size());
        customersListCB.setActionCommand("CUSTOMERLIST");
        customersListCB.addActionListener(this);
        selectUserID = (String) customersListCB.getSelectedItem();

        selectPanel.add(customersListCB);

        customerBankAccountsList = new JComboBox<>();
        customerBankAccountsList.addItem("-- Create New --");

        customerBankAccountsList.setBorder(BorderFactory.createTitledBorder(new TitledBorder("Please Select a Customer Bank Account to Edit")));
        customerBankAccountsList.setPreferredSize(new Dimension(300,60));
        customerBankAccountsList.setSelectedIndex(0);
        customerBankAccountsList.setActionCommand("ACCOUNTLIST");
        customerBankAccountsList.addActionListener(this);
        selectUserBankAccount = (String) customerBankAccountsList.getSelectedItem();

        selectPanel.add(customerBankAccountsList);

        // Line2 ( name )
        JPanel namePanel = new JPanel();
        nameTextField = new JTextField();
        nameTextField.setPreferredSize(new Dimension(200,40));
        nameTextField.setFont(sansSerifBoldBig);
        nameTextField.setBorder(BorderFactory.createTitledBorder(new TitledBorder("Name:")));
        nameTextField.setForeground(foregroundColor);

        // Line3 ( lastActivityLabel name )
        lastNameTextField = new JTextField();
        lastNameTextField.setPreferredSize(new Dimension(200,40));
        lastNameTextField.setFont(sansSerifBoldBig);
        lastNameTextField.setBorder(BorderFactory.createTitledBorder(new TitledBorder("Last Name:")));
        lastNameTextField.setForeground(foregroundColor);
        namePanel.add(nameTextField);
        namePanel.add(lastNameTextField);

        // Line4 ( Social Insurance Number )
        JPanel SINPanel = new JPanel();
        SINTextField = new JTextField();
        SINTextField.setPreferredSize(new Dimension(200,40));
        SINTextField.setFont(sansSerifBoldBig);
        SINTextField.setBorder(BorderFactory.createTitledBorder(new TitledBorder("SIN:")));
        SINTextField.setForeground(foregroundColor);

        // Line5 ( BirthDate )
        birthDateTextField = new JTextField();
        birthDateTextField.setPreferredSize(new Dimension(200,40));
        birthDateTextField.setFont(sansSerifBoldBig);
        birthDateTextField.setBorder(BorderFactory.createTitledBorder(new TitledBorder("Birth Date:")));
        birthDateTextField.setForeground(foregroundColor);
        SINPanel.add(SINTextField);
        SINPanel.add(birthDateTextField);

        // Balance Field
        JPanel balanceField = new JPanel();
        balanceTextField = new JTextField();
        balanceTextField.setPreferredSize(new Dimension(250,50));
        balanceTextField.setFont(sansSerifBoldBig);
        balanceTextField.setHorizontalAlignment(SwingConstants.RIGHT);
        balanceTextField.setBorder(BorderFactory.createTitledBorder(new TitledBorder("Balance:")));
        balanceTextField.setForeground(foregroundColor);

        currencyCB = new JComboBox(availableCurrency.toArray());
        currencyCB.setPreferredSize(new Dimension(150,50));
        currencyCB.setSelectedIndex(0);
        currencyCB.setFont(sansSerifBoldBig);
        currencyCB.setBorder(BorderFactory.createTitledBorder(new TitledBorder("Currency:")));
        currencyCB.setForeground(foregroundColor);

        balanceField.add(balanceTextField);
        balanceField.add(currencyCB);

        // SAVE Button
        JPanel saveButtonPanel = new JPanel();
        saveButtonPanel.setLayout(new GridLayout(1, 3,5,5));

        saveButton = new JButton();
        ImageIcon s1 = UserInterface.createImageIcon("images/save.png");
        ImageIcon s2 = UserInterface.createImageIcon("images/save2.png");
        saveButton.setIcon(s1);
        saveButton.setPressedIcon(s2);
        saveButton.setBackground(Color.BLUE);
        saveButton.setBorderPainted(false);
        saveButton.setContentAreaFilled(false);
        saveButton.setFocusPainted(false);
        saveButton.setSize(new Dimension(10, 10));
        saveButton.setActionCommand("SAVE");
        saveButton.addActionListener(this);

        saveButtonPanel.add(saveButton);
        saveButtonPanel.add(deleteCustomer);
        saveButtonPanel.add(deleteAccount);

        // Last Activity Line
        JPanel lastActivityFiledPanel = new JPanel();
        JLabel lastActivityTitleLabel = new JLabel("Last Activity: ");
        lastActivityTitleLabel.setFont(sansSerifItalic);
        lastActivityLabel = new JLabel("N/A");
        lastActivityLabel.setFont(sansSerifItalic);
        lastActivityFiledPanel.add(lastActivityTitleLabel);
        lastActivityFiledPanel.add(lastActivityLabel);

        JPanel midPanel = new JPanel();
        JPanel bothPanel = new JPanel();
        bothPanel.setLayout(new GridLayout(2,1));

        infoPanel.add(selectionAccountsPanel);
        infoPanel.add(namePanel);
        infoPanel.add(SINPanel);

        balancePanel.add(balanceField);

        bothPanel.add(saveButtonPanel);
        bothPanel.add(lastActivityFiledPanel);

        midPanel.add(selectPanel);
        midPanel.add(infoPanel);
        midPanel.add(balancePanel);
        midPanel.add(bothPanel);


        this.add(topPanel,BorderLayout.PAGE_START);
        this.add(midPanel,BorderLayout.CENTER);
    }

    /**
     * Creates a new ID for the new user
     * @param list of type of ArrayList<String>
     * @return ID of type int
     */
    public static String createID(List<String> list) {
        ArrayList<Long> newList = new ArrayList<>();

        Long max = 0L;
        for (String s : list) {
            try {
                Long temp = Long.parseLong(s);
                if (temp > max) max = temp;
                newList.add(temp);
            } catch (NumberFormatException nfe) {
                System.out.println("Error -> Could not parse " + nfe);
            }
        }

        long newID = max + 1;
        while (newList.contains(newID)) {
            newID += 1;
        }

       return Long.toString(newID);
    }

    /**
     * Updates the current user's info to be displayed
     * @param user of type ChequingAccount
     */
    private void updateInfo(ChequingAccount user) throws Exception {
        Boolean[] isSuccess = new Boolean[4];

        isSuccess[0] = user.setName(nameTextField.getText(), true);
        isSuccess[1] = user.setLastName(lastNameTextField.getText(), true);
        isSuccess[2] = user.setSIN(SINTextField.getText(), true);
        isSuccess[3] = user.setBirthDate(birthDateTextField.getText(), true);

        for (Boolean element : isSuccess) {
            if (!element) {
                throw new Exception("Got error from customer setters.");
            }
        }
        JOptionPane.showMessageDialog(null,
                "Customer Account --> " + user.getID() + " Was Updated!");
    }

    /**
     * Updates the current user's info to be displayed
     * @param user of type ChequingAccount
     */
    private void updateAccount(ChequingAccount user) throws Exception {
        Boolean[] isSuccess = new Boolean[2];

        try {
            isSuccess[0] = user.setBalance(new BigDecimal(balanceTextField.getText()), selectUserBankAccount);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error --> Please Enter a Valid Number");
        }

        String currency = (String) currencyCB.getSelectedItem();
        assert currency != null;
        isSuccess[1] = user.setCurrency(currency.toUpperCase(), selectUserBankAccount);

        for (Boolean element : isSuccess) {
            if (!element) {
                throw new Exception("Got error from account setters.");
            }
        }
        JOptionPane.showMessageDialog(null,
                "Bank Account --> " + selectUserBankAccount + " Was Updated!");
    }

    private boolean createBankAccount() {
        if (customerBankAccounts.isEmpty()) {
            customerBankAccounts.add(user.getID() + "00");
        }
        String newCurrencyAccountID = createID(customerBankAccounts);

        BigDecimal balance;

        try {
            balance = new BigDecimal(balanceTextField.getText());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error --> Please Enter an Valid Integer value in balance fields!");
            return false;
        }

        String currency = (String) currencyCB.getSelectedItem();
        assert currency != null;

        boolean result = user.createBankAccount(newCurrencyAccountID, balance, currency.toUpperCase());
        if (result) {
            JOptionPane.showMessageDialog(null,
                    "New Bank Account --> " + newCurrencyAccountID + " Added!");
        }
        return result;
    }

    private static void updateList(JComboBox<String> comboBox, ArrayList<String> newList) {
        comboBox.removeAllItems();
        for (String item : newList) {
            comboBox.addItem(item);
        }
        comboBox.addItem("-- Create New --");

        if (newList.isEmpty()) comboBox.setSelectedIndex(0);
        else comboBox.setSelectedIndex(newList.size() - 1);
    }

    private void clearCustomerTextFields() {
        nameTextField.setText("");
        lastNameTextField.setText("");
        SINTextField.setText("");
        birthDateTextField.setText("");
        lastActivityLabel.setText("N/A");
    }

    private void clearAccountTextFields() {
        balanceTextField.setText("");
        currencyCB.setSelectedIndex(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        eventHandler(e);
    }

    /**
     * Handles the events depending on their status code
     * @param event of type ActionEvent
     */
    @SuppressWarnings("unchecked")
    private void eventHandler(ActionEvent event){
        // Event 1 ---> Admin Clicks on the Selection Box and choose customer
        if ("CUSTOMERLIST".equals(event.getActionCommand())) {
            JComboBox<String> cb = (JComboBox<String>) event.getSource();

            if (event.getSource() instanceof JComboBox) {
                selectUserID = (String) cb.getSelectedItem();
                try {
                    if (selectUserID != null) {
                        getUserData(selectUserID);
                        customerBankAccounts = MySQLConnect.getCustomersBankAccounts(user.getID());
                        updateList(customerBankAccountsList, customerBankAccounts);

                        if (selectUserID.equals("-- Create New --")) {
                            clearCustomerTextFields();
                            clearAccountTextFields();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    clearCustomerTextFields();
                }
            }
        }

        // Event 2 ---> Admin Clicks on the Selection Box and choose account
        if ("ACCOUNTLIST".equals(event.getActionCommand())) {
            saveButton.setBackground(regularColor);

            if (event.getSource() instanceof JComboBox) {
                selectUserBankAccount = (String) customerBankAccountsList.getSelectedItem();
                try {
                    if (selectUserBankAccount != null && !selectUserID.equals("-- Create New --")) {
                        if (!selectUserBankAccount.equals("-- Create New --")) {
                            getAccountData(selectUserBankAccount);
                            getUserData(selectUserID);
                        } else {
                            clearAccountTextFields();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    clearAccountTextFields();
                }
            }
        }

        // Event 3 - Admin clicks on SAVE saveButton
        try {
            if ("SAVE".equals(event.getActionCommand())) {
                if (selectUserID.equals("-- Create New --")) {
                    String newID = createID(customersList);
                    user = new ChequingAccount(newID, true);

                    user.setUserData(nameTextField.getText(), lastNameTextField.getText(), SINTextField.getText(),
                                     newID, "None", birthDateTextField.getText());
                    if (user.createAccount()) {
                        if (createBankAccount()) {
                            customersList = MySQLConnect.getCustomers();
                            updateList(customersListCB, customersList);

                            customerBankAccounts = MySQLConnect.getCustomersBankAccounts(user.getID());
                            updateList(customerBankAccountsList, customerBankAccounts);
                            getUserData(newID);

                            JOptionPane.showMessageDialog(null,
                                    "New Customer Account Info --> "+ user.getID() + " Added  (username: " +
                                            user.getName() + ", password: " + user.getSIN() + ")!");
                        } else {
                            user.deleteAccount();
                        }
                    }
                } else if (selectUserBankAccount.equals("-- Create New --")) {
                    createBankAccount();

                    customerBankAccounts = MySQLConnect.getCustomersBankAccounts(user.getID());
                    updateList(customerBankAccountsList, customerBankAccounts);
                    getUserData(user.getID());
                } else {
                    updateInfo(user);
                    updateAccount(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error --> Cannot Write to the Database!");
        }

        //Event 4 - Admin presses on Logout saveButton
        if ("LOGOUT".equals(event.getActionCommand())){
            JOptionPane.showMessageDialog(null,"Logged out successfully!");
//            System.exit(0);
            UserInterface.close(this);
            parent.setVisible(true);
        }

        //Event 5 - Admin presses on Delete Customer saveButton
        if ("DELETECUSTOMER".equals(event.getActionCommand())) {
            switch (selectUserID) {
                case "-- Create New --":
                    JOptionPane.showMessageDialog(null, "No User Selected!");
                    break;
                case "0":
                    JOptionPane.showMessageDialog(null, "Cannot Delete Admin!");
                    break;
                default:
                    int response = JOptionPane.showConfirmDialog(null,
                            "Delete " + user.findUserName(user.getID()) + " ?",
                            "Delete Confirmation", JOptionPane.YES_NO_OPTION);

                    if (response == JOptionPane.OK_OPTION) {
                        JOptionPane.showMessageDialog(null,
                                "ID "+ user.getID() + " Has Been Successfully Deleted!");
                        user.deleteAccount();

                        customersList = MySQLConnect.getCustomers();
                        updateList(customersListCB, customersList);
                    }
                    break;
            }
        }

        //Event 6 - Admin presses on Delete Account saveButton
        if ("DELETEACCOUNT".equals(event.getActionCommand())) {
            switch (selectUserBankAccount) {
                case "-- Create New --":
                    JOptionPane.showMessageDialog(null, "No Account Selected!");
                    break;
                case "0":
                    JOptionPane.showMessageDialog(null, "Cannot Delete Admin!");
                    break;
                default:
                    int response = JOptionPane.showConfirmDialog(null,
                            "Delete Customer Account " + selectUserBankAccount + " ?",
                            "Delete Confirmation", JOptionPane.YES_NO_OPTION);

                    if (response == JOptionPane.OK_OPTION) {
                        JOptionPane.showMessageDialog(null,
                                "ID "+ selectUserBankAccount + " Has Been Successfully Deleted!");
                        user.deleteBankAccount(selectUserBankAccount);

                        customerBankAccounts = MySQLConnect.getCustomersBankAccounts(user.getID());
                        updateList(customerBankAccountsList, customerBankAccounts);
                        getUserData(user.getID());
                    }
                    break;
            }
        }
    }
}
