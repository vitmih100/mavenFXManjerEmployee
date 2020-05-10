package mavenfxmanjeremployee;

import enums.Gender;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import menajeremployee.Employee;
import menajeremployee.EmployeeDb;


public class AddDialogController implements Initializable{
    
    private ObservableList<String> strGender = FXCollections.observableArrayList("MALE","FEMALE");
    
    @FXML
    private TextField tfId, tfIdnp, tfName, tfSurname, tfSalary, tfHireDay, tfBirhDay;
 
    @FXML
    private ComboBox tfGender;
    
    private ObservableList<Employee> appMainObservableList;
    private int selectedRow;
    
    public void setAppMainObservableList(ObservableList<Employee> tvObservableList,int selectedRow){
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
      this.appMainObservableList = tvObservableList;
      this.selectedRow = selectedRow;
      if (selectedRow != -1){
        String sName = tvObservableList.get(selectedRow).getName();
        this.tfName.setText(sName);
        String sSurname = tvObservableList.get(selectedRow).getSurName();
        this.tfSurname.setText(sSurname);
        Double sSalary = tvObservableList.get(selectedRow).getSalary();
        this.tfSalary.setText(String.valueOf(sSalary));
        //LocalDate sHireDay = tvObservableList.get(selectedRow).getHireDay();
        //this.tfHireDay.setText(String.valueOf(sHireDay.format(formatter)));
        String sHireDay = tvObservableList.get(selectedRow).getHireDay();
        this.tfHireDay.setText(sHireDay);
        //LocalDate sBirhDay = tvObservableList.get(selectedRow).getBirthDay();
        //this.tfBirhDay.setText(String.valueOf(sBirhDay.format(formatter)));
        String sBirhDay = tvObservableList.get(selectedRow).getBirthDay();
        this.tfBirhDay.setText(sBirhDay);
        Gender sGender = tvObservableList.get(selectedRow).getGender();
        this.tfGender.setValue(String.valueOf(sGender));
      }
      
    }
    
    public ObservableList<Employee> getAppMainObservableList(){
      return this.appMainObservableList;
    }
    
    @FXML
    void btnAddPersonClicked(ActionEvent event){
        
      Employee emp;
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
      String name = tfName.getText().trim();
      String surName = tfSurname.getText().trim();
      double salary = Double.parseDouble(tfSalary.getText());
      LocalDate hireDay = LocalDate.parse(tfHireDay.getText(),formatter);
      LocalDate birthDay = LocalDate.parse(tfBirhDay.getText(),formatter);  
      Gender gender = Gender.valueOf(tfGender.getValue().toString());
  
      //System.out.println("this.selectedRow="+ this.selectedRow);
      if(this.selectedRow == -1){
        emp= new Employee(0,name,surName,salary,hireDay,birthDay,gender);
      } else {
        int id = this.appMainObservableList.get(this.selectedRow).getId();
        int idnp = this.appMainObservableList.get(this.selectedRow).getIdnp();
        emp= new Employee(id,idnp,name,surName,salary,hireDay,birthDay,gender);
      }
      //System.out.println("surname="+ emp.getsurName());
      EmployeeDb edb = new EmployeeDb();
      if(this.selectedRow == -1){
        edb.insertOne(emp);
        this.appMainObservableList.add(emp);
      } else {
        edb.update(emp);
        this.appMainObservableList.set(this.selectedRow, emp);
      }
      closeStage(event);
    }

    private void closeStage(ActionEvent event) {
      Node source = (Node) event.getSource();
      Stage stage = (Stage) source.getScene().getWindow();
      stage.close();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.tfGender.setValue("MALE");
        this.tfGender.setItems(strGender);
    } 
    
}
