package dbgui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;


class Customer extends JFrame implements ActionListener {

    private static final Color backgroundColor = Color.WHITE;
    private static final Color foregroundColor = Color.BLACK;
    private static final Color successColor = Color.GREEN;
    private static final Color failureColor = Color.RED;

    private static final Font sansSerifBold = new Font("SansSerif", Font.BOLD, 14);
    private static final Font sansSerifBoldBig = new Font("SansSerif", Font.BOLD, 16);
    private static final Font sansSerifBoldLarge = new Font("SansSerif", Font.BOLD, 22);
    private static final Font sansSerifItalic = new Font("SansSerif",  Font.ITALIC, 14);

    private JTextField nameTextField;
    private JTextField lastNameTextField;
    private JTextField SINTextField;
    private JTextField birthDateTextField;
    private JTextField depositTextField;
    private JTextField withdrawTextField;
    private JButton updateNameButton;
    private JButton updateLastNameButton;
    private JButton updateSINButton;
    private JButton updateBirthDateButton;
    private JButton depositButton;
    private JButton withdrawButton;
    private JLabel IDLabel;
    private JLabel lastActivityLabel;
    private JLabel balanceLabel;
    private JLabel currencyLabel;
    private JFrame parent;

    private String selectUserBankAccount = null;

    private ChequingAccount user;


    Customer(String username, JFrame parent) {
        super("**** Customer Console : " + username + " ****");
        this.parent = parent;

        user = new ChequingAccount(username);
        initialize();
        getUserData();
    }

    /**
     * Retrieves the user information
     */
    private void getUserData(){
        nameTextField.setText(user.getName());
        lastNameTextField.setText(user.getLastName());
        SINTextField.setText(user.getSIN());
        birthDateTextField.setText(user.getBirthDate().toString());
        balanceLabel.setText("");
        currencyLabel.setText("");
        lastActivityLabel.setText(user.getLastActivity());
        IDLabel.setText(user.getID());
    }

    /**
     * Retrieves the action information
     */
    private void getAccountData(String bankAccount){
        balanceLabel.setText(user.getBalance(bankAccount).toString());
        currencyLabel.setText(user.getCurrency(bankAccount));
    }

