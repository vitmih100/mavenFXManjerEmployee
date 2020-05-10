package menajeremployee;

import enums.Gender;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Employee{
  /*private int id;
  private int idnp;
  private static int nextIdnp = 1;
  private String name;
  private String surname;
  private double salary;
  private LocalDate hireDay;
  private LocalDate birthDay;
  private Gender gender;*/
 
  private SimpleIntegerProperty id;
  private SimpleIntegerProperty idnp;
  private static int nextIdnp = 1;
  private SimpleStringProperty name;
  private SimpleStringProperty surName;
  private SimpleDoubleProperty salary;
  private SimpleObjectProperty hireDay;
  private SimpleObjectProperty birthDay;
  private SimpleObjectProperty gender;

  public Employee(int id,String name,String surName,double salary,LocalDate hireDay,LocalDate birthDay,Gender gender){
    this.id=new SimpleIntegerProperty(id);
    //setIdnp();
    this.idnp=new SimpleIntegerProperty(id);
    this.name=new SimpleStringProperty(name);
    this.surName=new SimpleStringProperty(surName);
    this.salary=new SimpleDoubleProperty(salary);
    this.hireDay=new SimpleObjectProperty(hireDay);
    this.birthDay=new SimpleObjectProperty(birthDay);
    this.gender=new SimpleObjectProperty(gender);
  }

  public Employee(int id,int idnp,String name,String surName,double salary,LocalDate hireDay,LocalDate birthDay,Gender gender){
    this.id=new SimpleIntegerProperty(id);
    this.idnp=new SimpleIntegerProperty(idnp);
    this.name=new SimpleStringProperty(name);
    this.surName=new SimpleStringProperty(surName);
    this.salary=new SimpleDoubleProperty(salary);
    this.hireDay=new SimpleObjectProperty(hireDay);
    this.birthDay=new SimpleObjectProperty(birthDay);
    this.gender=new SimpleObjectProperty(gender);
  }  
  
  public int getId(){
    return this.id.get();
  }
  public void setId(int id){
    this.id.set(id);
  }
  
  public int getIdnp(){
    return this.idnp.get();
  }

  public void setIdnp(){
    this.idnp.set(this.nextIdnp++);
  }

  public void setIdnp(int idnp){
    this.idnp.set(idnp);
  }
  
  public static int getNextIdnp(){
    return nextIdnp;
  }

  public String getName(){
    return this.name.get();
  }

  public String getSurName(){
    return this.surName.get();
  }

  public double getSalary(){
    return this.salary.get();
  }

  public LocalDate getHireDayL(){
    return (LocalDate)this.hireDay.get();
  }

  public String getHireDay(){
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    LocalDate hireDayL = (LocalDate)this.hireDay.get();
    return String.valueOf(hireDayL.format(formatter));
  }

  public LocalDate getBirthDayL(){
    return (LocalDate)this.birthDay.get();
  }

  public String getBirthDay(){
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    LocalDate birthDayL = (LocalDate)this.birthDay.get();
    return String.valueOf(birthDayL.format(formatter));
  }  
  
  public void raiseSalary(double byPercent){
    double raise = getSalary() * byPercent / 100;
    this.salary.set(getSalary() + raise);
  }


  public Gender getGender() {
    return (Gender)this.gender.get();
  }

  public void setGender(Gender gender) {
    this.gender.set(gender);
  }
  @Override
  public boolean equals(Object obj){
    
    if(obj instanceof Employee){
      Employee emp=(Employee)obj;
      return this.getName() == emp.getName() && this.getSurName() == emp.getSurName() &&
           this.getSalary() == emp.getSalary() && this.getHireDay() == emp.getHireDay() &&
           this.getBirthDay() == emp.getBirthDay() && this.getGender() == emp.getGender();
    }
    return false;
  }
  
  public void setSalary(double salary) throws IllegalArgumentException{
    if(salary>0){
      this.salary.set(salary);
    }else{
      throw new IllegalArgumentException("Salariul nu poate fi negativ");
    }
  }
  
 /* private static void alterData() {
  // Nu este posibil de a seta valori altele decât prezente în Gender:
    emps[0].setGender(Gender.FEMALE);
    emps[0].setGender(Gender.MALE);
  }
*/
}