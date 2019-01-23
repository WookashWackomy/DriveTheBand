package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.beans.SimpleBeanInfo;

public class User {
    private StringProperty name;

    public User(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public User(){this.name = new SimpleStringProperty(null);}

    public StringProperty getNameProperty(){
        return name;
    }

    public void setName(String text) {
        this.name.setValue(text);
    }


}
