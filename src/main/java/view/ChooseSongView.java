package view;

import controller.ChooseSongPresenter;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Parent;
import javafx.scene.control.*;
import java.lang.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Song;
import model.Track;

public class ChooseSongView{
    private BorderPane view;
    private ChooseSongPresenter chooseSongPresenter;
    private ObservableList<Song> songObservableList;

    private TableView<Song> songTable;
    private TableColumn<Song,String> songNameColumn;
    private HBox hBox;
    private Button cancelButton;
    private Button okButton;

    public ChooseSongView(ChooseSongPresenter chooseSongPresenter, ObservableList<Song> songObservableList) {
        this.songObservableList = songObservableList;
        songTable = new TableView<Song>();
        songNameColumn = new TableColumn<Song,String>();
        hBox = new HBox();
        cancelButton = new Button();
        okButton = new Button();

        this.chooseSongPresenter = chooseSongPresenter;
        createAndConfigurePane();
        setListeners();


        view.setPrefHeight(400);
        view.setPrefWidth(200);

        songNameColumn.setText("Choose song");
        songTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        view.setCenter(songTable);

        BorderPane.setAlignment(hBox, javafx.geometry.Pos.CENTER);
        hBox.setAlignment(javafx.geometry.Pos.BOTTOM_RIGHT);
        hBox.setSpacing(5.0);

        cancelButton.setMnemonicParsing(false);
        cancelButton.setPrefWidth(60.0);
        cancelButton.setText("Cancel");
        HBox.setMargin(cancelButton, new Insets(5.0));

        okButton.setMnemonicParsing(false);
        okButton.setPrefWidth(60.0);
        okButton.setText("Ok");
        HBox.setMargin(okButton, new Insets(5.0));
        BorderPane.setMargin(hBox, new Insets(5.0, 5.0, 0.0, 5.0));
        view.setBottom(hBox);

        songTable.getColumns().add(songNameColumn);
        hBox.getChildren().add(cancelButton);
        hBox.getChildren().add(okButton);
        songTable.setItems(songObservableList);
    }

    private void createAndConfigurePane() {
        this.view = new BorderPane();
        songNameColumn.setCellValueFactory(cellData ->
                cellData.getValue().getNameProperty());
        songTable.setItems(FXCollections.observableArrayList());
        okButton.disableProperty().bind(Bindings.size(songTable.getSelectionModel().getSelectedItems()).isNotEqualTo(1));
    }

    private void setListeners(){
        cancelButton.setOnAction(event -> closeWindow());
        okButton.setOnAction(event -> {
            String songName = songTable.getSelectionModel().getSelectedItem().getNameProperty().getValue();
            chooseSongPresenter.handleOkAction(songName);
            closeWindow();
        });
        songObservableList.addListener((ListChangeListener<Song>) c -> songTable.setItems(songObservableList));

    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public Parent asParent(){
        return view;
    }

}
