/*  Name: Fredrick Bouloute
    Course: CNT 4714 - Fall 2024
    Assignment title: Project 1 - An Event-driven Enterprise Simulation
    Date: Sunday September 8, 2024
 */

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.JOptionPane;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.time.*;
import java.time.format.DateTimeFormatter;


public class CNT4714_Proj1_FB extends JFrame{
    //Color red = new Color(255, 102, 102);

    //Variables
    public JFrame window;//Main window
    private JPanel upper;//The upper part of the window for inputs
    private JPanel middle;//Middle part of the window for Items
    private JPanel lower;//Lower part for buttons
    double subtotalAmount;
    double totalAmount;
    double currentItemAmount;//Used for the item amount
    int itemNumber = 1;//Used to place items into textfields
    String[] currentItem;
    String[] viewCartString = new String[5];//USed to display cart in view car
    String discount = "";
   private DecimalFormat df = new DecimalFormat("0.00");

    //JLabels
    //.setLayout(new GridLayout(4,2));
    
    private JLabel enterID;
    private JLabel enterQuantity;
    private JLabel details;
    private JLabel subtotal;
    //TextFields
    //Upper right (Inputs)
    private JTextField idField;//Editable
    private JTextField quantityField;
    private JTextField detailsField;//Not editable
    private JTextField subtotalField;

    //Middle (Items)
    //.setLayout(new GridLayout(5,1));
    private JTextField item1Field;
    private JTextField item2Field;
    private JTextField item3Field;
    private JTextField item4Field;
    private JTextField item5Field;

    //Bottom
    private JLabel userControls;
    //.setLayout(new GridLayout(3,2));
    //Buttons
    private JButton searchButton;
    private JButton viewCartButton;
    private JButton emptyCartButton;
    private JButton addItemButton;
    private JButton checkOutButton;
    private JButton closeApp;

    //Gridlayouts 