    /**
     * Initializes and creates all the visual elements required for this Frame
     */
    private void initialize() {
        setSize(500, 850);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel infoPanel = new JPanel();
        JPanel balancePanel = new JPanel();
        JPanel selectPanel = new JPanel();
        selectPanel.setLayout(new GridLayout(2, 2,1,1));
        infoPanel.setLayout(new GridLayout(5, 1,1,1));
        balancePanel.setLayout(new GridLayout(4, 1,1,1));

        // Top image
        JPanel topPanel = new JPanel();
        JLabel logo = new JLabel();
        logo.setHorizontalAlignment(JLabel.LEFT);
        ImageIcon icon = UserInterface.createImageIcon("images/top.png");
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

        selectPanel.add(logout);

        // JSON Button
        JButton jsonButton = new JButton("Out JSON");
        jsonButton.setFont(sansSerifBold);
        jsonButton.setBackground(foregroundColor);
        jsonButton.setForeground(backgroundColor);
        jsonButton.setPreferredSize(new Dimension(140,40));
        jsonButton.setSize(new Dimension(10, 10));
        jsonButton.setActionCommand("OUTJSON");
        jsonButton.addActionListener(this);

        JButton historyButton = new JButton("Show History");
        historyButton.setFont(sansSerifBold);
        historyButton.setBackground(foregroundColor);
        historyButton.setForeground(backgroundColor);
        historyButton.setPreferredSize(new Dimension(140,40));
        historyButton.setSize(new Dimension(10, 10));
        historyButton.setActionCommand("HISTORY");
        historyButton.addActionListener(this);

        // Line1 (AccountID)
        JPanel accountPanel = new JPanel();
        accountPanel.setLayout(new GridLayout(2, 2,1,1));

        ArrayList<String> customerBankAccounts = MySQLConnect.getCustomersBankAccounts(user.getID());

        JComboBox<String> customerBankAccountsList = new JComboBox(customerBankAccounts.toArray());
        customerBankAccountsList.setBorder(BorderFactory.createTitledBorder(new TitledBorder("Please Select your Bank Account")));
        customerBankAccountsList.setPreferredSize(new Dimension(300,60));
        customerBankAccountsList.setSelectedIndex(-1);
        customerBankAccountsList.setActionCommand("ACCOUNTLIST");
        customerBankAccountsList.addActionListener(this);
        selectUserBankAccount = (String) customerBankAccountsList.getSelectedItem();

        selectPanel.add(customerBankAccountsList);

        JLabel accID = new JLabel("Account ID Label: ");
        IDLabel = new JLabel("N/A");
        accID.setFont(sansSerifBoldBig);
        IDLabel.setFont(sansSerifBoldBig);
        accountPanel.add(jsonButton);
        accountPanel.add(historyButton);
        accountPanel.add(accID);
        accountPanel.add(IDLabel);

        // Line2 ( name )
        JPanel namePanel = new JPanel();
        nameTextField = new JTextField("N/A");
        nameTextField.setPreferredSize(new Dimension(200,40));
        nameTextField.setFont(sansSerifBoldBig);
        nameTextField.setBorder(BorderFactory.createTitledBorder(new TitledBorder("Name:")));
        nameTextField.setForeground(foregroundColor);
        updateNameButton = new JButton("Update");
        updateNameButton.setPreferredSize(new Dimension(140,40));
        updateNameButton.setFont(sansSerifBold);
        updateNameButton.setBackground(foregroundColor);
        updateNameButton.setForeground(backgroundColor);
        updateNameButton.setActionCommand("NAME");
        updateNameButton.addActionListener(this);
        namePanel.add(nameTextField);
        namePanel.add(updateNameButton);

        // Line3 ( lastActivityLabel name )
        JPanel lastActivityPanel = new JPanel();
        lastNameTextField = new JTextField("N/A");
        lastNameTextField.setPreferredSize(new Dimension(200,40));
        lastNameTextField.setFont(sansSerifBoldBig);
        lastNameTextField.setBorder(BorderFactory.createTitledBorder(new TitledBorder("Last Name:")));
        lastNameTextField.setForeground(foregroundColor);

        updateLastNameButton = new JButton("Update");
        updateLastNameButton.setPreferredSize(new Dimension(140,40));
        updateLastNameButton.setFont(sansSerifBold);
        updateLastNameButton.setBackground(foregroundColor);
        updateLastNameButton.setForeground(backgroundColor);
        updateLastNameButton.setActionCommand("LASTNAME");
        updateLastNameButton.addActionListener(this);

        lastActivityPanel.add(lastNameTextField);
        lastActivityPanel.add(updateLastNameButton);

        // Line4 ( Social Insurance Number )
        JPanel SINPanel = new JPanel();
        SINTextField = new JTextField("N/A");
        SINTextField.setPreferredSize(new Dimension(200,40));
        SINTextField.setFont(sansSerifBoldBig);
        SINTextField.setBorder(BorderFactory.createTitledBorder(new TitledBorder("SIN:")));
        SINTextField.setForeground(foregroundColor);
        updateSINButton = new JButton("Update");
        updateSINButton.setPreferredSize(new Dimension(140,40));
        updateSINButton.setFont(sansSerifBold);
        updateSINButton.setBackground(foregroundColor);
        updateSINButton.setForeground(backgroundColor);
        updateSINButton.setActionCommand("SIN");
        updateSINButton.addActionListener(this);
        SINPanel.add(SINTextField);
        SINPanel.add(updateSINButton);

        // Line5 ( BirthDate )
        JPanel birthDatePanel = new JPanel();
        birthDateTextField = new JTextField("N/A");
        birthDateTextField.setPreferredSize(new Dimension(200,40));
        birthDateTextField.setFont(sansSerifBoldBig);
        birthDateTextField.setBorder(BorderFactory.createTitledBorder(new TitledBorder("Birth Date:")));
        birthDateTextField.setForeground(foregroundColor);

        updateBirthDateButton = new JButton("Update");
        updateBirthDateButton.setPreferredSize(new Dimension(140,40));
        updateBirthDateButton.setFont(sansSerifBold);
        updateBirthDateButton.setBackground(foregroundColor);
        updateBirthDateButton.setForeground(backgroundColor);
        updateBirthDateButton.setActionCommand("DATE");
        updateBirthDateButton.addActionListener(this);

        birthDatePanel.add(birthDateTextField);
        birthDatePanel.add(updateBirthDateButton);

        // Balance Field
        JPanel balanceFieldPanel = new JPanel();
        JLabel balanceTitleLabel = new JLabel("Balance: ");
        balanceLabel = new JLabel("N/A");
        currencyLabel = new JLabel("N/A");

        balanceTitleLabel.setFont(sansSerifBoldLarge);
        balanceLabel.setFont(sansSerifBoldLarge);
        currencyLabel.setFont(sansSerifBoldLarge);
        balanceFieldPanel.setForeground(foregroundColor);
        balanceFieldPanel.add(balanceTitleLabel);
        balanceFieldPanel.add(balanceLabel);
        balanceFieldPanel.add(currencyLabel);

        // Deposit Field
        JPanel depositPanel = new JPanel();
        depositTextField = new JTextField();
        depositTextField.setPreferredSize(new Dimension(200,40));
        depositTextField.setFont(sansSerifBoldBig);
        depositTextField.setBorder(BorderFactory.createTitledBorder(new TitledBorder("Enter Amount:")));
        depositTextField.setForeground(foregroundColor);
        depositTextField.setEnabled(false);

        depositButton = new JButton("Deposit");
        depositButton.setPreferredSize(new Dimension(140,40));
        depositButton.setFont(sansSerifBold);
        depositButton.setBackground(Color.RED);
        depositButton.setForeground(backgroundColor);
        depositButton.setActionCommand("DEPOSIT");
        depositButton.addActionListener(this);
        depositButton.setEnabled(false);

        depositPanel.add(depositButton);
        depositPanel.add(depositTextField);

        // Withdraw Field
        JPanel withdrawPanel = new JPanel();
        withdrawTextField = new JTextField();
        withdrawTextField.setPreferredSize(new Dimension(200,40));
        withdrawTextField.setFont(sansSerifBoldBig);
        withdrawTextField.setBorder(BorderFactory.createTitledBorder(new TitledBorder("Enter Amount:")));
        withdrawTextField.setForeground(foregroundColor);
        withdrawTextField.setEnabled(false);

        withdrawButton = new JButton("Withdraw");
        withdrawButton.setPreferredSize(new Dimension(140,40));
        withdrawButton.setFont(sansSerifBold);
        withdrawButton.setBackground(Color.BLUE);
        withdrawButton.setForeground(backgroundColor);
        withdrawButton.setActionCommand("WITHDRAW");
        withdrawButton.addActionListener(this);
        withdrawButton.setEnabled(false);

        withdrawPanel.add(withdrawButton);
        withdrawPanel.add(withdrawTextField);

        // Last Activity Line
        JPanel lastActivityFiledPanel = new JPanel();
        JLabel lastActivityTitleLabel = new JLabel("Last Activity: ");
        lastActivityTitleLabel.setFont(sansSerifItalic);
        lastActivityLabel = new JLabel("N/A");
        lastActivityLabel.setFont(sansSerifItalic);
        lastActivityFiledPanel.add(lastActivityTitleLabel);
        lastActivityFiledPanel.add(lastActivityLabel);

        JPanel midPanel = new JPanel();
        infoPanel.add(accountPanel);
        infoPanel.add(namePanel);
        infoPanel.add(lastActivityPanel);
        infoPanel.add(SINPanel);
        infoPanel.add(birthDatePanel);
        balancePanel.add(balanceFieldPanel);
        balancePanel.add(depositPanel);
        balancePanel.add(withdrawPanel);
        balancePanel.add(lastActivityFiledPanel);

        midPanel.add(selectPanel);
        midPanel.add(infoPanel);
        midPanel.add(balancePanel);

        this.add(topPanel,BorderLayout.PAGE_START);
        this.add(midPanel,BorderLayout.CENTER);
    }

