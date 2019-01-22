package controller;

import com.google.api.services.drive.Drive;
import controller.helpers.GoogleDriveHook;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Song;
import model.Track;
import model.User;
import view.ChooseSongView;
import view.CreateSongView;
import view.MainAppView;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DriveTheBandAppController {
    private Drive service;
    private DriveTheBandController driveTheBandController;
    private Stage primaryStage;
    private MainAppView mainAppView;
    private ObservableList<Track> tracks;
    private Song currentSong;
    private User currentUser;
    private ObservableList<Song> songList;
    private ChooseSongPresenter chooseSongPresenter;
    private CreateSongPresenter createSongPresenter;

    public DriveTheBandAppController(Stage primaryStage) {

        tracks = FXCollections.observableArrayList();
        songList = FXCollections.observableArrayList();
        currentSong = new Song();
        currentUser = new User();
        this.driveTheBandController = new DriveTheBandController(currentSong,tracks,currentUser);
        this.primaryStage = primaryStage;
        this.mainAppView = new MainAppView(driveTheBandController,currentSong,tracks,currentUser);
    }

    public void initialize() {
        initGoogleDrive();
        initLayout();
        chooseSongPresenter = new ChooseSongPresenter(songList);
        chooseSongPresenter.setService(service);
        chooseSongPresenter.setSongListTable();
        createSongPresenter = new CreateSongPresenter(songList,currentSong,currentUser);
        createSongPresenter.setService(service);
        driveTheBandController.setService(service);
        driveTheBandController.setDriveTheBandAppController(this);

    }

    public void initGoogleDrive(){
        GoogleDriveHook googleDriveHook = null;
        try {
            googleDriveHook = new GoogleDriveHook();
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }

        if (googleDriveHook != null) {
            service = googleDriveHook.getService();
        }
    }

    public void initLayout(){
            Scene scene = new Scene(mainAppView.asParent());
            primaryStage.setScene(scene);
            primaryStage.show();

    }



    public void showChooseSongDialog(){
        try {
            ChooseSongView chooseSongView = new ChooseSongView(chooseSongPresenter,songList);
            Stage stage = new Stage();
            Scene scene = new Scene(chooseSongView.asParent());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(primaryStage);
            stage.showAndWait();
        }catch(Exception e){
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
        for(Song s: songList){
            System.out.println(s.getNameProperty().getValue());
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void showCreateSongDialog() {
        CreateSongView createSongView = new CreateSongView(createSongPresenter,currentSong,currentUser);
        Stage stage = new Stage();
        Scene scene = new Scene(createSongView.asParent());
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(primaryStage);
        stage.showAndWait();

    }
}
