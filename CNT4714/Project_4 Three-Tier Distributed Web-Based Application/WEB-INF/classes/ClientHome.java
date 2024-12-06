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
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.util.Properties;

@SuppressWarnings("serial")
public class ClientHome extends HttpServlet{

	private Connection connection;
	private ResultSet resultSet;
	private PreparedStatement pstatement;
	
	@Override
	protected void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		//Take in the command from the webapp
		String sqlCommand = request.getParameter("commands");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String result = "<h1>Yo</h1>";
		
		//Connect to database
		try {
			getDBConnection();
			//pstatement = connection.prepareStatement(sqlCommand);
			
			Statement statement = connection.createStatement();
			//If SELECT, query. Otherwise Update
			if(sqlCommand.toUpperCase().contains("SELECT")) {
				try {
	        		resultSet = statement.executeQuery(sqlCommand);
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
				} catch (Exception e) {
					result = "<table><tr bgcolor=#ff0000><td><font color=#ffffff><b>Error executing the SQL statement:</b><br>" + e.getMessage() + "</font></td></tr></table>";
					HttpSession session = request.getSession();
			        session.setAttribute("result", result);
			        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/clientHome.jsp");
			        dispatcher.forward(request, response);
				}
    			
    		}
    		else {
    			
    			result = "<div>The statement executed successfully.</div>";
    			int rowsUpdated = 0;
    			
    			rowsUpdated = statement.executeUpdate(sqlCommand);
    			result += "<div>Number of rows affected: " + rowsUpdated + "</div>";
    			//If the update involves the shipments table, test
    			if(sqlCommand.toUpperCase().contains("SHIPMENTS")){
    				result += "<br>";
        			result += "<p>Business Logic Detected! - Updating Supplier Status</p>";
        			result+= "<br>";
        			
        			//Update Statuses
        			String updateStatus = "SET SQL_SAFE_UPDATES = 0;";
        			int useless = statement.executeUpdate(updateStatus);
        			updateStatus = "update suppliers set status = status + 5 where snum in (select snum from shipments where quantity >= 100);";
        			int suppliersUpdated = statement.executeUpdate(updateStatus);
        			updateStatus = "SET SQL_SAFE_UPDATES = 1";	
        			useless = statement.executeUpdate(updateStatus);
        			
        			result+= "Business Logic updated " + suppliersUpdated + " supplier status marks.";
        			result+= "<br>";

    			}
    			else {
    				result+= "<br>Business Logic Not Triggered!<br>";
    			}
    			
    		}
			
			
			
//			out.println(result);
//			out.close();
			
			HttpSession session = request.getSession();
	        session.setAttribute("result", result);
	        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/clientHome.jsp");
	        dispatcher.forward(request, response);

			
		} catch(SQLException e) {
			result = "<table><tr bgcolor=#ff0000><td><font color=#ffffff><b>Error executing the SQL statement:</b><br>" + e.getMessage() + "</font></td></tr></table>";
			HttpSession session = request.getSession();
	        session.setAttribute("result", result);
	        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/clientHome.jsp");
	        dispatcher.forward(request, response);
		}
		
		
	}
	
	
	
	
	
	private void getDBConnection() {
		Properties properties = new Properties();
		FileInputStream filein = null;
		MysqlDataSource dataSource = null;
		String credentialsSearchQuery = "select * from usercredentials where login_username = ? and login_password = ?";
		try {
			filein = new FileInputStream("/Library/Tomcat1101/apache-tomcat-11.0.1/webapps/Project4/lib/client.properties");
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

