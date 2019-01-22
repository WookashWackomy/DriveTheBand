package view;

import com.sun.prism.paint.Paint;
import controller.DriveTheBandController;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Song;
import model.Track;
import model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static javafx.beans.binding.Bindings.size;

public class MainAppView{
    private BorderPane view;

    private DriveTheBandController driveTheBandController;
    private Song currentSong;
    private User currentUser;
    private ObservableList<Track> trackObservableList;


    public Button chooseSongButton;

    public TableView<Track> trackTable;

    public TableColumn<Track,String> trackStringTableColumn;

    public TableColumn<Track,String> creatorUserColumn;

    public TableColumn<Track, LocalDateTime> creationDateColumn;

    private VBox vBox;
    private Label songTitleLabel;
    private Label labelOfSongTitle;
    private TextField userNameTextField;
    private Button deleteSelectedButton;
    private Button playButton;
    private Button stopButton;
    private Button addNewTrackButton;
    private Button createNewSongButton;

    public MainAppView(DriveTheBandController driveTheBandController, Song currentSong, ObservableList<Track> trackObservableList, User currentUser) {
        this.driveTheBandController = driveTheBandController;
        this.trackObservableList = trackObservableList;
        this.currentSong = currentSong;
        this.currentUser = currentUser;

        createAndConfigurePane();
        configureTrackTable();
        setListeners();
        observeModelAndUpdateControls();


    }


