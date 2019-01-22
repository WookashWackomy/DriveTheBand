package controller;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import controller.helpers.GoogleDriveTransfer;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Song;
import model.Track;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ChooseSongPresenter {

    private List<Song> songList;
    @FXML
    private TableColumn<Song, String> songNameColumn;

    private Drive service;
    private GoogleDriveTransfer googleDriveTransfer;

    public ChooseSongPresenter(List<Song> songList){
        this.songList = songList;
        this.googleDriveTransfer = new GoogleDriveTransfer();
    }

    public void setService(Drive service){this.service = service;}


    public void setSongListTable() {
        FileList result = null;
        List<File> subfolders = null;

        File folder = null;
        try {
            folder = googleDriveTransfer.getFolder("BandFolder",service);
            String folderID = folder.getId();
            System.out.println("folder: " + folder.getName() + ' ' + folder.getId());
            subfolders = googleDriveTransfer.getSubFolders(folderID,service);
        }catch (IOException e) {
            e.printStackTrace();
        }
        if( subfolders != null) {
            for (File song : subfolders) {
                System.out.println(song.getName() + " " + song.getCreatedTime() + " " + song.getOwners().get(0).getDisplayName());
                songList.add(new Song(song.getName(), song.getId()));
            }
        }
    }



    public void handleOkAction(String folderName) {
        String pageToken = null;
        

        List<File> files = null;

        File folder = null;
        try {
            folder = googleDriveTransfer.getFolder(folderName,service);

        String folderID = folder.getId();
        System.out.println("folder: " + folder.getName() + ' ' + folder.getId());
        files = googleDriveTransfer.getFilesFromFolder(folderID,service);

        }catch (IOException e) {
            e.printStackTrace();
        }
        try {
            deleteTracksFromResources();
            googleDriveTransfer.downloadTracks(files,service);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void deleteTracksFromResources() throws UnsupportedEncodingException {
        java.io.File dir = new java.io.File(URLDecoder.decode(getClass().getResource("/SongFiles").getPath(),"UTF-8").substring(1));
        System.out.println(dir.getAbsolutePath());
        java.io.File[] dirList = dir.listFiles();
        for(java.io.File file: dirList){
            file.delete();
        }
    }
}
