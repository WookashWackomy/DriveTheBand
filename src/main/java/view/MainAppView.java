package view;

import controller.DriveTheBandController;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import model.Song;
import model.Track;

import java.util.List;

public class MainAppView{
    private BorderPane view;

    private DriveTheBandController driveTheBandController;
    private Song song;
    private List<Track> trackList;

    private TableView trackTable;
    private TableColumn trackName;
    private TableColumn creatorUser;
    private TableColumn creationDate;
    private VBox vBox;
    private Label songTitleLabel;
    private Button chooseSongButton;
    private Button deleteSelectedButton;
    private Button playButton;
    private Button stopButton;
    private Button addNewTrackButton;

    public MainAppView(DriveTheBandController driveTheBandController, Song song, List<Track> trackList) {
        this.driveTheBandController = driveTheBandController;
        this.song = song;
        this.trackList = trackList;

        createAndConfigurePane();
        updateControllerFromListeners();
        observeModelAndUpdateControls();


    }

    private void createAndConfigurePane() {
        this.view = new BorderPane();
        trackTable = new TableView();
        trackName = new TableColumn();
        creatorUser = new TableColumn();
        creationDate = new TableColumn();
        vBox = new VBox();
        songTitleLabel = new Label();
        chooseSongButton = new Button();
        deleteSelectedButton = new Button();
        playButton = new Button();
        stopButton = new Button();
        addNewTrackButton = new Button();

        view.setPrefHeight(400.0);
        view.setPrefWidth(600.0);

        view.setAlignment(trackTable, javafx.geometry.Pos.CENTER);
        trackTable.setPrefHeight(200.0);
        trackTable.setPrefWidth(500.0);

        trackName.setText("Name");

        creatorUser.setText("Creator");

        creationDate.setText("date of creation");
        trackTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        view.setLeft(trackTable);

        view.setAlignment(vBox, javafx.geometry.Pos.CENTER);
        vBox.setAlignment(javafx.geometry.Pos.TOP_RIGHT);
        vBox.setSpacing(20);

        view.setAlignment(songTitleLabel, javafx.geometry.Pos.CENTER);
        songTitleLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        songTitleLabel.setFont(new Font("Gill Sans", 20));

        view.setAlignment(chooseSongButton, javafx.geometry.Pos.TOP_RIGHT);
        chooseSongButton.setOnAction(this::handleChooseSongButton);
        chooseSongButton.setPrefWidth(200);
        chooseSongButton.setText("Choose a song");

        view.setAlignment(deleteSelectedButton, javafx.geometry.Pos.TOP_RIGHT);
        deleteSelectedButton.setOnAction(this::handleDeleteSelectedButton);
        deleteSelectedButton.setPrefWidth(200);
        deleteSelectedButton.setText("delete selected...");

        view.setAlignment(playButton, javafx.geometry.Pos.TOP_RIGHT);
        playButton.setOnAction(this::handlePlayButton);
        playButton.setPrefWidth(200);
        playButton.setText("play selected tracks");

        view.setAlignment(stopButton, javafx.geometry.Pos.TOP_RIGHT);
        stopButton.setOnAction(this::handleStopButton);
        stopButton.setPrefWidth(200);
        stopButton.setText("stop playing");

        view.setAlignment(addNewTrackButton, javafx.geometry.Pos.TOP_RIGHT);
        addNewTrackButton.setOnAction(this::handleAddNewTrackButton);
        addNewTrackButton.setPrefWidth(200);
        addNewTrackButton.setText("add new stem");
        view.setRight(vBox);

        trackTable.getColumns().add(trackName);
        trackTable.getColumns().add(creatorUser);
        trackTable.getColumns().add(creationDate);
        vBox.getChildren().add(songTitleLabel);
        vBox.getChildren().add(chooseSongButton);
        vBox.getChildren().add(deleteSelectedButton);
        vBox.getChildren().add(playButton);
        vBox.getChildren().add(stopButton);
        vBox.getChildren().add(addNewTrackButton);

    }
    private void observeModelAndUpdateControls() {
        deleteSelectedButton.setOnAction(driveTheBandController);
    }

    private void updateControllerFromListeners() {

    }

    public Parent asParent(){
        return view;
    }

//    protected  void handleChooseSongButton(javafx.event.ActionEvent actionEvent);
//
//    protected  void handleDeleteSelectedButton(javafx.event.ActionEvent actionEvent);
//
//    protected  void handlePlayButton(javafx.event.ActionEvent actionEvent);
//
//    protected  void handleStopButton(javafx.event.ActionEvent actionEvent);
//
//    protected  void handleAddNewTrackButton(javafx.event.ActionEvent actionEvent);

}
