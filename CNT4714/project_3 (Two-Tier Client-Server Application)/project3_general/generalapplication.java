/*
	 * Name: Fredrick Bouloute
	 * Course: CNT 4714 Fall 2024
	 * Assignment Title: Project 3 - A Specialized Accountant Application
	 * Date: October 20, 2024
	 * 
	 * Class: General
	 */
package project3_general;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import com.mysql.cj.jdbc.MysqlDataSource;

@SuppressWarnings("serial")
public class generalapplication extends JFrame{
	

	    public JFrame window;// Main window
	    private JPanel upper;// Upper section
	    private JPanel lower;// Lower section

	    String[] dbProportiesItems = { "operationslog.properties" };
	    String[] userProportiesItems = { "theaccountant.properties"};

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
	    JScrollPane scrollPane = new JScrollPane();
	    
	    // DB Items

	    private Connection connect = null;
	    private Connection oConnect = null;
	    private TableModel Empty;

	    // JTextArea
	    JTextArea textCommand;
	    
	    int numberOfRows;
	    int numberOfColumns;
	    String userProp;

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
	        clearSQLButton.setBackground(Color.WHITE);
	        clearSQLButton.setForeground(Color.RED);
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
	            	//Open file and set properties
	            	Properties properties = new Properties();
	            	FileInputStream filein = null;
	            	MysqlDataSource dataSource = null;
	            	
	                // If already connected, close previous connection
	                try {
	                    if (connect != null) {
	                        connect.close();
	                        // Display status message when not connected
	                        connectionLabel.setText("No Connection Now");
	                    }
	                    
	                    try {
	                    	String dbProp =  (String) dbProporties.getSelectedItem();
	                    	userProp = (String) userProporties.getSelectedItem();
		                    
		                    //Check if username and password match
		                    String user = usernameTextField.getText();
		                    String pass = new String(passwordField.getPassword());
		                    filein = new FileInputStream(userProp);
		                    properties.load(filein);
		                    String rUser = properties.getProperty("USERNAME");
		                    String rpass = properties.getProperty("PASSWORD");
		                    
		                    if(user.compareTo(rUser) != 0 || pass.compareTo(rpass) != 0) {
		                    	connectionLabel.setText("NOT CONNECTED - User Credentials Do No Match Properties File");
		                    }
		                    else {
			                    filein = new FileInputStream(dbProp);  
			                    properties.load(filein);
			                    dataSource = new MysqlDataSource();
			                    dataSource.setURL(properties.getProperty("MYSQL_DB_URL"));
			                    dataSource.setUser(rUser);
			                    dataSource.setPassword(rpass);
			                    connect = dataSource.getConnection();
			                    connectionLabel.setText("CONNECTED TO: " + dataSource.getURL());
			                    
			                    
		                    }
	                    } catch (IOException e) {
	                    	JOptionPane.showMessageDialog(null, "An IO error has occurred.");
	                    	e.printStackTrace();
	                    }
	                }
	                catch (SQLException e) {
	                	JOptionPane.showMessageDialog(null, "An SQL error has occurred.");
	                	e.printStackTrace();
	                }
	            }
	        });

	        disconnectButton.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent event) {
	            	if(connect != null) {
		            	try {
		            		connect.close();
		            		connect = null;
		            		connectionLabel.setText("DISCONNECTED:");
		            	} catch(SQLException e) {
		                	JOptionPane.showMessageDialog(null, "An SQL error has occurred.");
		            		e.printStackTrace();
		            	}
	            	}
	            	else {
	            		connectionLabel.setText("NO CONNECTION: ");
	            	}
	            }
	        });

	        clearSQLButton.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent event) {
	            	sqlCommandsField.setText("");
	            }
	        });

	        executeSQLButton.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent event) {
	            
	            try {
	            	//Get TextArea string
	            	String sqlText = sqlCommandsField.getText();
	            	if (sqlText.isEmpty()) {
						JOptionPane.showMessageDialog(null, "NO SQL command to be executed");
						return;
					}
	            	Statement statement = connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
	            			ResultSet.CONCUR_UPDATABLE);
	            	
	            		String commands = sqlCommandsField.getText();
	            		ResultSet resultSet = null;
	            		
	            		if(sqlCommandsField.getText().toUpperCase().contains("SELECT")) {
//	                		resultSet = statement.executeQuery(commands);
	            			
	 
	            			setQuery(commands, statement);
	            			
	            		}
	            		else {
	            			 
	            			
	            			int columnsUpdated = setUpdate(commands, statement);
	            			JOptionPane.showMessageDialog(null, "Successful Update: " + columnsUpdated);
	            		}
		            	
	            	
	            }
	            catch (SQLException e) {
	            	JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	            	e.printStackTrace();
	            }
		            		