    //Constructor
    public CNT4714_Proj1_FB(){
        //Create window
        window = new JFrame("Nile.Com - Fall 2024 (FB)");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(750, 750);
        window.setLocationRelativeTo(null);
        window.setBackground(getForeground());
        window.setLayout(new GridLayout(3,1));

        //Create JPanels
        upper = new JPanel();
        middle = new JPanel();
        lower = new JPanel();
        window.add(upper);
        window.add(middle);
        window.add(lower);

        setUpper();
        setMiddle();
        setBottom();
        window.setVisible(true);
        emptyCartList();
    }
    public void setUpper(){
        upper.setBorder(BorderFactory.createEmptyBorder(20,20, 40, 20));
        upper.setLayout(new GridLayout(4,2, 10, 15));
        upper.setBackground(Color.LIGHT_GRAY);
        //Create JLabels
        enterID = new JLabel("Enter Item ID For Item #: ", JLabel.RIGHT);
        enterID.setForeground(Color.getHSBColor(255,255,255));
        enterQuantity = new JLabel("Enter quantity for Item #: ", JLabel.RIGHT);
        enterQuantity.setForeground(Color.getHSBColor(255,255,255));
        details = new JLabel("Details for Item #: ", JLabel.RIGHT);
        details.setForeground(Color.getHSBColor(255,255,255));
        subtotal = new JLabel("Current Subtotal: ", JLabel.RIGHT);
        subtotal.setForeground(Color.getHSBColor(255,255,255));

        //Create TextFields
        idField = new JTextField();
        quantityField = new JTextField();
        detailsField = new JTextField();
        subtotalField = new JTextField();


        //Add to JPanel
        upper.add(enterID);
        upper.add(idField);
        upper.add(enterQuantity);
        upper.add(quantityField);
        upper.add(details);
        upper.add(detailsField);
        upper.add(subtotal);
        upper.add(subtotalField);

        //enterID.setText("Enter Item ID For Item #: ");
        //Make uneditable fields
        detailsField.setEditable(false);
        subtotalField.setEditable(false);
        
    }
    public void setMiddle(){
        //Set up Panel
        middle.setBorder(BorderFactory.createEmptyBorder(10,20, 30, 20));
        middle.setLayout(new GridLayout(6,1, 0 , 10));
        middle.setBackground(Color.GRAY);

        //JLabel
        JLabel middleTitle = new JLabel("Your Shopping Cart: Empty", JLabel.CENTER);
        middleTitle.setForeground(Color.ORANGE);
        //Set up textfields
        item1Field = new JTextField();
        item1Field.setEditable(false);
        item2Field = new JTextField();
        item2Field.setEditable(false);
        item3Field = new JTextField();
        item3Field.setEditable(false);
        item4Field = new JTextField();
        item4Field.setEditable(false);
        item5Field = new JTextField();
        item5Field.setEditable(false);

        //Add to Panel
        middle.add(middleTitle);
        middle.add(item1Field);
        middle.add(item2Field);
        middle.add(item3Field);
        middle.add(item4Field);
        middle.add(item5Field);

        item1Field.setEditable(false);
        item2Field.setEditable(false);
        item3Field.setEditable(false);
        item4Field.setEditable(false);
        item5Field.setEditable(false);
    }
    public void setBottom(){
        
        //Setup Panel
        lower.setBorder(BorderFactory.createEmptyBorder(5,20, 10, 20));
        lower.setLayout(new GridLayout(2,1));
        lower.setBackground(Color.DARK_GRAY);

        //Make JButoon Panels
        JPanel buttonsJPanel = new JPanel();
        buttonsJPanel.setLayout(new GridLayout(3,2));
        buttonsJPanel.setBackground(Color.DARK_GRAY);

        //JLabel
        JLabel userControls = new JLabel("User Controls", JLabel.CENTER);
        userControls.setForeground(Color.red);

        //Buttons
        searchButton = new JButton("Search For Item");
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent a){
                //Search for the item in the list
                String userInput = idField.getText();
                String detialsSting = "";//This string is used to set the details field
                try
                {
                    //ArrayList<String> inventory = new ArrayList<String>();
                    BufferedReader reader = new BufferedReader(new FileReader("inventory.csv"));//Reader for file
                    int found = 1;//This determines if a match was found and the quantity was not too high
                    while(reader.ready())//Read in file
                    {
                        String temp = reader.readLine();
                        String[] example_arr = temp.split(", ");//Split the strings to search through
                        //System.out.println("Temp: " + userInput + "--- example_arr: " + example_arr[0]);
                        if(userInput.compareTo(example_arr[0]) == 0)
                        {//Match was found
                            found = 0;//Set found to true
                            currentItem = example_arr;
                            if(example_arr[2].compareTo("false") == 0)//User cannot purchase
                            {
                                JOptionPane.showMessageDialog(null, "Sorry. The item is currently not in stock.", "No Stock", JOptionPane.INFORMATION_MESSAGE);
                                idField.setText("");
                                quantityField.setText("");
                            }
                            else
                            {
                                for (int i = 0; i < 5; i++)//Set the details string
                                {
                                    if(i == 2)//Skip boolean
                                        i++;
                                    if(i == 3){
                                        detialsSting += quantityField.getText() + " ";
                                        i++;
                                    }

                                    if(i == 4)//Add dollar sign
                                        detialsSting += "$";
                                    detialsSting += example_arr[i] + " ";
                                }
                                int quan = Integer.parseInt(example_arr[3]);
                                int req = Integer.parseInt(quantityField.getText());
                                if(quan < req)
                                {//If there aren't enough in stock
                                    
                                    JOptionPane.showMessageDialog(null, "Sorry. The requested quantity is higher than stock. There is currently " + example_arr[3] + " in stock. Please reduce the quantity.", "No Stock", JOptionPane.INFORMATION_MESSAGE);
                                    quantityField.setText("");
                                }
                                else
                                {
                                    //Set discount
                                    
                                    if (req < 4)
                                    {
                                        discount = "0";
                                    }
                                    else if(req > 4 && req < 10)
                                    {
                                        discount = "10";
                                    }
                                    else if(req > 9 && req < 15)
                                    {
                                        discount = "15";
                                    }
                                    else
                                    {
                                        discount = "20";
                                    }
                                    detialsSting += discount + "%";
              
                                    double eachCost = Double.parseDouble(currentItem[4]);
                                    currentItemAmount = req * eachCost;
                                    currentItemAmount -= currentItemAmount * Double.parseDouble(discount) / 100;
                                    
                                    detialsSting += " $" + df.format(currentItemAmount);
                                    detailsField.setText(detialsSting);
                                    viewCartString[itemNumber - 1] = detialsSting;
                                    //System.out.println(currentItemAmount);
                                    addItemButton.setEnabled(true);
                                    searchButton.setEnabled(false);
                                }
                            }
                            //detialsSting = "";//Reset details String

                        }
                    }
                    if (found == 1)
                    {
                        JOptionPane.showMessageDialog(null, "Item ID:" + userInput + " not found." , "No Stock", JOptionPane.INFORMATION_MESSAGE);
                            
                    }
        
                    reader.close();
                }
                catch (FileNotFoundException e)
                {
                    System.out.println("File Not Found");
                }
                catch (IOException e)
                {
                    System.out.println("Exception occured");
                }
                        //Displays said item in item details


                    }
                });

        viewCartButton = new JButton("View Cart");
        viewCartButton.setEnabled(false);
        viewCartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent b){
                //Take the items in the cart and display a JOptionPane of the details
                //String currentCart = "";
                String temp = "";
                for(int i = 1; i < 6; i++){
                    if((viewCartString[i-1].equals("")) == true){
                        i++;
                    }
                    else
                        temp += i + ". " + viewCartString[i-1] + "\n";
                }
                JOptionPane.showMessageDialog(null, temp, "Current Cart", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        emptyCartButton = new JButton("Empty Cart");
        emptyCartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent c){
                //Remove everything from cart
                subtotalAmount = 0;
                totalAmount = 0;
                itemNumber = 1;
                idField.setText("");
                quantityField.setText("");
                detailsField.setText("");
                subtotalField.setText("");
                item1Field.setText("");
                item2Field.setText("");
                item3Field.setText("");
                item4Field.setText("");
                item5Field.setText("");
                searchButton.setEnabled(true);
                addItemButton.setEnabled(false);
                viewCartButton.setEnabled(false);
                checkOutButton.setEnabled(false);
                emptyCartButton.setEnabled(true);
                //Reset subtotal and any other variables
                emptyCartList();
                

            }
        });

        addItemButton = new JButton("Add Item");
        addItemButton.setEnabled(false);
        addItemButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                viewCartButton.setEnabled(true);
                checkOutButton.setEnabled(true);
                if(itemNumber == 1)
                {
                    //quantity * price for each
                    double eachCost = Double.parseDouble(currentItem[4]);
                    int quan = Integer.parseInt(quantityField.getText());
                    subtotalAmount += currentItemAmount;
                    subtotalField.setText(String.valueOf(subtotalAmount));

                    item1Field.setText("Item 1 - SKU: " + currentItem[0] + " Desc: \"" + currentItem[1] + "\", " + "Price Ea. $" + currentItem[4] + ", Qty: "  + quan + ", Total: $" +df.format(currentItemAmount));
                    idField.setText("");
                    quantityField.setText("");
                    addItemButton.setEnabled(false);
                    searchButton.setEnabled(true);
                    checkOutButton.setEnabled(true);
                    itemNumber++;
                }
                else if(itemNumber == 2)
                {
                    double eachCost = Double.parseDouble(currentItem[4]);
                    int quan = Integer.parseInt(quantityField.getText());
                    subtotalAmount += currentItemAmount;
                    subtotalField.setText(String.valueOf(subtotalAmount));

                    item2Field.setText("Item 2 - SKU: " + currentItem[0] + " Desc: \"" + currentItem[1] + "\", " + "Price Ea. $" + currentItem[4] + ", Qty: "  + quan + ", Total: $" +df.format(currentItemAmount));
                    idField.setText("");
                    quantityField.setText("");
                    addItemButton.setEnabled(false);
                    searchButton.setEnabled(true);
                    checkOutButton.setEnabled(true);
                    itemNumber++;
                }
                else if(itemNumber == 3)
                {
                    double eachCost = Double.parseDouble(currentItem[4]);
                    int quan = Integer.parseInt(quantityField.getText());
                    subtotalAmount += currentItemAmount;
                    subtotalField.setText(String.valueOf(subtotalAmount));

                    item3Field.setText("Item 3 - SKU: " + currentItem[0] + " Desc: \"" + currentItem[1] + "\", " + "Price Ea. $" + currentItem[4] + ", Qty: "  + quan + ", Total: $" +df.format(currentItemAmount));
                    idField.setText("");
                    quantityField.setText("");
                    addItemButton.setEnabled(false);
                    searchButton.setEnabled(true);
                    checkOutButton.setEnabled(true);
                    itemNumber++;
                }
                else if(itemNumber == 4)
                {
                    double eachCost = Double.parseDouble(currentItem[4]);
                    int quan = Integer.parseInt(quantityField.getText());
                    subtotalAmount += currentItemAmount;
                    subtotalField.setText(String.valueOf(subtotalAmount));

                    item4Field.setText("Item 4 - SKU: " + currentItem[0] + " Desc: \"" + currentItem[1] + "\", " + "Price Ea. $" + currentItem[4] + ", Qty: "  + quan + ", Total: $" +df.format(currentItemAmount));
                    idField.setText("");
                    quantityField.setText("");
                    addItemButton.setEnabled(false);
                    searchButton.setEnabled(true);
                    checkOutButton.setEnabled(true);
                    itemNumber++;
                }
                else if(itemNumber == 5)
                {
                    double eachCost = Double.parseDouble(currentItem[4]);
                    int quan = Integer.parseInt(quantityField.getText());
                    subtotalAmount += currentItemAmount;
                    subtotalField.setText(String.valueOf(subtotalAmount));

                    item5Field.setText("Item 5 - SKU: " + currentItem[0] + " Desc: \"" + currentItem[1] + "\", " + "Price Ea. $" + currentItem[4] + ", Qty: "  + quan + ", Total: $" +df.format(currentItemAmount));
                    idField.setText("");
                    quantityField.setText("");
                    addItemButton.setEnabled(false);
                    searchButton.setEnabled(true);
                    checkOutButton.setEnabled(true);
                    itemNumber++;
                    searchButton.setEnabled(false);
                }
                
            }
        });


        checkOutButton = new JButton("Check Out");
        checkOutButton.setEnabled(false);
        checkOutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                //Take all the items and print out the invoice using JOptionPane
                //Must include Date, Item Amount, Items, Subtotal, Tax, Tax Amount, Total Order
                String invoice = "Date: ";
                Date thisDate = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, Y, hh:mm:ss a z");
                SimpleDateFormat dateID = new SimpleDateFormat("ddMMYhhmmss, ");

                invoice += dateFormat.format(thisDate) + "\n\n";
                invoice += "Number of line items: " + (itemNumber-1) + "\n\n";
                invoice += "Item# / ID / Title / Price / Qty / Disc% / Subtotal:\n\n";
                String temp = "";
                for(int i = 1; i < 6; i++){
                    if((viewCartString[i-1].equals("")) == true){
                        i++;
                    }
                    else
                        temp += i + ". " + viewCartString[i-1] + "\n";
                }
                invoice += temp + "\n\n\n";
                invoice += "Order subtotal:\t$" + df.format(subtotalAmount) + "\n\n";
                double tax = subtotalAmount * 0.06;
                double actualTotal = subtotalAmount + tax;
                invoice += "Tax rate:\t6%\n\n";
                invoice += "Tax amount:\t$" + df.format(tax) + "\n\n";
                invoice += "ORDER TOTAL:\t$" + df.format(actualTotal) + "\n\n";
                invoice += "Thank you for shopping at Nile Dot Com!";
                JOptionPane.showMessageDialog(null, invoice, "Final Invoice", JOptionPane.INFORMATION_MESSAGE);
                
                //Additionally add to the transaction.csv file
                String transaction = "";
                for(int i = 1; i < 6; i++){
                    if((viewCartString[i-1].equals("")) == true){
                        i++;
                    }
                    else
                        transaction +=  dateID.format(thisDate) + viewCartString[i-1] + " " + dateFormat.format(thisDate) +"\n";
                }
                //System.out.print(transaction);
                
                try
                {
                    //ArrayList<String> inventory = new ArrayList<String>();
                    BufferedWriter writer = new BufferedWriter(new FileWriter("transaction.csv", true));//write to file
                    writer.append(transaction);
                    writer.append("\n");
                    writer.flush();
                    writer.close();
                }
                catch (IOException f)
                {
                    System.out.println("Exception occured");
                }

                //Options
                searchButton.setEnabled(false);
                addItemButton.setEnabled(false);
                viewCartButton.setEnabled(false);
                checkOutButton.setEnabled(false);
                emptyCartButton.setEnabled(true);
            }
        });


        closeApp = new JButton("Close App");
        closeApp.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent f)
            {
                System.exit(0);
            }
        });
        
        
        //Add to Panel
        
        buttonsJPanel.add(searchButton);
        buttonsJPanel.add(viewCartButton);
        buttonsJPanel.add(emptyCartButton);
        buttonsJPanel.add(addItemButton);
        buttonsJPanel.add(checkOutButton);
        buttonsJPanel.add(closeApp);
        lower.add(userControls);
        lower.add(buttonsJPanel);

        
    }

    public void emptyCartList(){
        for(int i = 0; i < 5; i++)//Make cart empty
            {
                viewCartString[i] = "";
            }
    }



     




    public static void main(String[] args) {
        CNT4714_Proj1_FB proj = new CNT4714_Proj1_FB();
        try
        {
            //ArrayList<String> inventory = new ArrayList<String>();
            BufferedReader reader = new BufferedReader(new FileReader("inventory.csv"));//Reader for file
            
            while(reader.ready())//Read in file
            {
                String temp = reader.readLine();
                String[] example_arr = temp.split(",");
                
            }
  
            reader.close();


        }
        catch (FileNotFoundException e)
        {
            System.out.println("File Not Found");
        }
        catch (IOException e)
        {
            System.out.println("Exception occured");
        }
    }
}