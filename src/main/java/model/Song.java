package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

public class Song extends DriveNode{
    private List<Track> tracks = new ArrayList<>();

    public Song(String name,String driveFileID) {
        this.driveFileID = driveFileID;
        this.name = new SimpleStringProperty(name);
    }

    public void AddTrack(Track track){
        tracks.add(track);
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public final StringProperty getNameProperty() { return name; }



}
