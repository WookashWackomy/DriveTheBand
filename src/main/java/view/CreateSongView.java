package view;

import controller.CreateSongPresenter;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.Song;
import model.User;

public class CreateSongView {
    private Song song;
    private User currentUser;
    private BorderPane view;
    private CreateSongPresenter createSongPresenter;

    private TextField songTitleTextField;
    private TextField userNameTextField;

    private HBox hBox;
    private VBox vBox;
    private Button cancelButton;
    private Button okButton;
    private Label songTitleLabel;
    private Label userNameLabel;

    public CreateSongView(CreateSongPresenter createSongPresenter, Song currentSong, User currentUser) {
        this.song = currentSong;
        this.currentUser = currentUser;
        hBox = new HBox();
        vBox = new VBox();
        cancelButton = new Button();
        okButton = new Button();
        songTitleTextField = new TextField();
        userNameTextField = new TextField();
        songTitleLabel = new Label();
        userNameLabel = new Label();

        this.createSongPresenter = createSongPresenter;
        createAndConfigurePane();
        setOnAction();



        view.setPrefHeight(400);
        view.setPrefWidth(200);



        BorderPane.setAlignment(hBox, javafx.geometry.Pos.CENTER);
        hBox.setAlignment(javafx.geometry.Pos.BOTTOM_RIGHT);
        hBox.setSpacing(5.0);

        vBox.setPadding(new Insets(10));
        vBox.setSpacing(5);

        cancelButton.setMnemonicParsing(false);
        cancelButton.setPrefWidth(60.0);
        cancelButton.setText("Cancel");
        HBox.setMargin(cancelButton, new Insets(5.0));

        okButton.setMnemonicParsing(false);
        okButton.setPrefWidth(60.0);
        okButton.setText("Ok");
        HBox.setMargin(okButton, new Insets(5.0));
        BorderPane.setMargin(hBox, new Insets(5.0, 5.0, 0.0, 5.0));

        songTitleTextField.setPromptText("insert currentSong title here");
        userNameTextField.setPromptText("insert creator name here");

        songTitleLabel.setText("New song title");
        songTitleLabel.setAlignment(Pos.CENTER);
        songTitleLabel.setTextAlignment(TextAlignment.CENTER);

        userNameLabel.setText("song description:");
        userNameLabel.setAlignment(Pos.CENTER);
        userNameLabel.setTextAlignment(TextAlignment.CENTER);

        view.setBottom(hBox);
        view.setCenter(vBox);
        vBox.getChildren().add(songTitleLabel);
        vBox.getChildren().add(songTitleTextField);
        vBox.getChildren().add(userNameLabel);
        vBox.getChildren().add(userNameTextField);
        hBox.getChildren().add(cancelButton);
        hBox.getChildren().add(okButton);

    }

    private void createAndConfigurePane() {
        this.view = new BorderPane();

        okButton.disableProperty().bind(Bindings.isEmpty(songTitleTextField.textProperty())
                                        .or(Bindings.isEmpty(userNameTextField.textProperty()))
        );

    }

    private void setOnAction(){
        cancelButton.setOnAction(event -> closeWindow());
        okButton.setOnAction(event -> {
            String songName = songTitleTextField.getText();
            String userName = userNameTextField.getText();
            createSongPresenter.handleCreateAction(songName,userName);
            closeWindow();
        });
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public Parent asParent(){
        return view;
    }

}
