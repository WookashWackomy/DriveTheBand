import com.google.api.services.drive.Drive;
import controller.helpers.GoogleDriveHook;
import controller.helpers.GoogleDriveTransfer;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class ServiceTests {

    private static Drive gDriveService;
    @BeforeClass
    public static void setUpClass(){
        try {
            GoogleDriveHook googleDriveHook = new GoogleDriveHook();
            gDriveService = googleDriveHook.getService();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void serviceExistTest(){
        Assertions.assertNotNull(gDriveService);
    }

    @Test
    public void serviceConnectionTest(){
        try {
            Assertions.assertNotNull(gDriveService.files().list()
                    .setPageSize(1)
                    .setFields("nextPageToken,files(id,name)")
                    .execute());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void serviceGetTest(){
        GoogleDriveTransfer googleDriveTransfer = new GoogleDriveTransfer();

        try {
            String folderID;
            Assertions.assertNotNull(folderID=googleDriveTransfer.getFolder("BandFolder",gDriveService).getId());
            Assertions.assertNotNull(googleDriveTransfer.getSubFolders(folderID,gDriveService));
            Assertions.assertNotNull(googleDriveTransfer.getFilesFromFolder(folderID,gDriveService));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
