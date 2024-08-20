package by.payyzau.tennis.service;

import by.payyzau.tennis.entity.Res;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class UploadService {

    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String SERVICE_ACOUNT_KEY_PATH = getPathToGoogleCredentials();

    private static String getPathToGoogleCredentials() {
        String currentDirectory = System.getProperty("user.dir");
        Path filePath = Paths.get(currentDirectory, "cred.json");
        return filePath.toString();
    }
    public Res uploadImageToDrive(File file) {
        Res res = new Res();
        System.out.println("Uploaded file name = " + file.getName());
        try {

            BufferedImage sourceImage = ImageIO.read(file);
            ImageIO.write(sourceImage, "jpg", file);

            String folderId = "1pD6oITX2CN0reKG9185gHJ6LFOp3pPRD";
            Drive drive = cteareDriveService();

            com.google.api.services.drive.model.File fileMetaData = new com.google.api.services.drive.model.File();
            fileMetaData.setName(file.getName());
            fileMetaData.setParents(Collections.singletonList(folderId));
            FileContent mediaContent = new FileContent("image/jpg", file);
            com.google.api.services.drive.model.File uploadedFile = drive.files().create(fileMetaData, mediaContent)
                    .setFields("id").execute();
            String imageUrl = "https://drive.google.com/file/d/"+uploadedFile.getId()+"/view?usp=drive_link";
            System.out.println("IMAGE URL: " + imageUrl);
            file.delete();
            res.setStatus(200);
            res.setMessage("Image Seccessfully Uploaded To Drive");
            res.setUrl(imageUrl);
        }catch (Exception e) {
            System.out.println(e.getMessage());
            res.setStatus(500);
            res.setMessage(e.getMessage());
        }
        return  res;
    }

    private Drive cteareDriveService() throws GeneralSecurityException, IOException {
        InputStream in = new ByteArrayInputStream(System.getenv("GOOGLE_CRED").getBytes());
        //GoogleCredential credentials = GoogleCredential.fromStream(new FileInputStream(SERVICE_ACOUNT_KEY_PATH))
        GoogleCredential credentials2 = GoogleCredential.fromStream(in)
                .createScoped(Collections.singleton(DriveScopes.DRIVE));
        return new Drive.Builder(
                 GoogleNetHttpTransport.newTrustedTransport(),
                 JSON_FACTORY,
                 credentials2).build();
    }

    public String deleteFile(String fileId) throws GeneralSecurityException, IOException {
        try {
            InputStream in = new ByteArrayInputStream(System.getenv("GOOGLE_CRED").getBytes());
            //GoogleCredential credentials = GoogleCredential.fromStream(new FileInputStream(SERVICE_ACOUNT_KEY_PATH))
            GoogleCredential credentials2 = GoogleCredential.fromStream(in)
                    .createScoped(Collections.singleton(DriveScopes.DRIVE));
            Drive drive = new Drive.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JSON_FACTORY,
                    credentials2).build();
            drive.files().delete(extractFileIdFromUrl(fileId)).execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }

        return "File with id = " + fileId + " was deleted!";
    }
    private String extractFileIdFromUrl(String url) {
        String fileId = "";
        String[] parts = url.split("/d/");

        if (parts.length > 1) {
            String[] fileIdParts = parts[1].split("/");
            fileId = fileIdParts[0];
        }

        return fileId;
    }


}
