package dbgui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The User Interface of the Application, manages the login process, contains the main method..
 */
class UserInterface extends JPanel implements ActionListener {
    private static final Color backgroundColor = Color.WHITE;
    private static final Color foregroundColor = Color.BLACK;

    private static JFrame frame;
    private JLabel picture;
    private JPasswordField passwordField;
    private JFormattedTextField usernameField;
    private JProgressBar progress;
    private JLabel status;
    private ImageIcon online;
    private ImageIcon offline;

    private int i = 0;
    private String select = null;

    private static Connection con = null;


    private UserInterface() {
        super(new BorderLayout(1,1));
        initialize();
    }

    /**
     * Initializes and creates all the visual elements required for this Frame
     */
    private void initialize() {

        final String[] str = { "Admin Login", "Customer Login" };

        // Create a connection to the Database
        con = MySQLConnect.ConnectDB();

        // Set up the online/offline status indicator.
        status = new JLabel();
        status.setHorizontalAlignment(JLabel.LEFT);
        online = createImageIcon("images/online.png");
        offline = createImageIcon("images/offline.png");
        status.setBorder(BorderFactory.createEmptyBorder(10,20,0,20));

        status();

        // Initialize the 3 Panels (left - auth - right)
        JPanel leftPanel = new JPanel();
        JPanel authPanel = new JPanel();
        JPanel rightPanel = new JPanel();
        JPanel bottomPanel = new JPanel();

        // Change their layouts and background color
        leftPanel.setBackground(backgroundColor);
        authPanel.setBackground(backgroundColor);
        rightPanel.setBackground(backgroundColor);
        leftPanel.setLayout(new GridLayout(1,1));
        authPanel.setLayout(new GridLayout(4,4));
        rightPanel.setLayout(new GridLayout(4,4));

        // Create the combo box, select the item at index 1.
        JComboBox<java.lang.String> list = new JComboBox<>(str);
        list.setSelectedIndex(1);
        select = (String)list.getSelectedItem();
        list.setBorder(BorderFactory.createTitledBorder(new TitledBorder("Select Role:")));
        list.setBackground(backgroundColor);
        list.setForeground(foregroundColor);
        list.setActionCommand("LIST");
        list.addActionListener(this);

        // Set up the picture.
        picture = new JLabel();
        picture.setFont(picture.getFont().deriveFont(Font.ITALIC));
        picture.setHorizontalAlignment(JLabel.RIGHT);
        updateLabel(str[list.getSelectedIndex()]);
        picture.setBorder(BorderFactory.createEmptyBorder(10,20,0,20));
        picture.setPreferredSize(new Dimension(177, 122+10));

        // Set up the logo.
        JLabel logo = new JLabel();
        logo.setHorizontalAlignment(JLabel.LEFT);
        ImageIcon icon = createImageIcon("images/logo.png");
        logo.setIcon(icon);
        logo.setBorder(BorderFactory.createEmptyBorder(10,20,0,20));

        // Set up the password field
        passwordField = new JPasswordField(10);
        passwordField.setHorizontalAlignment(SwingConstants.CENTER);
        passwordField.setMinimumSize(new Dimension(400, 20));
        passwordField.setBorder(BorderFactory.createTitledBorder(new TitledBorder("Password:")));
        passwordField.setBackground(backgroundColor);
        passwordField.setForeground(foregroundColor);
        passwordField.setSelectedTextColor(backgroundColor);
        passwordField.setSelectionColor(foregroundColor);
        passwordField.setActionCommand("ENTER");
        passwordField.addActionListener(this);

        //Set up the usernameField field
        usernameField = new JFormattedTextField();
        usernameField.setHorizontalAlignment(SwingConstants.CENTER);
        usernameField.setBorder(BorderFactory.createTitledBorder(new TitledBorder("User Name:")));
        usernameField.setBackground(backgroundColor);
        usernameField.setForeground(foregroundColor);
        usernameField.setSelectedTextColor(backgroundColor);
        usernameField.setSelectionColor(foregroundColor);
        usernameField.setActionCommand("ENTER");
        usernameField.addActionListener(this);

        // Create and set up the login button
        JButton login = new JButton();
        ImageIcon icon2 = createImageIcon("images/login.png");
        ImageIcon icon3 = createImageIcon("images/login2.png");
        login.setIcon(icon2);
        login.setPressedIcon(icon3);
        login.setBackground(backgroundColor);
        login.setBorderPainted(false);
        login.setContentAreaFilled(false);
        login.setFocusPainted(false);
        login.setSize(new Dimension(10, 10));
        login.setActionCommand("LOGIN");
        login.addActionListener(this);

        // Progress Bar
        progress = new JProgressBar();
        progress.setValue(0);
        progress.setBackground(backgroundColor);
        progress.setMaximum(100);
        progress.setPreferredSize(new Dimension(600,20));
        progress.setBorderPainted(false);

        // Add Elements to the page
        JLabel pad1 = new JLabel("-----------------------------------------------------");

        leftPanel.add(logo,LEFT_ALIGNMENT);

        authPanel.add(list);
        authPanel.add(pad1);
        authPanel.add(usernameField,BOTTOM_ALIGNMENT);
        authPanel.add(passwordField,BOTTOM_ALIGNMENT);
        rightPanel.add(status,LEFT_ALIGNMENT);
        rightPanel.add(picture, LEFT_ALIGNMENT);
        rightPanel.add(login,LEFT_ALIGNMENT);
        bottomPanel.add(progress);

        this.add(authPanel,BorderLayout.CENTER);
        this.add(leftPanel, BorderLayout.WEST);
        this.add(rightPanel, BorderLayout.EAST);
        this.add(bottomPanel,BorderLayout.SOUTH);
        this.setBorder(BorderFactory.createEmptyBorder(20,40,20,20));
    }

