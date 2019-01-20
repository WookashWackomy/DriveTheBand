package controller.helpers;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GoogleDriveTransfer {

    public GoogleDriveTransfer(){
    }

    public void downloadFile(Drive service,File file) throws IOException {
        String outputPath = URLDecoder.decode(getClass().getResource("/SongFiles/").getPath(),"UTF-8").substring(1) + file.getName();
        System.out.println("out =>" + outputPath);
        OutputStream outputStream= new FileOutputStream(outputPath);
        service.files().get(file.getId()).executeMediaAndDownloadTo(outputStream);
        outputStream.close();

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("fileID",file.getId());
        jsonObject.put("parentID",file.getParents().get(0));
        jsonObject.put("createdTime",file.getCreatedTime().toString());

        try(FileWriter localFile = new FileWriter(URLDecoder.decode(getClass().getResource("/SongFiles/").getPath(),"UTF-8").substring(1) + file.getName() + ".json")){
            localFile.write(jsonObject.toString());
            localFile.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println(jsonObject);
    }

    public File uploadFileToFolder(Drive service, String path, String parentId) throws IOException {
        String pageToken = null;
        File fileMetadata = new File();
        fileMetadata.setParents(Collections.singletonList(parentId));
        java.io.File filePath = new java.io.File(path);
        fileMetadata.setName(filePath.getName());
        FileContent mediaContent = new FileContent("audio/mp3", filePath);
        File fileToUpload;
        List<File> files = service.files().list()
                .setQ(String.format("name = '%s' and '%s' in parents",filePath.getName(),parentId))
                .setFields("nextPageToken,files(id,name)")
                .setPageToken(pageToken)
                .execute()
                .getFiles();
        if(files.isEmpty()){
         fileToUpload = service.files().create(fileMetadata, mediaContent)
                .setFields("id,parents")
                .execute();
        System.out.println(fileToUpload.getName() + " " + fileToUpload.getId());
        }else{
            fileToUpload = files.get(0);
        }
        return fileToUpload;
    }

    public File getFolder(String folderName,Drive service) throws IOException {
        String pageToken = null;
        FileList result = service.files().list()
                .setQ(String.format("name = '%s' and mimeType = 'application/vnd.google-apps.folder'",folderName))
                .setFields("nextPageToken, files(id, name)")
                .setPageToken(pageToken)
                .execute();
        List<File> folders = result.getFiles();
        if (folders == null || folders.isEmpty()) {
            System.out.println("No files found.");
        } else{
            return folders.get(0);
        }
        return null;
    }

    public void downloadTracks(List<File> tracks,Drive service) throws IOException {
        if(tracks != null) {
            for (File track : tracks) {
                System.out.println(track.getName() + " " + track.getCreatedTime() + " " + track.getOwners().get(0).getDisplayName());
                downloadFile(service,track);
            }
        }
    }

    public void deleteFile(Drive service, String driveFileID) throws IOException {
        service.files().delete(driveFileID).execute();
    }
}