    private void createAndConfigurePane() {
        this.view = new BorderPane();
        trackTable = new TableView<Track>();
        trackStringTableColumn = new TableColumn<Track,String>();
        creatorUserColumn = new TableColumn<Track,String>();
        creationDateColumn = new TableColumn<Track,LocalDateTime>();
        vBox = new VBox();
        songTitleLabel = new Label();
        songTitleLabel.setMaxWidth(Double.MAX_VALUE);
        songTitleLabel.setAlignment(Pos.CENTER);
        labelOfSongTitle = new Label("Song title:");
        labelOfSongTitle.setMaxWidth(Double.MAX_VALUE);
        labelOfSongTitle.setAlignment(Pos.CENTER);
        userNameTextField = new TextField();
        userNameTextField.setMaxWidth(Double.MAX_VALUE);
        userNameTextField.setAlignment(Pos.CENTER);
        chooseSongButton = new Button();
        deleteSelectedButton = new Button();
        playButton = new Button();
        stopButton = new Button();
        addNewTrackButton = new Button();
        createNewSongButton = new Button();

        view.setPrefHeight(400.0);
        view.setPrefWidth(600.0);
        view.setBackground(new Background(new BackgroundFill(Color.DARKGREY,CornerRadii.EMPTY, Insets.EMPTY)));

        view.setAlignment(trackTable, Pos.CENTER);
        trackTable.setPrefHeight(200.0);
        trackTable.setPrefWidth(500.0);
        trackTable.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY,CornerRadii.EMPTY, Insets.EMPTY)));

        trackStringTableColumn.setText("Name");

        creatorUserColumn.setText("Creator");

        creationDateColumn.setText("date of creation");
        trackTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        view.setLeft(trackTable);

        view.setAlignment(vBox, Pos.CENTER);
        vBox.setAlignment(Pos.TOP_RIGHT);
        vBox.setSpacing(20);
        vBox.setBackground(new Background(new BackgroundFill(Color.DARKGREY,CornerRadii.EMPTY, Insets.EMPTY)));

        view.setAlignment(songTitleLabel, Pos.CENTER);
        songTitleLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        songTitleLabel.setFont(Font.font("Gill Sans", FontWeight.BOLD,30));
        view.setAlignment(labelOfSongTitle, Pos.CENTER);
        labelOfSongTitle.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        labelOfSongTitle.setFont(Font.font("Tahoma", 20));

        view.setAlignment(chooseSongButton, Pos.TOP_RIGHT);
        chooseSongButton.setPrefWidth(200);
        chooseSongButton.setText("Choose a song");

        view.setAlignment(deleteSelectedButton, Pos.TOP_RIGHT);
        deleteSelectedButton.setPrefWidth(200);
        deleteSelectedButton.setText("delete selected...");

        view.setAlignment(playButton, Pos.TOP_RIGHT);
        playButton.setPrefWidth(200);
        playButton.setText("play selected track(s)");

        view.setAlignment(stopButton, Pos.TOP_RIGHT);
        stopButton.setPrefWidth(200);
        stopButton.setText("stop playing");

        view.setAlignment(addNewTrackButton, Pos.TOP_RIGHT);
        addNewTrackButton.setPrefWidth(200);
        addNewTrackButton.setText("add new stem");

        view.setAlignment(createNewSongButton, Pos.TOP_RIGHT);
        createNewSongButton.setText("create new song");

        view.setAlignment(userNameTextField, Pos.BOTTOM_CENTER);
        userNameTextField.setAlignment(Pos.CENTER);
        userNameTextField.setFont(Font.font("Gill Sans", FontWeight.BOLD,20));

        view.setRight(vBox);

        trackTable.getColumns().add(trackStringTableColumn);
        trackTable.getColumns().add(creatorUserColumn);
        trackTable.getColumns().add(creationDateColumn);
        vBox.getChildren().add(labelOfSongTitle);
        vBox.getChildren().add(songTitleLabel);
        vBox.getChildren().add(chooseSongButton);
        vBox.getChildren().add(deleteSelectedButton);
        vBox.getChildren().add(playButton);
        vBox.getChildren().add(stopButton);
        vBox.getChildren().add(addNewTrackButton);
        vBox.getChildren().add(createNewSongButton);
        vBox.getChildren().add(userNameTextField);
        userNameTextField.setText(" ");


    }

    private void configureTrackTable() {
        trackTable.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE);

        trackTable.setEditable(true);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        trackStringTableColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        creatorUserColumn.setCellValueFactory(cellData -> cellData.getValue().getCreatorNameProperty());
        creationDateColumn.setCellValueFactory(cellData -> cellData.getValue().getDateProperty());
        creationDateColumn.setCellFactory(col -> new TableCell<Track, LocalDateTime>(){
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
    }

    private void observeModelAndUpdateControls() {
        chooseSongButton.setOnAction(event -> {
            driveTheBandController.handleChooseSongButton();
        });
        deleteSelectedButton.setOnAction(event -> {
            List<Track> selectedTracks = trackTable.getSelectionModel().getSelectedItems();
            driveTheBandController.handleDeleteSelectedButton(selectedTracks);
        });
        playButton.setOnAction(event -> {
            List<Track> selectedTracks = trackTable.getSelectionModel().getSelectedItems();
            driveTheBandController.handlePlayButton(selectedTracks);
        });
        stopButton.setOnAction(event -> {
            List<Track> selectedTracks = trackTable.getSelectionModel().getSelectedItems();
            driveTheBandController.handleStopButton(selectedTracks);
        });
        addNewTrackButton.setOnAction(event -> driveTheBandController.handleAddNewTrackButton());
        createNewSongButton.setOnAction(event -> driveTheBandController.handleCreateNewSongButton());

        playButton.disableProperty().bind(Bindings.size(trackObservableList).isEqualTo(0));
        stopButton.disableProperty().bind(size(trackObservableList).isEqualTo(0));
        addNewTrackButton.disableProperty().bind(Bindings.isNull(currentSong.getNameProperty()));
        deleteSelectedButton.disableProperty().bind(Bindings.or(size(trackObservableList).isEqualTo(0),size(trackTable.getSelectionModel().getSelectedItems()).isEqualTo(0)));
        userNameTextField.setOnKeyReleased(event -> {
            currentUser.setName(userNameTextField.getText());
            event.consume();
        });
    }

    private void setListeners() {
        currentSong.getNameProperty().addListener((v,oldVal,newVal) ->{
            songTitleLabel.setText(currentSong.getNameProperty().getValue());
        });
        trackObservableList.addListener((ListChangeListener<Track>) c -> trackTable.setItems(trackObservableList));
    }

    public Parent asParent(){
        return view;
    }


}
