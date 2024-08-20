package by.payyzau.tennis.controller;

import by.payyzau.tennis.entity.Dish;
import by.payyzau.tennis.entity.Res;
import by.payyzau.tennis.service.DishService;
import by.payyzau.tennis.service.DowloadService;
import by.payyzau.tennis.service.UploadService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api/v1/menu")
public class MenuController {
    String ANSI_RESET = "\u001B[0m";
    String ANSI_RED = "\u001B[31m";
    String ANSI_GREEN = "\u001B[32m";
    @Autowired
    private DishService dishService;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private DowloadService dowloadService;
    private String urlSaver;
    @GetMapping
    public List<Dish> getAll() throws GeneralSecurityException, IOException {
        List<Dish> dishes = dishService.allDish();
        for (Dish dish: dishes) {
            System.out.println(dish);
            if(dish.getImageUrl() != null) {
                String cleanedUrl = dish.getImageUrl().replace("https://drive.google.com/file/d/", "");
                cleanedUrl = cleanedUrl.replace("/view?usp=drive_link", "");
                dish.setImageId(dowloadService.downloadImageFromDrive(cleanedUrl));
            }
            else {
                continue;
            }
        }
        return dishService.allDish();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Dish> getDishByID(@PathVariable("id") Long id) throws URISyntaxException{
        Dish dishById = dishService.getDishById(id);
        URI location = new URI("api/v1/menu/" + dishById.getId());
        return ResponseEntity.ok().location(location).body(dishById);
    }
    @PostMapping
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addDish(@Valid @RequestBody Dish dish1) throws URISyntaxException, JsonProcessingException {
        try {
            System.out.println("---HERE---");

            Dish savedDish = dishService.saveDish(dish1);
            System.out.println(savedDish.toString());
            return ResponseEntity.created(new URI("api/v1/menu/" + savedDish.getId())).body(savedDish);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        //return ResponseEntity.badRequest().body(null);
    }
    @PostMapping("/image")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addImageDish(
            @Valid
            @RequestParam("image")
                MultipartFile file,
            @RequestParam("imageId")
                String imageId) throws IOException {
        try {
            if (file.isEmpty()) return ResponseEntity.badRequest().body("File is empty");


            String clearImageId = imageId.replace("\"", "");
            File tempFile = File.createTempFile("temp", file.getOriginalFilename());
            file.transferTo(tempFile);


            System.out.println(clearImageId);
            Dish currentDish = new Dish();
            currentDish = dishService.getByImageId(clearImageId);

            //if(currentDish == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Нет данных блюда");

            Res res = uploadService.uploadImageToDrive(tempFile);
            currentDish.setImageUrl(res.getUrl());

            System.out.println("IMAGE_ID = " + clearImageId.toString());
            String cleanedUrl = res.getUrl().replace("https://drive.google.com/file/d/", "");
            cleanedUrl = cleanedUrl.replace("/view?usp=drive_link", "");

            String fileName = dowloadService.downloadImageFromDrive(cleanedUrl);
            System.out.println(ANSI_GREEN + "fileName = " + fileName + ANSI_RESET);
            currentDish.setImageId(fileName);
            dishService.saveDish(currentDish);

            return ResponseEntity.ok().body(res.toString());
        } catch (IOException e) {
            // Обработка исключения IOException
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the file");
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Dish> updateDish(@PathVariable("id") Long id, @RequestBody Dish dish) throws URISyntaxException {
        Dish updatedDish = dishService.updateDish(id, dish);
        URI location = new URI("api/v1/menu/" + id);
        return ResponseEntity.ok().location(location).body(updatedDish);
    }
    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteDish(@PathVariable("id") Long id) throws URISyntaxException, GeneralSecurityException, IOException {

        Dish dishById = dishService.getDishById(id);
        String currentDirectory = System.getProperty("user.dir");
        String filePathString = currentDirectory + "/frontend/public/images/" + dishById.getImageId();
        System.out.println(filePathString);

        // Deleting file
        FileUtils.touch(new File(filePathString));
        File fileToDelete = FileUtils.getFile(filePathString);
        boolean success = FileUtils.deleteQuietly(fileToDelete);
        System.out.println(success);

        uploadService.deleteFile(dishById.getImageUrl());
        dishService.deleteDish(id);
        URI location = new URI("api/v1/menu/" + id);
        return ResponseEntity.ok().location(location).body("Dish with id = " + id + " was deleted!");
    }
}
