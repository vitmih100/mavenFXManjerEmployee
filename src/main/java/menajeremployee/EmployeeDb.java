package menajeremployee;

import enums.Gender;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;
import javafx.collections.ObservableList;

public class EmployeeDb <T>{
    
  private ObservableList<Employee> appMainObservableList;
  
  public void insertOne(Employee emp) {
     String sql = "insert into postgres.employee(idnp,nume,prenume,sarariu,data_ang,data_nast,gen)"
                //+ "values(?,?,?,?,?,?,?)";
                + "values(nextval('postgres.employee_idnp_seq'),?,?,?,?,?,?)";     
    Connection conn = ConnectionDb.initConnection();
    PreparedStatement statement=null;
    
    try{
      if(conn!=null){
        statement = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        //statement.setInt   (1, emp.getIdnp());
        statement.setString(1, emp.getName());
        statement.setString(2, emp.getSurName());
        statement.setDouble(3, emp.getSalary());
        statement.setDate  (4, Date.valueOf(emp.getHireDayL()));
        statement.setDate  (5, Date.valueOf(emp.getBirthDayL()));
        statement.setInt   (6, emp.getGender().ordinal());
        int affectedRows = statement.executeUpdate();
        if(affectedRows>0){
          ResultSet rsVal=statement.getGeneratedKeys();
          rsVal.next();
          emp.setId(rsVal.getInt(1));
          emp.setIdnp(rsVal.getInt(2));
        }
        //System.out.println("Inserted" + rows + " rows"); 
        System.out.println(String.format("Executed insert statement. Affected %d rows", affectedRows));
      } else {
        System.out.println("Insert failed");
      } 
    } catch(SQLException ex){
      System.out.println("ERROR! Insert failed. " + ex.getMessage());
    }finally{
      try {
        if(statement!=null){
          statement.close(); 
        }
        if(conn!=null){
          conn.close();
        }
      }catch(SQLException ex){
        System.out.println(ex.getMessage());
      }
    }   
  }
  public void insert(ObservableList<Employee> tvObservableList) {
    this.appMainObservableList = tvObservableList;
    String sql = "insert into postgres.employee(idnp,nume,prenume,sarariu,data_ang,data_nast,gen)"
                + "values(?,?,?,?,?,?,?)";
    Connection conn = ConnectionDb.initConnection();
    PreparedStatement statement=null;
    
    try{
      if(conn!=null){
        for(Employee emp : this.appMainObservableList){
          statement = conn.prepareStatement(sql);
          statement.setInt   (1, emp.getIdnp());
          statement.setString(2, emp.getName());
          statement.setString(3, emp.getSurName());
          statement.setDouble(4, emp.getSalary());
          statement.setDate  (5, Date.valueOf(emp.getHireDayL()));
          statement.setDate  (6, Date.valueOf(emp.getBirthDayL()));
          statement.setInt   (7, emp.getGender().ordinal());
          int rows = statement.executeUpdate();
          System.out.println("Inserted " + rows + " rows"); 
        }
      } else {
        System.out.println("Multiple insert failed");
      } 
    } catch(SQLException ex){
      System.out.println("ERROR! Multiple insert failed. " + ex.getMessage());
    }finally{
      try {
        if(statement!=null){
          statement.close(); 
        }
        if(conn!=null){
          conn.close();
        }
      }catch(SQLException ex){
        System.out.println(ex.getMessage());
      }
    }       
      
  }
  
  public void get(ObservableList<Employee> tvObservableList,T searchParametr,int numParametr) {

    this.appMainObservableList = tvObservableList;
    String sql = "SELECT id, idnp, nume, prenume, sarariu, data_ang, data_nast, gen " +
                 "FROM postgres.employee ";
    String sqlo = " order by id";
    Connection conn = ConnectionDb.initConnection();
    PreparedStatement statement=null;

    try{
      if(conn!=null){
        switch(numParametr){
          case 0: 
            break;
          case 1: 
            sql=sql+"where id=?" + sqlo;
            statement = conn.prepareStatement(sql);
            statement.setInt(1,(Integer)searchParametr);
            break;
          case 2: 
            //System.out.println("(Integer)searchParametr="+(Integer)searchParametr);
            sql=sql+"where idnp=?" + sqlo;
            statement = conn.prepareStatement(sql);
            statement.setInt(1,(Integer)searchParametr);
            break;
          case 3: 
            sql=sql+"where nume=?" + sqlo;
            statement = conn.prepareStatement(sql);
            statement.setString(1,(String)searchParametr);
            break;
          case 4: 
            sql=sql+"where prenume=?" + sqlo;
            statement = conn.prepareStatement(sql);
            statement.setString(1,(String)searchParametr);
            break;
          case 5: 
            sql=sql+"where sarariu=?" + sqlo;
            statement = conn.prepareStatement(sql);
            statement.setDouble(1,(Double)searchParametr);
            break;
          case 6: 
            sql=sql+"where data_ang=?" + sqlo;
            statement = conn.prepareStatement(sql);
            statement.setDate(1,Date.valueOf((LocalDate)searchParametr));
            break;
          case 7: 
            sql=sql+"where data_nast=?" + sqlo;
            statement = conn.prepareStatement(sql);
            statement.setDate(1,Date.valueOf((LocalDate)searchParametr));
            break;
          case 8: 
            sql=sql+"where gen=?" + sqlo;
            statement = conn.prepareStatement(sql);
            statement.setInt(1,((Gender)searchParametr).ordinal());
            break;
        }
        ResultSet result = statement.executeQuery();
        this.appMainObservableList.clear();
        while (result.next()) {
          int id = result.getInt("id");
          int idnp = result.getInt("idnp");
          String name = result.getString("nume");
          String surname = result.getString("prenume");
          double salary = result.getDouble("sarariu");
          LocalDate hireDay = result.getDate("data_ang").toLocalDate();
          LocalDate birthDay = result.getDate("data_nast").toLocalDate(); 
          Gender gender = Gender.values()[result.getInt("gen")];
          this.appMainObservableList.add(new Employee(id,idnp,name,surname,salary,hireDay,birthDay,gender));
        }
        
      } else {
        System.out.println("Select failed");
      } 
    } catch(SQLException ex){
      System.out.println("ERROR! Select failed. " + ex.getMessage());
    }finally{
      try {
        if(statement!=null){
          statement.close(); 
        }
        if(conn!=null){
          conn.close();
        }
      }catch(SQLException ex){
        System.out.println(ex.getMessage());
      }
    }
  }
  


