package utDao;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

/**PoolConnection MySQL */
public class PoolConnection {
	private static PoolConnection datasource;
	private ComboPooledDataSource cpds;
	public static StringBuffer result = new StringBuffer ();
	
	private PoolConnection() throws IOException, SQLException, PropertyVetoException {
		cpds = new ComboPooledDataSource();
		ResourceBundle resource = ResourceBundle.getBundle("resources/database");
		String url = resource.getString("db.host");
		String driver = resource.getString("driver");
		String user = resource.getString("db.login");
		String pass = resource.getString("db.password");
		System.out.println("2 HOST: " + url + ", LOGIN: " + user + ", PASSWORD: " + pass);
		cpds.setDriverClass(driver);
		cpds.setJdbcUrl(url);
		cpds.setUser(user);
		cpds.setPassword(pass);
		// the settings below are optional -- c3p0 can work with defaults
		cpds.setMinPoolSize(5);
		cpds.setAcquireIncrement(5);
		cpds.setMaxPoolSize(20);
		cpds.setMaxStatements(180);
	}

	public static PoolConnection getInstance() throws IOException, SQLException, PropertyVetoException {
		if (datasource == null) {
			datasource = new PoolConnection();
			return datasource;
		} else {
			return datasource;
		}
	}
	
	public Connection getConnection() throws SQLException {
		return this.cpds.getConnection();
		
	}
	// метод для тестирования бд без вебсервера
	//можно упразднить
	public static void main(String[] args) { 
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = getInstance().getConnection();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM users");
			result= new StringBuffer (); //удалить
			while (rs.next()){
				System.out.println ("-----*****----");
				System.out.println (rs.getString("login"));
				System.out.println ("-----*****----");
//				result=result.append(rs.getString(2));
//				result=result.append("<br/>");
			}
			stmt.close();
			rs.close();
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
	}
}