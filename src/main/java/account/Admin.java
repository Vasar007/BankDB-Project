package account;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Creates the UI for Admin Panel and listens to the events on that panel.
 */
class Admin extends JFrame implements ActionListener {
    private static final Color backgroundColor = Color.WHITE;
    private static final Color foregroundColor = Color.BLACK;
    private static final Color regularColor = Color.BLUE;
    private static final Font font = new Font("SansSerif", Font.BOLD, 16);
    private static final Font font2 = new Font("SansSerif", Font.BOLD, 22);
    private static final Font font3 = new Font("SansSerif",  Font.ITALIC, 14);

    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JButton button;
    private JLabel last;
    private JComboBox customersListCB;
    private JComboBox customerBankAccountsList;

    private String selectUserID = null;
    private String selectUserBankAccount = null;

    private String newID;
    private ChequingAccount user;
    private ArrayList<String> customersList = new ArrayList<>();
    private ArrayList<String> customerBankAccounts = new ArrayList<>();
    private IdleListener timer;

    private JFrame parent;


    Admin(JFrame parent) {
        super("**** Admin Console  ****");
        this.parent = parent;
        timer = new IdleListener(180);

        initialize();

        timer.startTimer();

        // create an ID for the new user
        newID = createID(customersList);

        user = new ChequingAccount(newID, true);
    }

    /**
     * Retrieves the user information
     */
    private void getUserData(String ID){
        user.setID(ID);
        textField1.setText(user.getName());
        textField2.setText(user.getLastName());
        textField3.setText(user.getSIN());
        textField4.setText(user.getBirthDate().toString());
        last.setText(user.getLastActivity());
    }

    /**
     * Retrieves the account information
     */
    private void getAccountData(String bankAccount){
        textField5.setText(Integer.toString(user.getBalLeft(bankAccount)));
        textField6.setText(Integer.toString(user.getBalRight(bankAccount)));
        textField7.setText(user.getCurrency(bankAccount));
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
        JPanel p = new JPanel();
        p.add(logout);

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

        customerBankAccountsList = new JComboBox();
        customerBankAccountsList.addItem("-- Create New --");

        customerBankAccountsList.setBorder(BorderFactory.createTitledBorder(new TitledBorder("Please Select a Customer Bank Account to Edit")));
        customerBankAccountsList.setPreferredSize(new Dimension(300,60));
        customerBankAccountsList.setSelectedIndex(0);
        customerBankAccountsList.setActionCommand("ACCOUNTLIST");
        customerBankAccountsList.addActionListener(this);
        selectUserBankAccount = (String) customerBankAccountsList.getSelectedItem();

        selectPanel.add(customerBankAccountsList);

        // Line2 ( name )
        JPanel panel1 = new JPanel();
        textField1 = new JTextField();
        textField1.setPreferredSize(new Dimension(200,40));
        textField1.setFont(font);
        textField1.setBorder(BorderFactory.createTitledBorder(new TitledBorder("Name:")));
        textField1.setForeground(foregroundColor);
        // Line3 ( last name )
        textField2 = new JTextField();
        textField2.setPreferredSize(new Dimension(200,40));
        textField2.setFont(font);
        textField2.setBorder(BorderFactory.createTitledBorder(new TitledBorder("Last Name:")));
        textField2.setForeground(foregroundColor);
        panel1.add(textField1);
        panel1.add(textField2);

        // Line4 ( Social Insurance Number )
        JPanel panel2 = new JPanel();
        textField3 = new JTextField();
        textField3.setPreferredSize(new Dimension(200,40));
        textField3.setFont(font);
        textField3.setBorder(BorderFactory.createTitledBorder(new TitledBorder("SIN:")));
        textField3.setForeground(foregroundColor);
        // Line5 ( BirthDate )
        textField4 = new JTextField();
        textField4.setPreferredSize(new Dimension(200,40));
        textField4.setFont(font);
        textField4.setBorder(BorderFactory.createTitledBorder(new TitledBorder("Birth Date:")));
        textField4.setForeground(foregroundColor);
        panel2.add(textField3);
        panel2.add(textField4);

        // Balance Field
        JPanel panel3 = new JPanel();
        textField5 = new JTextField();
        textField5.setPreferredSize(new Dimension(150,40));
        textField5.setFont(font);
        textField5.setHorizontalAlignment(SwingConstants.RIGHT);
        textField5.setBorder(BorderFactory.createTitledBorder(new TitledBorder("Balance:")));
        textField5.setForeground(foregroundColor);
        JLabel dot = new JLabel(".");
        dot.setFont(font2);
        textField6 = new JTextField();
        textField6.setPreferredSize(new Dimension(100,40));
        textField6.setFont(font);
        textField6.setBorder(BorderFactory.createTitledBorder(new TitledBorder("Balance:")));
        textField6.setForeground(foregroundColor);
        textField7 = new JTextField();
        textField7.setPreferredSize(new Dimension(90,40));
        textField7.setFont(font);
        textField7.setBorder(BorderFactory.createTitledBorder(new TitledBorder("Currency:")));
        textField7.setForeground(foregroundColor);

        panel3.add(textField5);
        panel3.add(dot);
        panel3.add(textField6);
        panel3.add(textField7);

        // SAVE Button
        JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayout(1, 3,5,5));

