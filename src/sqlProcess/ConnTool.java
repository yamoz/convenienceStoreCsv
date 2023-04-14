package sqlProcess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnTool {
	private static Connection conn = null;
	
	private String url = "jdbc:sqlserver://localhost:1433;databasename=temp;encrypt=false";
	private String userName = "sa";
	private String pwd = "123";
	
	public Connection getConnection() throws SQLException {
		if ( conn == null ) {
			conn = DriverManager.getConnection(url, userName, pwd);
		}
		if ( conn.isClosed() ) {
			conn = DriverManager.getConnection(url, userName, pwd);
		}
		return conn;
	}
}
