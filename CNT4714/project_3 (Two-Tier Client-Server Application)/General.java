/*
 * Name: Fredrick Bouloute
 * Course: CNT 4714 Fall 2024
 * Assignment Title: Project 3 - A Tow-tier Client-Server Application
 * Date: October 20, 2024
 * 
 * Class: General
 */

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

//import com.mysql.cj.jdbc.*;

//import com.mysql.cj.jdbc.Driver;

import java.sql.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.Field;
import java.io.FileInputStream;

public class General extends JFrame {
    public JFrame window;// Main window
    private JPanel upper;// Upper section
    private JPanel lower;// Lower section

    String[] dbProportiesItems = { "project3.proporties", "bikedb.proporties", "modeldb.proporties" };
    String[] userProportiesItems = { "root.proporties", "client1.proporties", "client2.proporties",
            "newuser.proporties", "mysteryuser.proporties" };

    // Buttons
    JButton connectButton;
    JButton disconnectButton;
    JButton clearSQLButton;
    JButton executeSQLButton;
    JButton clearResultButton;
    JButton closeApplicationButton;
    // JLabel
    JLabel usernameLabel, passwordLabel, userProportiesLabel, dbProportiesLabel, sqlCommandLabel, sqlExecutionLabel,
            connectionLabel;
    // JComboBox/DropDowns
    JComboBox dbProporties = new JComboBox<String>(dbProportiesItems);
    JComboBox userProporties = new JComboBox<String>(userProportiesItems);
    // JTextField
    JTextField usernameTextField;
    JTextArea sqlCommandsField;
    JPasswordField passwordField;
    // JTable
    JTable resultTable;
    // DB Items

    private Connection connect = null;
    private TableModel Empty;

    // JTextArea
    JTextArea textCommand;