    private void toggleBankAccountButtons(boolean flag) {
        depositTextField.setEnabled(flag);
        withdrawTextField.setEnabled(flag);
        depositButton.setEnabled(flag);
        withdrawButton.setEnabled(flag);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        eventHandler(e);
    }

    /**
     * Handles the events depending on their status code
     * @param event of type ActionEvent
     */
    private void eventHandler(ActionEvent event) {
        Date date = new Date();
        user.setLastActivity(date.toString());
        lastActivityLabel.setText(user.getLastActivity());

        // Event 2 ---> Admin Clicks on the Selection Box and choose account
        if ("ACCOUNTLIST".equals(event.getActionCommand())) {
            JComboBox<String> cb = (JComboBox<String>) event.getSource();

            if (event.getSource() instanceof JComboBox) {
                selectUserBankAccount = (String) cb.getSelectedItem();
                try {
                    if (selectUserBankAccount != null) {
                        getAccountData(selectUserBankAccount);
                        toggleBankAccountButtons(true);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    nameTextField.setText("");
                    lastNameTextField.setText("");
                    SINTextField.setText("");
                    birthDateTextField.setText("");
                    depositTextField.setText("");
                    withdrawTextField.setText("");
                    lastActivityLabel.setText("N/A");

                    toggleBankAccountButtons(false);
                }
            }
        } else if ("LOGOUT".equals(event.getActionCommand())) {
            JOptionPane.showMessageDialog(null,"Logged out successfully!");
//            System.exit(0);
            UserInterface.close(this);
            parent.setVisible(true);
        }

        else if ("NAME".equals(event.getActionCommand())) {
            boolean isSuccess = user.setName(nameTextField.getText());
            if (isSuccess) updateNameButton.setBackground(successColor);
            else updateNameButton.setBackground(failureColor);
        }

        else if ("LASTNAME".equals(event.getActionCommand())) {
            boolean isSuccess = user.setLastName(lastNameTextField.getText());
            if (isSuccess) updateLastNameButton.setBackground(successColor);
            else updateLastNameButton.setBackground(failureColor);
        }

        else if ("SIN".equals(event.getActionCommand())) {
            boolean isSuccess = user.setSIN(SINTextField.getText());
            if (isSuccess) updateSINButton.setBackground(successColor);
            else updateSINButton.setBackground(failureColor);
        }

        else if ("DATE".equals(event.getActionCommand())) {
            boolean isSuccess = user.setBirthDate(birthDateTextField.getText());
            if (isSuccess) updateBirthDateButton.setBackground(successColor);
            else updateBirthDateButton.setBackground(failureColor);
        }

        else if ("DEPOSIT".equals(event.getActionCommand())) {
            try {
                user.deposit(new BigDecimal(depositTextField.getText()), selectUserBankAccount);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,"Error --> Please Enter a Valid Number!");
            }
            balanceLabel.setText(user.getBalance(selectUserBankAccount).toString());
            depositTextField.setText("");
        }

        else if ("WITHDRAW".equals(event.getActionCommand())) {
            try {
                user.withdraw(new BigDecimal(withdrawTextField.getText()), selectUserBankAccount);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,"Error --> Please Enter a Valid Number!");
            }
            balanceLabel.setText(user.getBalance(selectUserBankAccount).toString());
            withdrawTextField.setText("");
        }

        else if ("OUTJSON".equals(event.getActionCommand())) {
            // create a JTextArea
            JTextArea textArea = new JTextArea(25, 100);
            textArea.setLineWrap(true);
            textArea.setText(user.getJSON()
                    .replace("},", "},\n\t")
                    .replace("],", "\n],\n\n")
                    .replace("[{", "[\n\t{"));
            textArea.setEditable(false);
            textArea.setFont(sansSerifBold);

            // wrap a scrollpane around it
            JScrollPane scrollPane = new JScrollPane(textArea);

            // display them in a message dialog
            JOptionPane.showMessageDialog(null, scrollPane);
        }

        else if ("HISTORY".equals(event.getActionCommand())) {
            List<String> result = user.getActions();
            String output = String.join("\n", result);
            if (output.isEmpty()) output = "No Activity Found.";
            JOptionPane.showMessageDialog(null, output);
        }
    }
}
