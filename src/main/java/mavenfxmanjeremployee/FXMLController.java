package mavenfxmanjeremployee;

import enums.Gender;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import menajeremployee.Employee;
import menajeremployee.EmployeeDb;
import menajeremployee.XmlIOService;
import org.jdom2.JDOMException;

public class FXMLController implements Initializable {
    
    private ObservableList<String> strFilter = FXCollections.observableArrayList("IDNP","Nume","Prenume","Salariu","Data angajarii","Data nasterii","Gen");
    
    @FXML
    private TableView<Employee> tvData;
    
    @FXML
    Button addEmployee, editEmployee, deleteEmployee, importButton, exportButtons, SearchButton;
    
    @FXML
    private ComboBox CFilter;
    
    @FXML
    TextField TSearch;
    
    @FXML
    private TableColumn colId, colIdnp, colName, colSurName, colSalary, colHireDay, colBirhDay, colGender;
    
    private final ObservableList<Employee> tvObservableList = FXCollections.observableArrayList();
    //private ObservableList<Employee> tvObservableList; // = FXCollections.observableArrayList();
    
    private void handleDialogController(int selectedRow)throws IOException{
        FXMLLoader fmxloader = new FXMLLoader(getClass().getResource("/fxml/AddDialog.fxml"));
        Parent parent = fmxloader.load();
        AddDialogController dialogController = (AddDialogController) fmxloader.getController();
        //tvObservableList=dialogController.getAppMainObservableList();
        dialogController.setAppMainObservableList(tvObservableList,selectedRow);
        
        Scene scene = new Scene(parent,480,330);
        scene.getStylesheets().add("/styles/Styles.css");
        
        Stage stage = new Stage();
        stage.setTitle("Add Employee");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }
    
    @FXML
    private void handleAddEmployee(ActionEvent event) throws IOException{     
        //Parent root = FXMLLoader.load(getClass().getResource("/fxml/AddDialog.fxml"));
        int selectedRow = -1;
        handleDialogController(selectedRow);
    }   
    
    @FXML
    private void onOpenEditDialog(ActionEvent event) throws IOException{
      int selectedRow = this.tvData.getSelectionModel().getSelectedIndex();
      if(selectedRow != -1){
        handleDialogController(selectedRow);
      }
    }
    
    
    @FXML
    private void onDeleteEmployee(ActionEvent event) throws IOException{
      int selectedRow = this.tvData.getSelectionModel().getSelectedIndex();
      if(selectedRow != -1){
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Please confirm");
            alert.setHeaderText("Are you sure you want to delete this user?");
            alert.setContentText("This action cannot be undone");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                int id = this.tvObservableList.get(selectedRow).getId();
                int idnp = this.tvObservableList.get(selectedRow).getIdnp();
                String name = this.tvObservableList.get(selectedRow).getName();
                String surName = this.tvObservableList.get(selectedRow).getSurName();
                double salary = this.tvObservableList.get(selectedRow).getSalary();
                LocalDate hireDay = this.tvObservableList.get(selectedRow).getHireDayL();
                LocalDate birthDay = this.tvObservableList.get(selectedRow).getBirthDayL();
                Gender gender = this.tvObservableList.get(selectedRow).getGender();
                
                Employee emp= new Employee(id,idnp,name,surName,salary,hireDay,birthDay,gender);
                EmployeeDb edb = new EmployeeDb();
                edb.delete(emp);
                
                this.tvObservableList.remove(selectedRow);
            }          
      }   
    }
    
    
    @FXML
    private void onSearchEmployee(ActionEvent event){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        EmployeeDb edb = new EmployeeDb();        

        switch(CFilter.getSelectionModel().getSelectedIndex()){
            case 0: 
                edb.get(tvObservableList,Integer.parseInt(TSearch.getText()),2);
                break;
            case 1: 
                edb.get(tvObservableList,TSearch.getText(),3);
                break;       
            case 2: 
                edb.get(tvObservableList,TSearch.getText(),4);
                break;    
            case 3: 
                edb.get(tvObservableList,Double.parseDouble(TSearch.getText()),5);
                break;  
            case 4: 
                edb.get(tvObservableList,LocalDate.parse(TSearch.getText(),formatter),6);
                break; 
            case 5: 
                edb.get(tvObservableList,LocalDate.parse(TSearch.getText(),formatter),7);
                break;
            case 6: 
                Gender gender=null;  
                try{
                  gender = Gender.valueOfIgnoreCase(TSearch.getText());
                }
                catch(IllegalArgumentException e){
                  gender = Gender.valueOf(TSearch.getText());
                }

                if(gender!=null){
                  edb.get(tvObservableList,gender,8);
                  //jFormattedSearch.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("'null'"))));
                }
                break;
        }
        
    }
    
    @FXML
    private void onExpButtons(ActionEvent event){
      XmlIOService xmlFile = new XmlIOService();
      try {
        xmlFile.writeToFile(tvObservableList);
      }
      catch(IOException ex){
        System.out.println("Datele nu au fost salvate in fisier ");
      }
    
    }
    
    @FXML
    private void onImpButtons(ActionEvent event){
       XmlIOService xmlRead = new XmlIOService();
      tvObservableList.clear();
      try{
        xmlRead.readFromFile(tvObservableList);
      }
      catch(JDOMException|IOException ex){
        System.out.println("Datele nu au fost citite din fisier ");
      } 
      EmployeeDb edb = new EmployeeDb();
      edb.insert(tvObservableList);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      this.CFilter.setValue("IDNP");
      this.CFilter.setItems(strFilter);
      
      colId.setCellValueFactory(new PropertyValueFactory<>("id"));
      colIdnp.setCellValueFactory(new PropertyValueFactory<>("idnp"));
      colName.setCellValueFactory(new PropertyValueFactory<>("name"));
      colSurName.setCellValueFactory(new PropertyValueFactory<>("surName"));
      colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
      colHireDay.setCellValueFactory(new PropertyValueFactory<>("hireDay"));
      colBirhDay.setCellValueFactory(new PropertyValueFactory<>("birthDay"));
      colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));  
   
      tvData.setItems(tvObservableList);
      tvData.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
      
 /*       tvObservableList.addListener((ListChangeListener) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                   // DAO.addEmployee()
                    System.out.println(tvObservableList.get(change.getFrom()));
                } else if (change.wasUpdated()){
                    // DAO.updateEmployee();
                }
            }
        });*/
  
    }    
}