    /**
     * Closes the Frame after successful login
     */
    public static void close(JFrame frame) {
        WindowEvent wce = new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wce);
    }

    private void status() {
        if (MySQLConnect.status) {
            status.setIcon(online);
        } else status.setIcon(offline);
    }

    /**
     * Listens to the events */
    public void actionPerformed(ActionEvent event) {
        eventHandler(event);
    }

    /**
     * Handles the events depending on their status code
     * @param event of type ActionEvent
     */
    @SuppressWarnings("unchecked")
    private void eventHandler(ActionEvent event) {
        status();

        // Event 1 ---> User Clicks on the Selection Box
        if ("LIST".equals(event.getActionCommand())) {
            JComboBox<String> cb = (JComboBox<String>) event.getSource();

            if (event.getSource() instanceof JComboBox) {
                select = (String) cb.getSelectedItem();
                if (select != null) {
                    updateLabel(select);
                    usernameField.setText("");
                    passwordField.setText("");
                }
            }
        }

        // Event 2 ---> User enters a Username and a Password and presses on 'Enter'
        if ("ENTER".equals(event.getActionCommand()) || "LOGIN".equals(event.getActionCommand())) {

            if (con == null) {
                con = MySQLConnect.ConnectDB();
            }

            try {
                 if (isLoginCorrect()) {
                     progressBar(true);
                     // If admin is logging in
                     if (select.equals("Admin Login") && usernameField.getText().equals("admin")) {
                         // Close the current page and move to the Admin Panel
                         Admin admin = new Admin(frame);
                         admin.setVisible(true);
//                         close(frame);
                         frame.setVisible(false);
                         con.close();  // close the connection
                         con = null;
                     } else if (select.equals("Admin Login") && !usernameField.getText().equals("admin")) {
                         cannotLogin();
                     }
                     // If admin or customers are logging in
                     else if (select.equals("Customer Login")&& !usernameField.getText().equals("admin")) {
                         // Close the current page and move to the Admin Panel
                         Customer customer = new Customer(usernameField.getText(), frame);
                         customer.setVisible(true);
//                         close(frame);
                         frame.setVisible(false);
                         con.close(); // close the connection
                         con = null;
                    }
                 } else {
                     cannotLogin();
                 }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error --> Login Error.");
            }
        }
    }

    /**
     * Login Failure
     */
    private void cannotLogin() {
        progressBar(false);
        JOptionPane.showMessageDialog(this,
                "Invalid entry. Try again.", "Error Message", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Progress bar implementation (Red(%50) or Green(%100))
     * @param b of type boolean
     */
    private void progressBar(boolean b){
        if (b) {
            progress.setForeground(Color.GREEN);
            while (i < 100) {
                i++;
                progress.setValue(i);
            }
        } else {
            progress.setForeground(Color.RED);
            while (i < 100) {
                i++;
                progress.setValue(i);
            }
        }
    }

    /**
     * Method to check the login information
     * @return isCorrect of type boolean
     */
    private boolean isLoginCorrect() {
        String enteredPassword = String.valueOf(passwordField.getPassword());

        try {
            String sql = "SELECT password FROM  account WHERE username=?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, usernameField.getText());

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String rsPassword = rs.getString("password");
                    return SafetyPassword.checkPass(enteredPassword, rsPassword);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error --> System is Offline.");
        }
        return false;
   }

    /**
     * Updates the label of the selected image
     * @param name of type String
     */
    private void updateLabel(String name) {
        ImageIcon icon = createImageIcon("images/" + name + ".png");
        picture.setIcon(icon);
        picture.setToolTipText("A drawing of a " + name.toLowerCase());
        if (icon != null) {
            picture.setText(null);
        } else {
            picture.setText("Image not found");
        }
    }

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     */
    static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = Thread.currentThread().getContextClassLoader().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        // Create and set up the window.

        frame = new JFrame("Welcome to BotBank Login!");
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Ask for confirmation before terminating the program.
                int option = JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to close the application?",
                        "Close Confirmation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (option == JOptionPane.YES_OPTION) {
                    try {
                        if (con != null) con.close();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    close(frame);
                    System.exit(0);
                }
            }
        });
        frame.setResizable(false);

        // Create and set up the content pane.
        JComponent newContentPane = new UserInterface();
        newContentPane.setBackground(backgroundColor);
        newContentPane.setPreferredSize(new Dimension(700,280));
        frame.setContentPane(newContentPane);

        // Display the window.
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        /*
        Schedule a job for the event-dispatching thread:
        creating and showing this application's GUI.
        */
        javax.swing.SwingUtilities.invokeLater(UserInterface::createAndShowGUI);
    }
}
