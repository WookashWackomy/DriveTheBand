package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.net.URL;

public abstract class DriveNode {
    protected URL path;
    protected StringProperty name;
    protected String driveFileID;

    public String getDriveFileID(){return driveFileID;}

}