  public int updatep(int id,T updateParametr,int numParametr) {
    String sql = "update postgres.employee set ";
    String sqlw= " where id=?";
    Connection conn = ConnectionDb.initConnection();
    PreparedStatement statement=null;

    try{
      if(conn!=null){
        switch(numParametr){
          case 0: 
            break;
          case 1: 
            break;
          case 2: 
            sql=sql+"idnp=?"+sqlw;
            statement = conn.prepareStatement(sql);
            statement.setString(1,(String)updateParametr);
            break;
          case 3: 
            sql=sql+"nume=?"+sqlw;
            statement = conn.prepareStatement(sql);
            statement.setString(1,(String)updateParametr);
            break;
          case 4: 
            sql=sql+"prenume=?"+sqlw;
            statement = conn.prepareStatement(sql);
            statement.setString(1,(String)updateParametr);
            break;
          case 5: 
            sql=sql+"sarariu=?"+sqlw;
            statement = conn.prepareStatement(sql);
            statement.setDouble(1,(Double)updateParametr);
            break;
          case 6: 
            sql=sql+"data_ang=?"+sqlw;
            statement = conn.prepareStatement(sql);
            statement.setDate(1,Date.valueOf((LocalDate)updateParametr));
            break;
          case 7: 
            sql=sql+"data_nast=?"+sqlw;
            statement = conn.prepareStatement(sql);
            statement.setDate(1,Date.valueOf((LocalDate)updateParametr));
            break;
          case 8: 
            sql=sql+"gen=?"+sqlw;
            statement = conn.prepareStatement(sql);
            statement.setInt(1,((Gender)updateParametr).ordinal());
            break;
        }
        statement.setInt(2,id);
        int affectedRows = statement.executeUpdate();
        System.out.println(String.format("Executed update statement. Affected %d rows", affectedRows));
        return affectedRows;
        
      } else {
        System.out.println("Update failed");
      } 
    } catch(SQLException ex){
      System.out.println("ERROR! Update failed. " + ex.getMessage());
    }finally{
      try {
        if(statement!=null){
          statement.close(); 
        }
        if(conn!=null){
          conn.close();
        }
      }catch(SQLException ex){
        System.out.println(ex.getMessage());
      }
    }
    return -1;
  }
  
  public int update(Employee emp){
  
    String sql = "update postgres.employee set idnp=?, nume=?, prenume=?, sarariu=?, data_ang=?, data_nast=?, gen=? where id=?";
    try(Connection conn = ConnectionDb.initConnection();
        PreparedStatement statement = conn.prepareStatement(sql)){
       statement.setInt(1,emp.getIdnp());
       statement.setString(2,emp.getName());
       statement.setString(3,emp.getSurName());
       statement.setDouble(4, emp.getSalary());
       statement.setDate(5,Date.valueOf(emp.getHireDayL()));
       statement.setDate(6,Date.valueOf(emp.getBirthDayL()));
       statement.setInt(7,emp.getGender().ordinal());      
       statement.setInt(8,emp.getId());
       int affectedRows = statement.executeUpdate();
       System.out.println(String.format("Executed update statement. Affected %d rows", affectedRows));
       return affectedRows;          
        
    }catch(SQLException ex){
      System.out.println("ERROR! Update failed. " + ex.getMessage());
    }
    return -1;
  }
  
  public int delete(Employee emp){
    String sql = "delete from postgres.employee where id=?";
    
    try(Connection conn = ConnectionDb.initConnection();
        PreparedStatement statement = conn.prepareStatement(sql)){
      statement.setInt(1,emp.getId());
      int affectedRows = statement.executeUpdate();
      System.out.println(String.format("Executed delete statement. Affected %d rows", affectedRows));
      return affectedRows;    
    
    }catch(SQLException ex){
      System.out.println("ERROR! Delete failed. " + ex.getMessage());
    }
    return -1;
  }
 
   
}