        button = new JButton();
        ImageIcon s1 = UserInterface.createImageIcon("images/save.png");
        ImageIcon s2 = UserInterface.createImageIcon("images/save2.png");
        button.setIcon(s1);
        button.setPressedIcon(s2);
        button.setBackground(Color.BLUE);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setSize(new Dimension(10, 10));
        button.setActionCommand("SAVE");
        button.addActionListener(this);

        panel4.add(button);
        panel4.add(deleteCustomer);
        panel4.add(deleteAccount);

        // Last Activity Line
        JPanel lastActivity = new JPanel();
        JLabel la = new JLabel("Last Activity: ");
        la.setFont(font3);
        last = new JLabel("N/A");
        last.setFont(font3);
        lastActivity.add(la);
        lastActivity.add(last);

        JPanel midPanel = new JPanel();
        JPanel bothPanel = new JPanel();
        bothPanel.setLayout(new GridLayout(2,1));

        infoPanel.add(p);
        infoPanel.add(panel1);
        infoPanel.add(panel2);

        balancePanel.add(panel3);

        bothPanel.add(panel4);
        bothPanel.add(lastActivity);

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
    private static String createID(ArrayList<String> list) {
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
     * @throws Exception
     */
    private void updateInfo(ChequingAccount user) throws Exception {
        Boolean[] isSuccess = new Boolean[4];

        isSuccess[0] = user.setName(textField1.getText());
        isSuccess[1] = user.setLastName(textField2.getText());

        try {
            isSuccess[2] = user.setSIN(textField3.getText());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error --> Please Enter a valid SIN");
        }

        isSuccess[3] = user.setBirthDate(textField4.getText());

        for (Boolean element : isSuccess) {
            if (!element) {
                throw new Exception();
            }
        }
        JOptionPane.showMessageDialog(null, "New Customer Account Info --> "+ user.getID() + " Added!");
    }

    /**
     * Updates the current user's info to be displayed
     * @param user of type ChequingAccount
     * @throws Exception
     */
    private void updateAccount(ChequingAccount user) throws Exception {
        Boolean[] isSuccess = new Boolean[3];

        try {
            isSuccess[0] = user.setBalLeft(Integer.parseInt(textField5.getText()), selectUserBankAccount);
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error --> Please Enter an Integer");
        }

        try {
            isSuccess[1] = user.setBalRight(Integer.parseInt(textField6.getText()), selectUserBankAccount);
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error --> Please Enter an Integer");
        }

        isSuccess[2] = user.setCurrency(textField7.getText(), selectUserBankAccount);

        for (Boolean element : isSuccess) {
            if (!element) {
                throw new Exception();
            }
        }
        JOptionPane.showMessageDialog(null, "New Customer Account --> "+ user.getID() + " Added!");
    }

    public void createBankAccount(String newID){
        if (customerBankAccounts.isEmpty())
            customerBankAccounts.add(newID + "00");
        String newCurrencyAccountID = createID(customerBankAccounts);

        int balleft = 0;
        int balright = 0;

        try {
            balleft = Integer.parseInt(textField5.getText());
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error --> Please Enter an Integer in left field!");
        }

        try {
            balright = Integer.parseInt(textField6.getText());
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error --> Please Enter an Integer in right field!");
        }

        String currency = textField7.getText();

        user.createBankAccount(newID, newCurrencyAccountID, balleft, balright, currency);

        JOptionPane.showMessageDialog(null, "New Bank Account --> "+ newCurrencyAccountID + " Added!");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.eventDispatched(e);

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
                //System.out.println(Integer.parseInt(selectUserID));
                try {
                    if (selectUserID != null) {
                        getUserData(selectUserID);

                        customerBankAccounts = MySQLConnect.getCustomersBankAccounts(user.getID());
                        customerBankAccountsList.removeAllItems();
                        for (String item : customerBankAccounts) {
                            customerBankAccountsList.addItem(item);
                        }
                        customerBankAccountsList.addItem("-- Create New --");
                        customerBankAccountsList.setSelectedIndex(0);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    textField1.setText("");
                    textField2.setText("");
                    textField3.setText("");
                    textField4.setText("");
                    last.setText("N/A");
                }
            }
        }

        // Event 2 ---> Admin Clicks on the Selection Box and choose account
        if ("ACCOUNTLIST".equals(event.getActionCommand())) {
            button.setBackground(regularColor);

            if (event.getSource() instanceof JComboBox) {
                selectUserBankAccount = (String) customerBankAccountsList.getSelectedItem();
                //System.out.println(Integer.parseInt(selectUserBankAccount));
                try {
                    if (selectUserBankAccount != null) {
                        getAccountData(selectUserBankAccount);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    textField5.setText("");
                    textField6.setText("");
                    textField7.setText("");
                }
            }
        }

        // Event 3 - Admin clicks on SAVE button
        try {
            if ("SAVE".equals(event.getActionCommand())) {
                if (selectUserID.equals("-- Create New --")) {
                    ChequingAccount newUser = new ChequingAccount(newID, true);

                    newUser.name = textField1.getText();
                    newUser.lastName = textField2.getText();
                    newUser.SIN = textField3.getText();
                    newUser.ID = newID;
                    newUser.lastActivity = "None";
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date parsed = format.parse(textField4.getText());
                    newUser.birthDate = new java.sql.Date(parsed.getTime());

                    newUser.createAccount(newID);
                    updateInfo(newUser);
                    createBankAccount(newID);

                    customersList = MySQLConnect.getCustomers();

                    customersListCB.removeAllItems();
                    for (String item : customersList) {
                        customersListCB.addItem(item);
                    }
                    customersListCB.addItem("-- Create New --");
                    customersListCB.setSelectedIndex(0);

                } else if (selectUserBankAccount.equals("-- Create New --")) {
                    createBankAccount(user.getID());
                } else {
                    updateInfo(user);
                    updateAccount(user);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error --> Cannot Write to the Database");
        }

        //Event 4 - Admin presses on Logout button
        if ("LOGOUT".equals(event.getActionCommand())){
            JOptionPane.showMessageDialog(null,"Logged out successfully!");
//            System.exit(0);
            UserInterface.close(this);
            parent.setVisible(true);
        }

        //Event 5 - Admin presses on Delete Customer button
        if ("DELETECUSTOMER".equals(event.getActionCommand())) {
            switch (selectUserID) {
                case "-- Create New --":
                    JOptionPane.showMessageDialog(null, "No User Selected!");
                    break;
                case "0":
                    JOptionPane.showMessageDialog(null, "Cannot Delete Admin!");
                    break;
                default:
                    String userID = user.getID();
                    int response = JOptionPane.showConfirmDialog(null, "Delete " + user.findUserName(userID) + " ?");

                    if (response == JOptionPane.OK_OPTION) {
                        JOptionPane.showMessageDialog(null, "ID "+ userID + " Has Been Successfully Deleted!");
                        user.deleteAccount(user.getID());

                        customersList = MySQLConnect.getCustomers();

                        customersListCB.removeAllItems();
                        for (String item : customersList) {
                            customersListCB.addItem(item);
                        }
                        customersListCB.addItem("-- Create New --");
                        customersListCB.setSelectedIndex(0);
                    }
                    break;
            }
        }

        //Event 6 - Admin presses on Delete Account button
        if ("DELETEACCOUNT".equals(event.getActionCommand())) {
            switch (selectUserBankAccount) {
                case "-- Create New --":
                    JOptionPane.showMessageDialog(null, "No Account Selected!");
                    break;
                case "0":
                    JOptionPane.showMessageDialog(null, "Cannot Delete Admin!");
                    break;
                default:
                    int response = JOptionPane.showConfirmDialog(null, "Delete Customer Account " + selectUserBankAccount + " ?");

                    if (response == JOptionPane.OK_OPTION) {
                        JOptionPane.showMessageDialog(null, "ID "+ selectUserBankAccount + " Has Been Successfully Deleted!");
                        user.deleteBankAccount(selectUserBankAccount);
                    }
                    break;
            }
        }
    }
}
