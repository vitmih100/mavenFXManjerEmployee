package enums;
public enum Gender{
  MALE("M"),
  FEMALE("F");

  private String value;

  Gender(String value) {
    this.value = value;
  }

  public static Gender valueOfIgnoreCase(String value){
    for(Gender gender: values()){
      if(gender.value.equalsIgnoreCase(value)){
        return gender;
      }
    }
    throw new IllegalArgumentException("Expected M/F. Got " + value);
  }

}
