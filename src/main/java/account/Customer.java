package account;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
    private JLabel IDLabel;
    private JLabel lastActivityLabel;
    private JLabel balLeftLabel;
    private JLabel balRightLabel;
    private JLabel currencyLabel;
    private JFrame parent;

    private String selectUserBankAccount = null;

    private ChequingAccount user;
    private IdleListener timer;


    Customer(String username, JFrame parent) {
        super("**** Customer Console : " + username + " ****");
        this.parent = parent;
        timer = new IdleListener(180);

        user = new ChequingAccount(username);
        initialize();

        timer.startTimer();

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
        balLeftLabel.setText("");
        balRightLabel.setText("");
        currencyLabel.setText("");
        lastActivityLabel.setText(user.getLastActivity());
        IDLabel.setText(user.getID());
    }

    /**
     * Retrieves the account information
     */
    private void getAccountData(String bankAccount){
        balLeftLabel.setText(Integer.toString(user.getBalLeft(bankAccount)));
        balRightLabel.setText(Integer.toString(user.getBalRight(bankAccount)));
        currencyLabel.setText(user.getCurrency(bankAccount));
    }

    /**
     * Initializes and creates all the visual elements required for this Frame
     */
    private void initialize() {
        setSize(500, 700);
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

        // Line1 (AccountID)
        JPanel p = new JPanel();

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
        p.add(jsonButton);
        p.add(accID);
        p.add(IDLabel);

        // Line2 ( name )
        JPanel panel1 = new JPanel();
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
        panel1.add(nameTextField);
        panel1.add(updateNameButton);

        // Line3 ( lastActivityLabel name )
        JPanel panel2 = new JPanel();
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
        panel2.add(lastNameTextField);
        panel2.add(updateLastNameButton);

        // Line4 ( Social Insurance Number )
        JPanel panel3 = new JPanel();
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
        panel3.add(SINTextField);
        panel3.add(updateSINButton);

        // Line5 ( BirthDate )
        JPanel panel4 = new JPanel();
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
        panel4.add(birthDateTextField);
        panel4.add(updateBirthDateButton);

        // Balance Field
        JPanel panel5 = new JPanel();
        JLabel label5 = new JLabel("Balance: ");
        balLeftLabel = new JLabel("N/A");//balLeft
        JLabel label7 = new JLabel(".");
        balRightLabel = new JLabel("N/A"); // balRight
        currencyLabel = new JLabel("N/A");//currency

        label5.setFont(sansSerifBoldLarge);
        balLeftLabel.setFont(sansSerifBoldLarge);
        label7.setFont(sansSerifBoldLarge);
        balRightLabel.setFont(sansSerifBoldLarge);
        currencyLabel.setFont(sansSerifBoldLarge);
        panel5.setForeground(foregroundColor);
        panel5.add(label5);
        panel5.add(balLeftLabel);
        panel5.add(label7);
        panel5.add(balRightLabel);
        panel5.add(currencyLabel);

        // Deposit Field
        JPanel panel6 = new JPanel();
        depositTextField = new JTextField();
        depositTextField.setPreferredSize(new Dimension(200,40));
        depositTextField.setFont(sansSerifBoldBig);
        depositTextField.setBorder(BorderFactory.createTitledBorder(new TitledBorder("Enter Amount:")));
        depositTextField.setForeground(foregroundColor);
        JButton button6 = new JButton("Deposit");
        button6.setPreferredSize(new Dimension(140,40));
        button6.setFont(sansSerifBold);
        button6.setBackground(Color.RED);
        button6.setForeground(backgroundColor);
        button6.setActionCommand("DEPOSIT");
        button6.addActionListener(this);
        panel6.add(button6);
        panel6.add(depositTextField);

        // Withdraw Field
        JPanel panel7 = new JPanel();
        withdrawTextField = new JTextField();
        withdrawTextField.setPreferredSize(new Dimension(200,40));
        withdrawTextField.setFont(sansSerifBoldBig);
        withdrawTextField.setBorder(BorderFactory.createTitledBorder(new TitledBorder("Enter Amount:")));
        withdrawTextField.setForeground(foregroundColor);
        JButton button7 = new JButton("Withdraw");
        button7.setPreferredSize(new Dimension(140,40));
        button7.setFont(sansSerifBold);
        button7.setBackground(Color.BLUE);
        button7.setForeground(backgroundColor);
        button7.setActionCommand("WITHDRAW");
        button7.addActionListener(this);
        panel7.add(button7);
        panel7.add(withdrawTextField);

        // Last Activity Line
        JPanel lastActivity = new JPanel();
        JLabel la = new JLabel("Last Activity: ");
        la.setFont(sansSerifItalic);
        lastActivityLabel = new JLabel("N/A");
        lastActivityLabel.setFont(sansSerifItalic);
        lastActivity.add(la);
        lastActivity.add(lastActivityLabel);

        JPanel midPanel = new JPanel();
        infoPanel.add(p);
        infoPanel.add(panel1);
        infoPanel.add(panel2);
        infoPanel.add(panel3);
        infoPanel.add(panel4);
        balancePanel.add(panel5);
        balancePanel.add(panel6);
        balancePanel.add(panel7);
        balancePanel.add(lastActivity);

        midPanel.add(selectPanel);
        midPanel.add(infoPanel);
        midPanel.add(balancePanel);

        this.add(topPanel,BorderLayout.PAGE_START);
        this.add(midPanel,BorderLayout.CENTER);
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
            try{
                boolean isSuccess = user.setSIN(SINTextField.getText());
                if (isSuccess) updateSINButton.setBackground(successColor);
                else updateSINButton.setBackground(failureColor);
            } catch (NumberFormatException e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,"Error --> Please Enter an Integer");
                updateSINButton.setBackground(failureColor);
            }
        }

        else if ("DATE".equals(event.getActionCommand())) {
            boolean isSuccess = user.setBirthDate(birthDateTextField.getText());
            if (isSuccess) updateBirthDateButton.setBackground(successColor);
            else updateBirthDateButton.setBackground(failureColor);
        }

        else if ("DEPOSIT".equals(event.getActionCommand())) {
            try {
                user.deposit(Integer.parseInt(depositTextField.getText()), selectUserBankAccount);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,"Error --> Please Enter an Integer");
            }
            balLeftLabel.setText(Integer.toString(user.getBalLeft(selectUserBankAccount)));
        }

        else if ("WITHDRAW".equals(event.getActionCommand())) {
            try {
                user.withdraw(Integer.parseInt(withdrawTextField.getText()), selectUserBankAccount);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,"Error --> Please Enter an Integer( Multiples of 20 )");
            }
            balLeftLabel.setText(Integer.toString(user.getBalLeft(selectUserBankAccount)));
        }

        else if ("OUTJSON".equals(event.getActionCommand())) {
            // create a JTextArea
            JTextArea textArea = new JTextArea(25, 75);
            textArea.setLineWrap(true);
            textArea.setText(user.getJSON()
                    .replace("},", "},\n\t")
                    .replace("],", "\n],\n\n")
                    .replace("[{", "[\n\t{"));
            textArea.setEditable(false);
            textArea.setFont(sansSerifBoldLarge);

            // wrap a scrollpane around it
            JScrollPane scrollPane = new JScrollPane(textArea);

            // display them in a message dialog
            JOptionPane.showMessageDialog(null, scrollPane);
        }
    }
}
