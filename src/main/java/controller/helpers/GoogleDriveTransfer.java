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
        String outputPath = URLDecoder.decode(getClass().getResource("/SongFiles/").getPath(),"UTF-8") + file.getName();
        System.out.println("out =>" + outputPath);
        java.io.File file2 = new java.io.File(outputPath);
        file2.createNewFile();
        OutputStream outputStream= new FileOutputStream(file2);
        service.files().get(file.getId()).executeMediaAndDownloadTo(outputStream);
        outputStream.close();

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("fileID",file.getId());
        jsonObject.put("parentID",file.getParents().get(0));
        jsonObject.put("createdTime",file.getCreatedTime().toString());
        if(file.getDescription()== null){
            jsonObject.put("description","");
        }
        else{
            jsonObject.put("description",file.getDescription().toString());
        }

        try(FileWriter localFile = new FileWriter(URLDecoder.decode(getClass().getResource("/SongFiles/").getPath(),"UTF-8") + file.getName() + ".json")){
            localFile.write(jsonObject.toString());
            localFile.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println(jsonObject);
    }

    public File uploadFileToFolder(Drive service, String path, String parentId,String description) throws IOException {
        String pageToken = null;
        File fileMetadata = new File();
        fileMetadata.setParents(Collections.singletonList(parentId));
        java.io.File filePath = new java.io.File(path);
        fileMetadata.setName(filePath.getName());
        fileMetadata.setDescription(description);
        FileContent mediaContent = new FileContent("audio/mp3", filePath);
        File fileToUpload;
        List<File> files = service.files().list()
                .setQ(String.format("name = '%s' and '%s' in parents and trashed=false",filePath.getName(),parentId))
                .setFields("nextPageToken,files(id,name)")
                .setPageToken(pageToken)
                .execute()
                .getFiles();
        if(files.isEmpty()){
         fileToUpload = service.files().create(fileMetadata, mediaContent)
                .setFields("id,parents,description")
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
                .setQ(String.format("name = '%s' and mimeType = 'application/vnd.google-apps.folder' and trashed=false",folderName))
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

    public List<File> getSubFolders(String folderID,Drive service) throws IOException{
        FileList result = null;
        List<File> subfolders = null;
        result = service.files().list()
                .setQ(String.format("'%s' in parents and mimeType = 'application/vnd.google-apps.folder'", folderID))
                .setFields("nextPageToken, files(id, name,createdTime,owners)")
                .execute();
        subfolders = result.getFiles();
        return subfolders;
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

    public File createFolder(Drive service, String folderName,String parentId,String description) throws IOException{
        String pageToken = null;
        File fileMetadata = new File();

        fileMetadata.setName(folderName);
        fileMetadata.setParents(Collections.singletonList(parentId));
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        fileMetadata.setDescription(description);
        List<File> files = service.files().list()
                .setQ(String.format("name = '%s' and '%s' in parents and trashed=false",folderName,parentId))
                .setFields("nextPageToken,files(id,name,description)")
                .setPageToken(pageToken)
                .execute()
                .getFiles();
        File createdFolder;
        if(files.isEmpty()){
            createdFolder = service.files().create(fileMetadata)
                    .setFields("id,parents")
                    .execute();
        }else{
            createdFolder = files.get(0);
        }
        return createdFolder;
    }

    public List<File> getFilesFromFolder(String folderID, Drive service) throws IOException {
        FileList result = null;
        result = service.files().list()
                .setQ(String.format("'%s' in parents and mimeType != 'application/vnd.google-apps.folder'", folderID))
                .setFields("nextPageToken, files(id, name,createdTime,owners,parents,description)")
                .execute();
        return result.getFiles();
    }
}
