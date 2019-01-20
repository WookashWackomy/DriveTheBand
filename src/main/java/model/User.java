package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.beans.SimpleBeanInfo;

public class User extends DriveNode {


    public User(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public StringProperty getNameProperty(){
        return name;
    }
}
