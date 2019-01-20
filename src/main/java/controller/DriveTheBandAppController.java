package controller;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import controller.ChooseSongPresenter;
import controller.DriveTheBandController;
import controller.helpers.GoogleDriveHook;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Track;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DriveTheBandAppController {
    private Drive service;
    private DriveTheBandController driveTheBandController;
    private ChooseSongPresenter chooseSongPresenter;
    private Stage primaryStage;

    public DriveTheBandAppController(Stage primaryStage) {
        this.driveTheBandController = new DriveTheBandController();
        this.primaryStage = primaryStage;
    }

    public void initialize() throws IOException {
        initGoogleDrive();
        initLayout();
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
        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/MainAppView.fxml"));
            Parent rootLayout = (Parent) fxmlLoader.load();

            this.driveTheBandController = fxmlLoader.getController();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public Drive getService() {
        return service;
    }

    public void showChooseSongDialog(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/ChooseSongView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            chooseSongPresenter = fxmlLoader.getController();
            chooseSongPresenter.setStage(stage);
            chooseSongPresenter.setService(service);
            chooseSongPresenter.songNamesToTable();
            stage.setTitle("Choose song");
            stage.setScene(scene);
            stage.showAndWait();
        }catch(Exception e){
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
