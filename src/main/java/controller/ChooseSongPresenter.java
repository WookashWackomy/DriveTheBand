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

    @FXML
    private TableView<String> songListTable;
    @FXML
    private TableColumn<String, String> songNameColumn;

    private Drive service;
    private Stage stage;
    private GoogleDriveTransfer googleDriveTransfer;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void setService(Drive service){this.service = service;}

    @FXML
    private void initialize(){
        this.googleDriveTransfer = new GoogleDriveTransfer();
        songNameColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue()));
        songListTable.setItems(FXCollections.observableArrayList());
    }
    public void songNamesToTable() {
        FileList result = null;
        List<File> subfolders = null;

        File folder = null;
        try {
            folder = googleDriveTransfer.getFolder("BandFolder",service);
            String folderID = folder.getId();
            System.out.println("folder: " + folder.getName() + ' ' + folder.getId());
            result = service.files().list()
                    .setQ(String.format("'%s' in parents and mimeType = 'application/vnd.google-apps.folder'", folderID))
                    .setFields("nextPageToken, files(id, name,createdTime,owners)")
                    .execute();
            subfolders = result.getFiles();
        }catch (IOException e) {
            e.printStackTrace();
        }
        for (File song : subfolders) {
            System.out.println(song.getName() + " " + song.getCreatedTime() + " " + song.getOwners().get(0).getDisplayName());
            songListTable.getItems().add(song.getName());
        }
    }

    public void handleCancelAction(ActionEvent actionEvent) {
        stage.close();
    }

    public void handleOkAction(ActionEvent actionEvent) {
        String pageToken = null;
        String folderName = songListTable.getSelectionModel().getSelectedItem();
        
        FileList result = null;
        List<File> files = null;

        File folder = null;
        try {
            folder = googleDriveTransfer.getFolder(folderName,service);

        String folderID = folder.getId();
        System.out.println("folder: " + folder.getName() + ' ' + folder.getId());
        result = service.files().list()
                .setQ(String.format("'%s' in parents and mimeType != 'application/vnd.google-apps.folder'", folderID))
                .setFields("nextPageToken, files(id, name,createdTime,owners,parents,createdTime)")
                .execute();
        files = result.getFiles();
        }catch (IOException e) {
            e.printStackTrace();
        }
        try {
            deleteTracksFromResources();
            googleDriveTransfer.downloadTracks(files,service);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.close();
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
