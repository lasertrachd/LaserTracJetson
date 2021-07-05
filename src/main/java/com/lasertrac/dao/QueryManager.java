package com.lasertrac.dao;

import java.sql.Connection;

import org.apache.commons.dbcp2.BasicDataSource;

public class QueryManager {

	private static BasicDataSource connectionPool=null;
	static void  initializeConnectionPool(){
		if(connectionPool==null) {
			String dbUrl = "jdbc:postgresql://127.0.0.1:5432/lasertrac" ; //+ dbUri.getPath();
			  connectionPool = new BasicDataSource();

//			  if (dbUri.getUserInfo() != null) {
//			    connectionPool.setUsername(dbUri.getUserInfo().split(":")[0]);
//			    connectionPool.setPassword(dbUri.getUserInfo().split(":")[1]);
//			  }
			  connectionPool.setUsername("postgres");
			  connectionPool.setPassword("postgres");
			  
			  connectionPool.setDriverClassName("org.postgresql.Driver");
			  connectionPool.setUrl(dbUrl);
			  
			  connectionPool.setInitialSize(100);
		}
	}
	
	public static Connection getConnection() {
		try {
			if(connectionPool==null) {
				initializeConnectionPool();
			}
			Connection connection = connectionPool.getConnection();	
			return connection;
		}catch(Exception ex) {
			return null;
		}
	}
	
}