//		            
		            		
		            	
	            	
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
	    
	    public void setQuery(String query, Statement statement) {
	    	try {
	    			ResultSet resultSet = null;
	        		resultSet = statement.executeQuery(query);
	        		
//	        		resultTable.setModel(DbUtils.resultSetToTableModel(DbUtils.resultSetToel(resultSet)));
	        		
	        		ResultSetMetaData metaData = resultSet.getMetaData();
	        		numberOfColumns = metaData.getColumnCount();
	        		resultSet.last();
	        		numberOfRows = resultSet.getRow();
	        		System.out.println("Number of Rows: " + numberOfRows);
	        		System.out.println("The Number Of Columns is " + numberOfColumns);
	        		
	        		//Update Table
	        		DefaultTableModel model = new DefaultTableModel(numberOfRows,numberOfColumns);
	        		//resultTable = new JTable(model, columnNames);
	        		resultTable.setModel(model);
	        	
	        		
	        		
	        		
	        		int i = 1;
	        		
	        		//Get Column Names
	        		String [] columnNames = new String [numberOfColumns];
	        		for(i = 0; i < numberOfColumns; i++) {
	    				columnNames[i] = metaData.getColumnName(i + 1);
	    				TableColumn tc = resultTable.getColumnModel().getColumn(i);
	    				tc.setHeaderValue(columnNames[i]);
	    			}
	        		
	        		//Get data
	        		resultSet.first();
	        		for(i = 1; i <= numberOfColumns; i++) {
		        		System.out.print(resultSet.getString(columnNames[i-1]) + " ");
						resultTable.setValueAt(resultSet.getString(columnNames[i-1]), 0, i-1);
	        		}
	        		int j = 0;
	        		while(resultSet.next()) {
	        			
	        			for(i = 0; i < numberOfColumns; i++) {
	        				System.out.print(resultSet.getString(columnNames[i]) + " ");
	        				resultTable.setValueAt(resultSet.getString(columnNames[i]), j+1, i);
	        				
	        			}
	        			j++;
	        			System.out.println();
	        			
	        		}
	        		
	    	}
		    catch(SQLException e) {
		    	JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

	    	
	    }
	    
	    public int setUpdate(String update, Statement statement) {
	    	try {
		    	int affected = statement.executeUpdate(update);
		    	return affected;
	    	} catch (SQLException e){
	    		JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    	}
	    	return 0;
	    	
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
	        resultTable = new JTable();
	        scrollPane = new JScrollPane(resultTable);
	        // BoxLayout boxLayout = new BoxLayout(lower, BoxLayout.Y_AXIS);
	        // FlowLayout flowLayout = new FlowLayout(1, 15, 10);
	        lower.setLayout(new BoxLayout(lower, BoxLayout.Y_AXIS));
	        // lower.add(connectionLabel);
	        // lower.add(sqlCommandLabel);
	        // lower.add(resultTable);
	        // lower.revalidate();
	        // lower.repaint();
	        JPanel northPanel = new JPanel();
	        JPanel southPanel = new JPanel();
	        
	        // BoxLayout boxLayout = new BoxLayout(northPanel, BoxLayout.Y_AXIS);
	        northPanel.setLayout(new GridLayout(2,1,0,5));
	        southPanel.setLayout(new GridLayout(1,2,75,0));
	        
	        northPanel.add(connectionLabel);
	        northPanel.add(sqlCommandLabel);
	        southPanel.add(clearResultButton);
	        southPanel.add(new JLabel());
	        southPanel.add(closeApplicationButton);
	        lower.add(northPanel);
//	        lower.add(resultTable, BorderLayout.CENTER);
	        lower.add(scrollPane);
	        lower.add(southPanel);

	    }

	    public generalapplication() {

	        // Initialize drop down menus

	        // Create and Set Window
	        window = new JFrame("SPECIALIZED ACCOUNTANT APPLICATION - (MJL - CNT 4714 - FALL 2024 - PROJECT 3)");
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

	    @SuppressWarnings("unused")
		public static void main(String[] args) {
	    	generalapplication projectGeneral = new generalapplication();
	        System.out.println(System.getProperty("user.dir"));
	        

	    }
	
}


