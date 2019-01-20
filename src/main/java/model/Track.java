package model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Track extends DriveNode {
    private User creator;
    private ObjectProperty<LocalDateTime> date;
    private Song song;

    public Track(Song song,String name, URL path,String driveFileID,LocalDateTime date){
        this.name = new SimpleStringProperty( name);
        this.path = path;
        this.date = new SimpleObjectProperty<LocalDateTime>(date);
        this.creator = new User("");
        this.driveFileID = driveFileID;
        this.song = song;
    }

    public void setCreator(User creator) { this.creator = creator; }

    public final StringProperty getNameProperty() { return name; }

    public LocalDateTime getDate() { return date.get(); }

    public ObjectProperty<LocalDateTime> getDateProperty() { return date; }

    public StringProperty getUserNameProperty(){return creator.getNameProperty();}

    public void setDate(LocalDateTime date) {
        this.date.set(date);
    }

    public URL getPath() {return this.path;}

    public Song getSong() {
        return song;
    }


}
