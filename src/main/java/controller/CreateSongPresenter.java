package controller;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import controller.helpers.GoogleDriveTransfer;
import javafx.collections.ObservableList;
import model.Song;
import model.User;

import java.io.IOException;

public class CreateSongPresenter {

    private GoogleDriveTransfer googleDriveTransfer;
    private ObservableList<Song> songList;
    private Drive service;
    private Song currentSong;
    private User currentUser;

    public CreateSongPresenter(ObservableList<Song> songList, Song currentSong,User currentUser){
        this.currentSong = currentSong;
        this.songList = songList;
        this.googleDriveTransfer = new GoogleDriveTransfer();
        this.currentUser = currentUser;
    }

    public void handleCreateAction(String songName, String userName) {
        try {
            currentSong.setName(songName);
            currentUser.setName(userName);
            currentSong.setUser(currentUser);
            currentUser.setName(userName);
            String rootFolderID = googleDriveTransfer.getFolder("BandFolder",service).getId();
            File createdSong = googleDriveTransfer.createFolder(service,currentSong.getNameProperty().getValue(),rootFolderID,currentUser.getNameProperty().getValue());
            currentSong.setID(createdSong.getId());
            songList.add(currentSong);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setService(Drive service){this.service = service;}
}
