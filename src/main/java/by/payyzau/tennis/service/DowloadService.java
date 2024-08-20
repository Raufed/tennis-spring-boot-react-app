package by.payyzau.tennis.service;

import by.payyzau.tennis.creds.GoogleCred;
import com.fasterxml.jackson.core.JsonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.SecurityUtils;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.util.Collections;

//import static by.payyzau.tennis.service.UploadService.getPathToGoogleCredentials;
@Service
public class DowloadService {
    private static final String APPLICATION_NAME = "Tennis";
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    //private static final String SERVICE_ACOUNT_KEY_PATH = getPathToGoogleCredentials();
    //private static final String SERVICE_ACOUNT_KEY_PATH2 = System.getenv("${GOOGLE_CRED}");
    String ANSI_RESET = "\u001B[0m";
    String ANSI_RED = "\u001B[31m";
    String ANSI_GREEN = "\u001B[32m";

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
    public String downloadImageFromDrive(String fileId) throws GeneralSecurityException, IOException {
        try {
            String currentDirectory = System.getProperty("user.dir");
            Drive drive = cteareDriveService();

            File file = drive.files().get(fileId).execute();
            String filePathString = currentDirectory + "/frontend/public/images/" + file.getName();
            if(Files.exists(Path.of(filePathString)) == true) {
                System.out.println(ANSI_RED + "файл уже загружен" + ANSI_RESET);
                System.out.println(filePathString);
                System.out.println(file.getName());
                return file.getName();
            } else {
                System.out.println(ANSI_GREEN + "Загрузка файла..." + ANSI_RESET);
            }
            java.io.File fileInSystem = new java.io.File(filePathString);

            OutputStream outputStream = new FileOutputStream(filePathString);

            System.out.println(ANSI_GREEN + fileInSystem.getPath() + ANSI_RESET);

            drive.files().get(fileId).executeMediaAndDownloadTo(outputStream);
            outputStream.close();

            System.out.println("Image downloaded successfully.");
            return file.getName();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
