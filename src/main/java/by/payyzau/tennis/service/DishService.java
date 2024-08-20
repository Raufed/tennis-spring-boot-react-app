package by.payyzau.tennis.service;

import by.payyzau.tennis.entity.Dish;
import by.payyzau.tennis.exception.NotFoundException;
import by.payyzau.tennis.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public class DishService {
    @Autowired
    private DishRepository dishRepository;
    @Autowired
    private DowloadService dowloadService;
    public Dish getDishById(Long id) {
        Dish dish = dishRepository.findById(id).orElseThrow(() -> new NotFoundException("Dish not found!"));
        return dish;
    }
    public List<Dish> allDish () {
        return dishRepository.findAll();
    }
    public Dish saveDish(Dish dish) {
        return dishRepository.save(dish);
    }
    public void deleteDish(Long id) {
        dishRepository.deleteById(id);
    }

    public Dish updateDish(Long id, Dish dish) {
        Dish currentDish = dishRepository.findById(id).orElseThrow(() -> new NotFoundException("Dish not found!"));
        currentDish.setName(dish.getName());
        currentDish.setPrice(dish.getPrice());
        currentDish = dishRepository.save(currentDish);

        return  currentDish;
    }

    public Dish getByImageId(String imageId) {
        Dish currentDish = dishRepository.getDishByImageId(imageId).orElse(null);
        return currentDish;
    }
    public void showImage(Dish dish) throws GeneralSecurityException, IOException {
        dowloadService.downloadImageFromDrive(dish.getImageUrl());
    }
}
