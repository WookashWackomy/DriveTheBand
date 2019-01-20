package controller;

import com.google.api.services.drive.Drive;
import controller.helpers.GoogleDriveTransfer;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.media.AudioClip;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import model.Song;
import model.Track;
import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
//import com.google.api.services.drive.model.File;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static javafx.beans.binding.Bindings.size;

public class DriveTheBandController {
    @FXML
    private Label songTitleLabel;
    @FXML
    private Button deleteSelectedButton;
    @FXML
    private Button playButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button addNewTrackButton;

    private GoogleDriveTransfer googleDriveTransfer;
    private DriveTheBandAppController driveTheBandAppController;
    private ObservableList<Track> tracks = FXCollections.observableArrayList();
    private List<AudioClip> mediaPlayers = new ArrayList<>();
    private Song currentSong;

    @FXML
    public Button chooseSongButton;
    @FXML
    public TableView<Track> trackTable;
    @FXML
    public TableColumn<Track,String> trackName;
    @FXML
    public TableColumn<Track,String> creatorUser;
    @FXML
    public TableColumn<Track, LocalDateTime> creationDate;

    private Drive service;

    public void setService(Drive service) throws IOException {
        this.service = service;
        if(service != null) {
            System.out.println("service nullem nie jest");
        } else{
            System.out.println("service nullem jest");
        }
    }


    @FXML
    private void initialize() {
        googleDriveTransfer = new GoogleDriveTransfer();
        trackTable.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE);

        trackTable.setEditable(true);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        trackName.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        creatorUser.setCellValueFactory(cellData -> cellData.getValue().getUserNameProperty());
        creationDate.setCellValueFactory(cellData -> cellData.getValue().getDateProperty());
        creationDate.setCellFactory(col -> new TableCell<Track,LocalDateTime>(){
            @Override
            protected void updateItem(LocalDateTime date, boolean empty){
                super.updateItem(date,empty);
                if(empty){
                    setText("");
                } else{
                    setText(formatter.format(date));
                }
            }
        });
        playButton.disableProperty().bind(Bindings.or(size(tracks).isEqualTo(0),size(trackTable.getSelectionModel().getSelectedItems()).isEqualTo(0)));
        stopButton.disableProperty().bind(size(tracks).isEqualTo(0));
        addNewTrackButton.disableProperty().bind(size(tracks).isEqualTo(0));
        deleteSelectedButton.disableProperty().bind(Bindings.or(size(tracks).isEqualTo(0),size(trackTable.getSelectionModel().getSelectedItems()).isEqualTo(0)));

        currentSong = new Song("","");

    }

    public void handleChooseSongButton() {

        this.driveTheBandAppController.showChooseSongDialog();
        loadTracks();
        songTitleLabel.setText(currentSong.getNameProperty().getValue());
        songTitleLabel.setMaxWidth(Double.MAX_VALUE);
        songTitleLabel.setAlignment(Pos.CENTER);
    }

    public void handleDeleteSelectedButton() {
        List<Track> selectedTracks = trackTable.getSelectionModel().getSelectedItems();
        for(Track track : selectedTracks){
            try {
                    googleDriveTransfer.deleteFile(service, track.getDriveFileID());
                    tracks.remove(track);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public DriveTheBandController(){
    }

    public void setDriveTheBandAppController(DriveTheBandAppController driveTheBandAppController) {
        this.driveTheBandAppController = driveTheBandAppController;
    }


    public void handlePlayButton() {
        List<Track> selectedTracks = trackTable.getSelectionModel().getSelectedItems();
        try {
            if(!selectedTracks.isEmpty()) {
                for (Track track : selectedTracks) {
                    AudioClip mediaPlayer = new AudioClip(track.getPath().toURI().toString());
                    mediaPlayers.add(mediaPlayer);
                }
            }else{
                for (Track track : tracks) {
                    AudioClip mediaPlayer = new AudioClip(track.getPath().toURI().toString());
                    mediaPlayers.add(mediaPlayer);
                }
            }
            for (AudioClip mediaPlayer : mediaPlayers) {
                mediaPlayer.play();
            }
        }catch (URISyntaxException e){
            e.printStackTrace();
        }
    }

    public void handleStopButton() {
        for(AudioClip mediaPlayer: mediaPlayers){
            mediaPlayer.stop();
        }
        mediaPlayers.clear();
    }

    public void loadTracks() {
        tracks.clear();
        File dir = null;
        try {
            dir = new File(URLDecoder.decode(getClass().getResource("/SongFiles").getPath(), "UTF-8").substring(1));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(dir.getAbsolutePath());
        File[] dirList = dir.listFiles();
        JSONParser parser = new JSONParser();
        String parentID = null;
        String fileID = null;
        String songName = null;
        LocalDateTime date = null;
        for (File file : dirList) {
            try {
                if (FilenameUtils.getExtension(URLDecoder.decode(getClass().getResource("/SongFiles").getPath(), "UTF-8").substring(1) + file.getName()).equals("mp3")){
                    try {
                        Object obj = parser.parse(new FileReader(URLDecoder.decode(getClass().getResource("/SongFiles").getPath(), "UTF-8").substring(1) + "/" + file.getName() + ".json"));
                        JSONObject jsonObject = (JSONObject) obj;
                        parentID = (String) jsonObject.get("parentID");
                        fileID = (String) jsonObject.get("fileID");
                        String createdTime = (String) jsonObject.get("createdTime");
                        songName = service.files().get(parentID).setFields("id,name").execute().getName();
                        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                        date = LocalDateTime.parse(createdTime,formatter);
                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                    }
                    currentSong = new Song(songName, parentID);
                    Track track = new Track(currentSong, file.getName(), getClass().getResource("/SongFiles/" + file.getName()), fileID,date);
                    tracks.add(track);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        trackTable.setItems(tracks);
    }

    public void handleAddNewTrackButton() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio Files", "*.mp3")
        );
        File selectedFile = fileChooser.showOpenDialog(this.driveTheBandAppController.getPrimaryStage());
        if (selectedFile != null){
            try {
                System.out.println(selectedFile.getAbsolutePath());
                String newFileID = googleDriveTransfer.uploadFileToFolder(service, selectedFile.getAbsolutePath(), currentSong.getDriveFileID()).getId();
                Track track = new Track(currentSong, selectedFile.getName(), selectedFile.toURI().toURL(), newFileID, LocalDateTime.now());
                tracks.add(track);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        trackTable.setItems(tracks);
    }
}
