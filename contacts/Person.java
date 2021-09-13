package contacts;

import java.io.*;

public class Person extends Contact implements Serializable {

    private static final long serialVersionUID = 2L;
    private String surname;
    String gender;
    String birthday;
    private final String fields = "(name, surname, birth, gender, number)";

    public Person() {

    }

    public String getValues() {
        return this.name + " " + this.surname + " " + this.birthday + " " + this.gender + " " + this.phoneNumber;
    }

    public String getFields() {
        return fields;
    }

    public void changeField(String field, String newValue) {
        if (fields.contains(field)) {
            switch (field) {
                case "name":
                    this.name = newValue;
                    break;
                case "surname":
                    this.surname = newValue;
                    break;
                case "number":
                    this.setPhoneNumber(newValue);
                    break;
                case "birth":
                    this.birthday = newValue;
                    break;
                case "gender": {
                    if (!newValue.equals("M") && !newValue.equals("F")) {
                        System.out.println("Bad gender!");
                    } else {
                        this.gender = newValue;
                    }
                }
            }
        }
    }

    public String getFieldValue(String field) {
        switch (field) {
            case "name": {
                return this.name;
            }
            case "surname": {
                return this.surname;
            }

            case "gender": {
                return this.gender;
            }

            case "birth": {
                return this.birthday;
            }
            case "number": {
                return this.phoneNumber;
            }
        }
        return "";
    }

    @Override
    String getFullName() {
        return getName() + " " + getSurname();
    }


    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return  "Name: " + this.name  + "\n" +
                "Surname: " + this.surname + "\n" +
                "Birth date: " + this.birthday + "\n" +
                "Gender: " + this.gender + "\n" +
                "Number: " + this.phoneNumber + "\n" +
                "Time created: " + this.dateCreated + "\n" +
                "Time last edit: " + this.lastEdit;
    }


    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }


    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
    }
}
