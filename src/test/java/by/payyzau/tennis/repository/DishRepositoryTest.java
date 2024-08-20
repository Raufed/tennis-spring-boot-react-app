package by.payyzau.tennis.repository;

import by.payyzau.tennis.entity.Dish;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class DishRepositoryTest {

    @Autowired
    private DishRepository dishRepository;

    private Dish testDish;
    @BeforeEach
    void setUp() {
        System.out.println("---SET_UP---");

        testDish = new Dish();

        testDish.setName("Котлета");
        testDish.setPrice(5);
        testDish.setImageId("sjT63-sjYY72-22F");
        testDish.setImageUrl("https://google.com");
        dishRepository.save(testDish);
    }

    @AfterEach
    void tearDown() {
        dishRepository.delete(testDish);
        System.out.println("---TEAR_DOWN---");
    }
    @Test
    void givenDish_SavedWhen_CanFoundById() {

        Dish savedDish = dishRepository.findById(testDish.getId()).orElse(null);

        Assertions.assertNotNull(savedDish);

        Assertions.assertEquals(testDish.getName(), savedDish.getName());
        Assertions.assertEquals(testDish.getPrice(), savedDish.getPrice());
        Assertions.assertEquals(testDish.getImageId(), savedDish.getImageId());
        Assertions.assertEquals(testDish.getImageUrl(), savedDish.getImageUrl());
    }
    @Test
    void givenDish_SavedWhen_CanFoundByName() {
        Dish savedDish = dishRepository.findByName(testDish.getName());

        Assertions.assertNotNull(savedDish);

        Assertions.assertEquals(testDish.getId(), savedDish.getId());
        Assertions.assertEquals(testDish.getPrice(), savedDish.getPrice());
        Assertions.assertEquals(testDish.getImageId(), savedDish.getImageId());
        Assertions.assertEquals(testDish.getImageUrl(), savedDish.getImageUrl());
    }

}