    private void setUpper() {
        upper.setLayout(new GridLayout(1, 2, 10, 15));// Split upper into two halves

        JPanel upperLeft;
        JPanel upperRight;
        upperLeft = new JPanel();
        upperRight = new JPanel();

        upper.add(upperLeft);
        upper.add(upperRight);
        // Upper
        // Left************************************************************************
        // Create Buttons
        connectButton = new JButton("Connect to Database");
        connectButton.setFont(new Font("Arial", Font.BOLD, 12));
        connectButton.setBackground(Color.BLUE);
        connectButton.setForeground(Color.WHITE);
        connectButton.setBorderPainted(false);
        connectButton.setOpaque(true);

        disconnectButton = new JButton("Disconnect From Database");
        disconnectButton.setFont(new Font("Arial", Font.BOLD, 12));
        disconnectButton.setBackground(Color.RED);
        disconnectButton.setForeground(Color.BLACK);
        disconnectButton.setBorderPainted(false);
        disconnectButton.setOpaque(true);

        clearSQLButton = new JButton("Clear SQL Command");
        clearSQLButton.setFont(new Font("Arial", Font.BOLD, 12));
        clearSQLButton.setBackground(Color.YELLOW);
        clearSQLButton.setForeground(Color.BLACK);
        clearSQLButton.setBorderPainted(false);
        clearSQLButton.setOpaque(true);

        executeSQLButton = new JButton("Execute SQL Command");
        executeSQLButton.setFont(new Font("Arial", Font.BOLD, 12));
        executeSQLButton.setBackground(Color.GREEN);
        executeSQLButton.setForeground(Color.BLACK);
        executeSQLButton.setBorderPainted(false);
        executeSQLButton.setOpaque(true);

        clearResultButton = new JButton("Clear Result Window");
        clearResultButton.setFont(new Font("Arial", Font.BOLD, 12));
        clearResultButton.setBackground(Color.YELLOW);
        clearResultButton.setForeground(Color.BLACK);
        clearResultButton.setBorderPainted(false);
        clearResultButton.setOpaque(true);

        closeApplicationButton = new JButton("Close Application");
        closeApplicationButton.setFont(new Font("Arial", Font.BOLD, 12));
        closeApplicationButton.setBackground(Color.RED);
        closeApplicationButton.setForeground(Color.BLACK);
        closeApplicationButton.setBorderPainted(false);
        closeApplicationButton.setOpaque(true);

        // JLabels
        userProportiesLabel = new JLabel();
        userProportiesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        userProportiesLabel.setForeground(Color.BLACK);
        userProportiesLabel.setBackground(Color.GRAY);
        userProportiesLabel.setOpaque(true);
        userProportiesLabel.setText("User Properties");

        dbProportiesLabel = new JLabel();
        dbProportiesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        dbProportiesLabel.setForeground(Color.BLACK);
        dbProportiesLabel.setBackground(Color.GRAY);
        dbProportiesLabel.setOpaque(true);
        dbProportiesLabel.setText("DB URL Properties");

        usernameLabel = new JLabel();
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameLabel.setForeground(Color.BLACK);
        usernameLabel.setBackground(Color.GRAY);
        usernameLabel.setOpaque(true);
        usernameLabel.setText("Username");

        passwordLabel = new JLabel();
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setBounds(20, 150, 165, 25);
        passwordLabel.setForeground(Color.BLACK);
        passwordLabel.setBackground(Color.GRAY);
        passwordLabel.setOpaque(true);
        passwordLabel.setText("Password");

        // DropDown
        dbProporties = new JComboBox<>(dbProportiesItems);
        userProporties = new JComboBox<>(userProportiesItems);

        // Set Textfields
        usernameTextField = new JTextField();
        usernameTextField.setEditable(true);
        usernameTextField.setFont(new Font("Arial", Font.BOLD, 14));

        passwordField = new JPasswordField();

        // Button Bounds
        connectButton.setBounds(20, 200, 25, 15);
        disconnectButton.setBounds(20, 100, 125, 15);

        upperLeft.setLayout(new GridLayout(5, 2, 10, 10));
        upperLeft.add(dbProportiesLabel);
        upperLeft.add(dbProporties);
        upperLeft.add(userProportiesLabel);
        upperLeft.add(userProporties);
        upperLeft.add(usernameLabel);
        upperLeft.add(usernameTextField);
        upperLeft.add(passwordLabel);
        upperLeft.add(passwordField);
        upperLeft.add(connectButton);
        upperLeft.add(disconnectButton);

        // Upper
        // Right************************************************************************
        sqlCommandLabel = new JLabel();
        sqlCommandLabel.setFont(new Font("Arial", Font.BOLD, 14));
        sqlCommandLabel.setForeground(Color.BLUE);
        sqlCommandLabel.setOpaque(true);
        sqlCommandLabel.setAlignmentX(LEFT_ALIGNMENT);
        sqlCommandLabel.setText("Enter An SQL Command");

        sqlCommandsField = new JTextArea(10, 30);
        sqlCommandsField.setEditable(true);
        sqlCommandsField.setFont(new Font("Arial", Font.BOLD, 14));
        sqlCommandsField.setSize(150, 150);
        sqlCommandsField.setAlignmentY(Component.TOP_ALIGNMENT);

        BoxLayout boxLayout = new BoxLayout(upperRight, BoxLayout.Y_AXIS);
        JPanel upperRightButtonsJPanel = new JPanel();
        upperRightButtonsJPanel.setLayout(new GridLayout(1, 2, 10, 10));
        upperRightButtonsJPanel.add(clearSQLButton);
        upperRightButtonsJPanel.add(executeSQLButton);
        upperRight.setLayout(boxLayout);
        upperRight.add(sqlCommandLabel);
        upperRight.add(sqlCommandsField);
        upperRight.add(upperRightButtonsJPanel);

        // Action Listeners for Buttons
        connectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // If already connected, close previous connection
                try {
                    if (connect != null) {
                        connect.close();
                        // Display status message when not connected
                        connectionLabel.setText("No Connection Now");
                    }
                    String url = "jdbc:mysql://localhost:3306/project3";
                    String userProp = (String) userProporties.getSelectedItem();
                    String dbProp = (String) dbProporties.getSelectedItem();
                    String user = usernameTextField.getText();
                    String pass = new String(passwordField.getPassword());
                    System.out.println(dbProp);
                    connect = DriverManager.getConnection(url, user, pass);

                    
                    connectionLabel.setText(dbProp);
                    //DataSource dataSource = new MysqlDataSource();
                    // dataSource.setURL(url + userProporties);
                    // dataSource.setUser(user);
                    // dataSource.setPassword(pass);
                    // connect = dataSource.getConnection();
                }

                catch (SQLException e) {

                }
            }
        });

        disconnectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {

            }
        });

        clearSQLButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {

            }
        });

        executeSQLButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {

            }
        });

        clearResultButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                DefaultTableModel newResultsTableModel = new DefaultTableModel();
                resultTable.setModel(newResultsTableModel);
            }
        });

        closeApplicationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });

    }

    private void setBottom() {
        connectionLabel = new JLabel("NO CONNECTION ESTABLISHED", SwingConstants.CENTER);
        connectionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        connectionLabel.setBounds(100, 100, 300, 100);
        connectionLabel.setBackground(Color.BLACK);
        connectionLabel.setForeground(Color.YELLOW);
        connectionLabel.setOpaque(true);

        sqlCommandLabel = new JLabel("SQL Execution Result Window", SwingConstants.LEFT);
        sqlCommandLabel.setFont(new Font("Arial", Font.BOLD, 14));
        sqlCommandLabel.setForeground(Color.BLUE);
        sqlCommandLabel.setOpaque(true);
        sqlCommandLabel.setAlignmentX(LEFT_ALIGNMENT);
        sqlCommandLabel.setText("SQL Execution Result Window");

        Empty = new DefaultTableModel();
        resultTable = new JTable(5, 5);
        resultTable.setFont(new Font("Arial", Font.BOLD, 14));
        resultTable.setPreferredSize(new Dimension(650, 175));
        resultTable.setModel(Empty);

        // BoxLayout boxLayout = new BoxLayout(lower, BoxLayout.Y_AXIS);
        // FlowLayout flowLayout = new FlowLayout(1, 15, 10);
        // //lower.setLayout(flowLayout);
        // lower.add(connectionLabel);
        // lower.add(sqlCommandLabel);
        // lower.add(resultTable);
        // lower.revalidate();
        // lower.repaint();
        JPanel northPanel = new JPanel();
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(1, 2, 300, 10));
        // BoxLayout boxLayout = new BoxLayout(northPanel, BoxLayout.Y_AXIS);
        northPanel.setLayout(new GridLayout(2, 1, 0, 10));
        northPanel.setAlignmentX(LEFT_ALIGNMENT);
        northPanel.add(connectionLabel);
        northPanel.add(sqlCommandLabel);
        southPanel.add(clearResultButton);
        southPanel.add(closeApplicationButton);
        lower.add(northPanel, BorderLayout.NORTH);
        lower.add(resultTable, BorderLayout.CENTER);
        lower.add(southPanel, BorderLayout.SOUTH);

    }

    public General() {

        // Initialize drop down menus

        // Create and Set Window
        window = new JFrame("SQL CLIENT APPLICATION - (MJL - CNT 4714 - FALL 2024 - PROJECT 3)");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(750, 600);
        window.setLocationRelativeTo(null);
        window.setBackground(getForeground());
        window.setLayout(new GridLayout(2, 1, 0, 10));

        upper = new JPanel();
        upper.setBorder(BorderFactory.createEmptyBorder(20, 20, 40, 20));
        lower = new JPanel();

        setUpper();
        setBottom();

        window.add(upper);
        window.add(lower);

        window.setVisible(true);
    }

    public static void main(String[] args) {
        General projectGeneral = new General();

    }
}