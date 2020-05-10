package menajeremployee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class ConnectionDb {
  static Connection initConnection(){
    String url = "jdbc:postgresql://127.0.0.1:5432/postgres";
    String user = "mihail";
    String password = "postgres";
    try {
      System.out.println("Connecting...");
      return DriverManager.getConnection(url, user, password);
    } catch(SQLException ex){
      System.out.println("ERROR! Cann't connect to db!" + ex.getMessage());
      return null;
    }
  
  }
    
}
