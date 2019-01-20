import controller.DriveTheBandAppController;
import javafx.application.Application;
import javafx.stage.Stage;

public class DriveTheBandApp extends Application {

    private Stage primaryStage;
    private DriveTheBandAppController driveTheBandAppController;

    @Override
    public void start(Stage primaryStage){
        try{
            this.primaryStage = primaryStage;
            this.primaryStage.setTitle("Drive The Band");

            this.driveTheBandAppController = new DriveTheBandAppController(primaryStage);
            this.driveTheBandAppController.initialize();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String... args){
        Application.launch(args);
    }
}
