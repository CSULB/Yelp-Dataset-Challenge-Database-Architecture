package edu.csulb.yelp.database;

import java.sql.Connection;
import java.sql.SQLException;

import oracle.jdbc.pool.OracleDataSource;

public class DatabaseHelper {

	public Connection sysConn;

	public boolean open() {
		try {
			// Create a OracleDataSource instance and set properties
			OracleDataSource ods = new OracleDataSource();
			ods.setURL(DbCredentials.CONNECTION_URL);
			ods.setUser(DbCredentials.ORACLE_USERNAME);
			ods.setPassword(DbCredentials.ORACLE_PASSWORD);
			sysConn = ods.getConnection();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void close() {
		try {
			if (sysConn != null && !sysConn.isClosed()) {
				sysConn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
