package project4;
/*
 *Name: Fredrick Bouloute
 *Course: CNT 4714 - Fall 2024 - Project Four
 */
import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.util.Properties;


//@WebServlet("/AuthenticationServlet")
@SuppressWarnings("serial")
public class AutheticationServlet extends HttpServlet{

	private Connection connection;
	private ResultSet lookupResults;
	private PreparedStatement pstatement;
	
	
	@Override
	protected void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		String inBoundUserName = request.getParameter("username");
		String inBoundPassword = request.getParameter("password");
		
		String credentialsSearchQuery = "select * from usercredentials where login_username = ? and login_password = ?";
		boolean userCredentialsOk = false;

		
		Properties properties = new Properties();
		FileInputStream filein = null;
		MysqlDataSource dataSource = null;
		
		try {
			getDBConnection();		
			
			pstatement = connection.prepareStatement(credentialsSearchQuery);
			
			pstatement.setString(1,  inBoundUserName);
			pstatement.setString(2,  inBoundPassword);
			
			
			lookupResults = pstatement.executeQuery();
			
			
			if(lookupResults.next()) {
				userCredentialsOk = true;
			}
			else {
				userCredentialsOk = false;
			}
			connection.close();
			
			
		}
		catch(SQLException e) {
			JOptionPane.showMessageDialog(null, "Error: SQL Exception" + e, "MAJOR ERROR- ERROR", JOptionPane.ERROR_MESSAGE);
		}
		
		if(userCredentialsOk) {
			if(inBoundUserName.equals("root")) {
				//Redirect to root user home page
				response.sendRedirect("/Project4/rootHome.jsp");
			}
			else if(inBoundUserName.equals("client")){
				//Redirect to client page
				response.sendRedirect("/Project4/clientHome.jsp");
			}
			else if(inBoundUserName.equals("theaccountant")){
				//Redirect to accountant home page
				response.sendRedirect("/Project4/AccountantUserApp.jsp");
			}
		}
		else {
			response.sendRedirect("/Project4/errorpage.html");
		}
		
	}
	
	private void getDBConnection() {
		Properties properties = new Properties();
		FileInputStream filein = null;
		MysqlDataSource dataSource = null;
		String credentialsSearchQuery = "select * from usercredentials where login_username = ? and login_password = ?";
		try {
			filein = new FileInputStream("/Library/Tomcat1101/apache-tomcat-11.0.1/webapps/Project4/lib/systemapp.properties");
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
	
	public static void main() {
		//Nothing
	}
	
	
	
}
