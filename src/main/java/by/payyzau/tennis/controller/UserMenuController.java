package by.payyzau.tennis.controller;

import by.payyzau.tennis.entity.Dish;
import by.payyzau.tennis.service.DishService;
import by.payyzau.tennis.service.DowloadService;
import by.payyzau.tennis.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api/v1/tennismenu")
public class UserMenuController {
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
    public ResponseEntity<Dish> getDishByID(@PathVariable("id") Long id) throws URISyntaxException {
        Dish dishById = dishService.getDishById(id);
        URI location = new URI("api/v1/menu/" + dishById.getId());
        return ResponseEntity.ok().location(location).body(dishById);
    }
}
