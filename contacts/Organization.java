package contacts;

import java.io.*;

public class Organization extends Contact implements Serializable {

    private static final long serialVersionUID = 3L;

    private String address;
    private final String fields = "(name, address, number";

    public Organization() {
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
                case "address":
                    this.address = newValue;
                    break;
                case "number":
                    this.setPhoneNumber(newValue);
                    break;
            }
        }
    }

    public String getFieldValue(String field) {
        switch (field) {
            case "name": {
                return this.name;
            }
            case "address": {
                return this.address;
            }
            case "number": {
                return this.phoneNumber;
            }
        }
        return "";
    }

    @Override
    String getFullName() {
        return getName();
    }

    @Override
    String getValues() {
        return this.name + " " + this.address + " " + this.getPhoneNumber();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Organization name: " + this.name  + "\n" +
                "Address: " + this.address + "\n" +
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
