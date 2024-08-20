package by.payyzau.tennis.repository;

import by.payyzau.tennis.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    Dish findByName(String name);
    @Query("SELECT d FROM Dish d WHERE d.imageId = ?1")
    Optional<Dish> getDishByImageId(String imageId);

}
