/*
 *Name: Fredrick Bouloute
 *Course: CNT 4714 - Fall 2024 - Project Four
 */
package project4;
import javax.swing.JOptionPane;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JOptionPane;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.util.Properties;

@SuppressWarnings("serial")
public class AccountantHome extends HttpServlet{
	private Connection connection;
	private ResultSet resultSet;
	private PreparedStatement pstatement;
	
	@Override
	protected void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		String sqlCommand = request.getParameter("qOptions");
		response.setContentType("text/html");
		String result = " ";
		try {
			getDBConnection();
			Statement statement = connection.createStatement();
			
			if(sqlCommand.equals("Max")) {
//				String command = "select Max(status) FROM suppliers;";
				String command = "CALL Get_The_Sum_Of_All_Parts_Weights();";
				resultSet = statement.executeQuery(command);
				ResultSetMetaData data = resultSet.getMetaData();
        		int numColumns = data.getColumnCount();
				result = "<table border:1>";
        		result += "<tr style=\"background-color: red\">";
        		
        		//Add column names
        		for(int i = 1; i <= numColumns; i++) {
        			result += "<th>" + data.getColumnLabel(i) + "</th>";
        		}    		
        		
        		int count = 0;
        		while(resultSet.next()) {
        			if(count % 2 == 0) {
        				result += "<tr bgcolor = \"grey\"";
        			}
        			else {
        				result += "<tr bgcolor = \"white\"";
        			}
        			result += "<tr>";
        			for(int i = 1; i <= numColumns; i++) {
        				result += "<td>" + resultSet.getString(i) + "</td>";
        			}
        			result += "</tr>";
        			count++;
        		}
        		result += "</table>";
			}
			else if(sqlCommand.equals("Weight")){
				String command = "CALL Get_The_Maximum_Status_Of_All_Suppliers();";
				resultSet = statement.executeQuery(command);
				ResultSetMetaData data = resultSet.getMetaData();
        		int numColumns = data.getColumnCount();
				result = "<table border:1>";
        		result += "<tr style=\"background-color: red\">";
        		
        		//Add column names
        		for(int i = 1; i <= numColumns; i++) {
        			result += "<th>" + data.getColumnLabel(i) + "</th>";
        		}    		
        		
        		int count = 0;
        		while(resultSet.next()) {
        			if(count % 2 == 0) {
        				result += "<tr bgcolor = \"grey\"";
        			}
        			else {
        				result += "<tr bgcolor = \"white\"";
        			}
        			result += "<tr>";
        			for(int i = 1; i <= numColumns; i++) {
        				result += "<td>" + resultSet.getString(i) + "</td>";
        			}
        			result += "</tr>";
        			count++;
        		}
        		result += "</table>";
				
				
			}
			else if(sqlCommand.equals("Shipments")) {
				String command = "CALL Get_The_Total_Number_Of_Shipments();";
				resultSet = statement.executeQuery(command);
				ResultSetMetaData data = resultSet.getMetaData();
        		int numColumns = data.getColumnCount();
				result = "<table border:1>";
        		result += "<tr style=\"background-color: red\">";
        		
        		//Add column names
        		for(int i = 1; i <= numColumns; i++) {
        			result += "<th>" + data.getColumnLabel(i) + "</th>";
        		}    		
        		
        		int count = 0;
        		while(resultSet.next()) {
        			if(count % 2 == 0) {
        				result += "<tr bgcolor = \"grey\"";
        			}
        			else {
        				result += "<tr bgcolor = \"white\"";
        			}
        			result += "<tr>";
        			for(int i = 1; i <= numColumns; i++) {
        				result += "<td>" + resultSet.getString(i) + "</td>";
        			}
        			result += "</tr>";
        			count++;
        		}
        		result += "</table>";
			}
			else if(sqlCommand.equals("Workers")) {
				String command = "CALL Get_The_Name_Of_The_Job_With_The_Most_Workers();";
				resultSet = statement.executeQuery(command);
				ResultSetMetaData data = resultSet.getMetaData();
        		int numColumns = data.getColumnCount();
				result = "<table border:1>";
        		result += "<tr style=\"background-color: red\">";
        		
        		//Add column names
        		for(int i = 1; i <= numColumns; i++) {
        			result += "<th>" + data.getColumnLabel(i) + "</th>";
        		}    		
        		
        		int count = 0;
        		while(resultSet.next()) {
        			if(count % 2 == 0) {
        				result += "<tr bgcolor = \"grey\"";
        			}
        			else {
        				result += "<tr bgcolor = \"white\"";
        			}
        			result += "<tr>";
        			for(int i = 1; i <= numColumns; i++) {
        				result += "<td>" + resultSet.getString(i) + "</td>";
        			}
        			result += "</tr>";
        			count++;
        		}
        		result += "</table>";
			}
			else if(sqlCommand.equals("Suppliers")) {
				String command = "CALL List_The_Name_And_Status_Of_All_Suppliers();";
				resultSet = statement.executeQuery(command);
				ResultSetMetaData data = resultSet.getMetaData();
        		int numColumns = data.getColumnCount();
				result = "<table border:1>";
        		result += "<tr style=\"background-color: red\">";
        		
        		//Add column names
        		for(int i = 1; i <= numColumns; i++) {
        			result += "<th>" + data.getColumnLabel(i) + "</th>";
        		}    		
        		
        		int count = 0;
        		while(resultSet.next()) {
        			if(count % 2 == 0) {
        				result += "<tr bgcolor = \"grey\"";
        			}
        			else {
        				result += "<tr bgcolor = \"white\"";
        			}
        			result += "<tr>";
        			for(int i = 1; i <= numColumns; i++) {
        				result += "<td>" + resultSet.getString(i) + "</td>";
        			}
        			result += "</tr>";
        			count++;
        		}
        		result += "</table>";
			}
			else {
				
				result = "<p>--" + sqlCommand + "--</p>";
			}
			
		}catch (Exception e) {
			result = "<p>Something Went Wrong. Sorry</p>";
			result += "<p>--" + e.getMessage() + "--</p>";
		}
		HttpSession session = request.getSession();
        session.setAttribute("resultA", result);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/AccountantUserApp.jsp");
        dispatcher.forward(request, response);
		

		
		//Fifth Option
			//select sname, status from suppliers;
	}
	
	private void getDBConnection() {
		Properties properties = new Properties();
		FileInputStream filein = null;
		MysqlDataSource dataSource = null;
		String credentialsSearchQuery = "select * from usercredentials where login_username = ? and login_password = ?";
		try {
			filein = new FileInputStream("/Library/Tomcat1101/apache-tomcat-11.0.1/webapps/Project4/lib/accountant.properties");
			properties.load(filein);
			dataSource = new MysqlDataSource();
			dataSource.setUrl(properties.getProperty("MYSQL_DB_URL"));
			dataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
			dataSource.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));
			connection = dataSource.getConnection();
			pstatement = connection.prepareStatement(credentialsSearchQuery);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
