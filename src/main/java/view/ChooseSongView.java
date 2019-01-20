package view;

import javafx.geometry.*;
import javafx.scene.control.*;
import java.lang.*;
import javafx.scene.layout.*;
import javafx.scene.layout.GridPane;

public abstract class ChooseSongView extends BorderPane {

    private final TableView songListTable;
    private final TableColumn songNameColumn;
    private final HBox hBox;
    private final Button button;
    private final Button button0;

    public ChooseSongView() {

        songListTable = new TableView();
        songNameColumn = new TableColumn();
        hBox = new HBox();
        button = new Button();
        button0 = new Button();

        setPrefHeight(400);
        setPrefWidth(200);

        songNameColumn.setText("Choose song");
        songListTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setCenter(songListTable);

        BorderPane.setAlignment(hBox, javafx.geometry.Pos.CENTER);
        hBox.setAlignment(javafx.geometry.Pos.BOTTOM_RIGHT);
        hBox.setSpacing(5.0);

        button.setMnemonicParsing(false);
        button.setOnAction(this::handleCancelAction);
        button.setPrefWidth(60.0);
        button.setText("Cancel");
        HBox.setMargin(button, new Insets(5.0));

        button0.setMnemonicParsing(false);
        button0.setOnAction(this::handleOkAction);
        button0.setPrefWidth(60.0);
        button0.setText("Ok");
        HBox.setMargin(button0, new Insets(5.0));
        BorderPane.setMargin(hBox, new Insets(5.0, 5.0, 0.0, 5.0));
        setBottom(hBox);

        songListTable.getColumns().add(songNameColumn);
        hBox.getChildren().add(button);
        hBox.getChildren().add(button0);

    }

    protected abstract void handleCancelAction(javafx.event.ActionEvent actionEvent);

    protected abstract void handleOkAction(javafx.event.ActionEvent actionEvent);

}
