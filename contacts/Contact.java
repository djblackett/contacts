package contacts;

import java.io.Serializable;

abstract class Contact implements Serializable {

    private static final long serialVersionUID = 4L;

    String name;
    String phoneNumber = "";
    String dateCreated;
    String lastEdit;



    abstract String getFields();
    abstract void changeField(String field, String newValue);
    abstract String getFieldValue(String field);

    public boolean hasNumber() {
        return !this.phoneNumber.equals("");
    }

    abstract String getFullName();

    public String getName() {
        return name;
    }

    abstract String getValues();

    public void setName(String name) {
        this.name = name;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (isPhoneNumberValid(phoneNumber)) {
            this.phoneNumber = phoneNumber;
        }

        else {
            this.phoneNumber = "[no number]";
        }
    }

    public boolean checkNumber(String number) {
        return isPhoneNumberValid(number);
    }


    private boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber.matches("^\\+?\\d? ?(\\(?[\\da-zA-Z]{2,}\\)?)?-? ?(\\(?[\\da-zA-Z]{2,}\\))?-?([\\da-zA-Z]{2,})?-? ?([\\da-zA-Z]{2,})?-? ?([\\da-zA-Z]{2,})?$")
                && !phoneNumber.matches(".*\\(.* .*\\).*");
    }



    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getLastEdit() {
        return lastEdit;
    }

    public void setLastEdit(String lastEdit) {
        this.lastEdit = lastEdit;
    }
}
