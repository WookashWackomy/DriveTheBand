package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

public class Song extends DriveNode{
    private List<Track> tracks = new ArrayList<>();
    private User user;

    public Song(String name,String driveFileID) {
        this.driveFileID = driveFileID;
        this.name = new SimpleStringProperty(name);
    }

    public Song(){
        this.driveFileID = null;
        this.name = new SimpleStringProperty(null);
    }

    public void addTrack(Track track){
        tracks.add(track);
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public final StringProperty getNameProperty() { return name; }

    public void setName(String newName){
        this.name.setValue(newName);
    }

    public void setID(String newID){
        this.driveFileID = newID;
    }
    public void setTracks(List<Track> newTracks){
        this.tracks = newTracks;
    }

    public void addTracks(List<Track> tracks){
        for(Track t: tracks){
            tracks.add(t);
        }
    }

    public void setUser(User user) {
        this.user = user;
    }
}
