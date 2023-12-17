package com.app.api;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {
	
	static Connection con;
	
	public static Connection createC() {
		try {
			// load the driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			String user = "root";
			String password = "root";
			String url = "jdbc:mysql://localhost:3306/grey_goose";
			con = DriverManager.getConnection(url, user, password);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return con;
	}
}