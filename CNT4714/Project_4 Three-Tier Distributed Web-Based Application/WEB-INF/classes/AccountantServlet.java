//package project4;
//
//import java.io.*;
//import java.sql.SQLException;
//import java.util.Properties;
//
//import com.mysql.cj.jdbc.MysqlDataSource;
//
//import jakarta.servlet.http.*;
//
//public class AccountantServlet extends HttpServlet{
//	
//
//
//
//	private void getDBConnection() {
//		Properties properties = new Properties();
//		FileInputStream filein = null;
//		MysqlDataSource dataSource = null;
//		
//		try {
//			filein = new FileInputStream("/Users/fredrickbouloute/Downloads/apache-tomcat-11.0.1/webapps/project-4/lib/systemapp.properties");
//			properties.load(filein);
//			dataSource = new MysqlDataSource();
//			dataSource.setUrl(properties.getProperty("MYSQL_DB_URL"));
//			dataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
//			dataSource.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));
//			connection = dataSource.getConnection();
//		}
//		catch(SQLException e) {
//			e.printStackTrace();
//		}
//		catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//}
//
