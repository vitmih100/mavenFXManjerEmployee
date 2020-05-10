package menajeremployee;

import enums.Gender;
import java.io.FileNotFoundException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ConvertingToJson {
  public void writeToJsonFile(List<Employee> employees)throws FileNotFoundException,IOException{
    System.out.println("Writing JSON -------");
    JSONArray list = new JSONArray();
    for(Employee emp : employees){
      JSONObject obj = new JSONObject();
      obj.put("id", String.valueOf(emp.getId()));
      obj.put("name", emp.getName());
      obj.put("surname", emp.getSurName());
      obj.put("salary", String.valueOf(emp.getSalary()));
      obj.put("gethireday", String.valueOf(emp.getHireDay()));
      obj.put("birthday", String.valueOf(emp.getBirthDay()));
      obj.put("gender", String.valueOf(emp.getGender()));     
      list.add(obj);      
    }
    System.out.println(list);
    
    try (FileWriter file = new FileWriter("src/main/java/menajeremployee/employee.json")){
      file.write(list.toJSONString());
    } catch(IOException e){
      System.out.println("Could not write to JSON. Reason:" + e.getMessage());
    } 
  }
 
  public List<Employee> convertFromJson()throws ParseException,IOException{
    List<Employee> staff = new ArrayList<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    System.out.println("Reading JSON ------");
    Reader reader = new FileReader("src/main/java/menajeremployee/employee.json");
      JSONParser parser = new JSONParser();
      JSONArray jsonArr = (JSONArray) parser.parse(reader);
      System.out.println(jsonArr);  
      
      for(int i=0; i < jsonArr.size(); i++){
        JSONObject employeeJson = (JSONObject)jsonArr.get(i);
        System.out.println("id: " + employeeJson.get("id"));
        System.out.println("name: " + employeeJson.get("name"));
        System.out.println("surname: " + employeeJson.get("surname"));
        System.out.println("salary: " + employeeJson.get("salary"));
        System.out.println("gethireday: " + employeeJson.get("gethireday"));
        System.out.println("birthday: " + employeeJson.get("birthday"));
        System.out.println("gender: " + employeeJson.get("gender"));
        staff.add(new Employee(0,(String)employeeJson.get("name"),(String)employeeJson.get("surname"),Double.valueOf((String)employeeJson.get("salary")),
                LocalDate.parse((String)employeeJson.get("gethireday"),formatter),LocalDate.parse((String)employeeJson.get("birthday"),formatter),Gender.valueOf((String)employeeJson.get("gender"))));
      }
    
    return staff;
  }

}
