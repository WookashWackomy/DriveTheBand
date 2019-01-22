package controller;

import com.google.api.services.drive.Drive;
import controller.helpers.GoogleDriveTransfer;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import model.Song;
import model.Track;
import model.User;
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

public class DriveTheBandController{
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
    private ObservableList<Track> tracks;
    private List<MediaPlayer> mediaPlayers = new ArrayList<>();
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
    private User currentUser;
    private List<User> userList;

    public void setService(Drive service) throws IOException {
        this.service = service;
        if(service != null) {
            System.out.println("service nullem nie jest");
        } else{
            System.out.println("service nullem jest");
        }
    }

    public void handleChooseSongButton() {

        this.driveTheBandAppController.showChooseSongDialog();
        loadTracks();
    }

    public void handleDeleteSelectedButton(List<Track> selectedTracks) {
        for(Track track : selectedTracks){
            try {
                    googleDriveTransfer.deleteFile(service, track.getDriveFileID());
                    tracks.remove(track);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public DriveTheBandController(Song currentSong,ObservableList<Track> tracks,User currentUser){
        this.currentSong = currentSong;
        this.tracks = tracks;
        this.googleDriveTransfer = new GoogleDriveTransfer();
        this.currentUser = currentUser;
        this.userList = new ArrayList<User>();
    }

    public void setDriveTheBandAppController(DriveTheBandAppController driveTheBandAppController) {
        this.driveTheBandAppController = driveTheBandAppController;
    }


    public void handlePlayButton(List<Track> selectedTracks) {

        try {
            if(!selectedTracks.isEmpty()) {
                for (Track track : selectedTracks) {
                    for(MediaPlayer mediaPlayerInList: mediaPlayers){
                        if(mediaPlayerInList.getMedia().getSource().toString().equals(track.getPath().toURI().toString())){
                            mediaPlayerInList.stop();
                            mediaPlayerInList.dispose();
                            mediaPlayers.remove(mediaPlayerInList);
                        }
                    }
                    Media playedMedia = new Media(track.getPath().toURI().toString());
                    MediaPlayer mediaPlayer = new MediaPlayer(playedMedia);
                    mediaPlayers.add(mediaPlayer);
                }
            }else{
                stopAndClearMediaPlayers();
                for (Track track : tracks) {
                    Media playedMedia = new Media(track.getPath().toURI().toString());
                    MediaPlayer mediaPlayer = new MediaPlayer(playedMedia);
                    mediaPlayers.add(mediaPlayer);
                }
            }
            Duration currentTime = mediaPlayers.get(0).getCurrentTime();
            for (MediaPlayer mediaPlayer : mediaPlayers) {
                mediaPlayer.setStartTime(currentTime);
                mediaPlayer.play();
            }
        }catch (URISyntaxException e){
            e.printStackTrace();
        }
    }

    public void handleStopButton(List<Track> selectedTracks) {
            stopAndClearMediaPlayers();
    }

    private void stopAndClearMediaPlayers() {
        for(MediaPlayer mediaPlayer: mediaPlayers){
            mediaPlayer.stop();
            mediaPlayer.dispose();
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
        String userName = null;
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
                        userName = (String) jsonObject.get("description");
                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                    }
                    User songUser = new User(userName);

                    currentSong.setName(songName);
                    currentSong.setID(parentID);
                    Track track = new Track(currentSong, file.getName(), getClass().getResource("/SongFiles/" + file.getName()), fileID,date);
                    track.setCreator(songUser);
                    tracks.add(track);
                }
                currentSong.setTracks(tracks);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
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
                String newFileID = googleDriveTransfer.uploadFileToFolder(service, selectedFile.getAbsolutePath(), currentSong.getDriveFileID(),currentUser.getNameProperty().getValue()).getId();
                Track track = new Track(currentSong, selectedFile.getName(), selectedFile.toURI().toURL(), newFileID, LocalDateTime.now());
                track.setCreator(new User(currentUser.getNameProperty().getValue()));
                tracks.add(track);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void handleCreateNewSongButton(){
        this.driveTheBandAppController.showCreateSongDialog();
        this.driveTheBandAppController.showChooseSongDialog();
    }
}
