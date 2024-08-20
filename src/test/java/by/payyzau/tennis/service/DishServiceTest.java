package by.payyzau.tennis.service;

import by.payyzau.tennis.entity.Dish;
import by.payyzau.tennis.repository.DishRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class DishServiceTest {
    @InjectMocks
    private  DishService dishService;
    @Mock
    private DishRepository dishRepository;
    private List<Dish> dishes;

    @BeforeEach
    void setUp() {
        System.out.println("---SET UP---");

        Mockito.when(dishRepository.findAll()).thenReturn(dishes);
        dishService.allDish();
    }

    @AfterEach
    void tearDown() {
        System.out.println("---TEAR DOWN---");
    }

    @Test
    void saveDish() {
        Dish dish = new Dish(18L,"Котлета", 5.0,"jj7-fHrED-y2","https://google.com");
        Mockito.when(dishRepository.save(any(Dish.class))).thenReturn(dish);

        Dish savedDish = dishService.saveDish(dish);

        assertThat(savedDish).isNotNull();
        assertThat(savedDish).isEqualTo(savedDish);
    }

    @Test
    void allDish() {
        List<Dish> dishList = dishService.allDish();

        System.out.println(dishList);

        assertThat(dishList).isEqualTo(dishes);
        assertThat(dishList).isNull();
    }

    @Test
    void getDishById() {
        Dish dish2 = new Dish(19L,"Отбивная куриная", 7.0,"gQrt-56Jj-ib","https://youtube.com");
        Mockito.when(dishRepository.findById(19L)).thenReturn(Optional.ofNullable(dish2));

        Dish savedDish = dishService.getDishById(19L);

        assertThat(savedDish).isEqualTo(dish2);
        assertThat(savedDish).isNotNull();
        //Mockito.when(dishRepository.findById(Mockito.any())).thenReturn(Optional.of(D));
    }

    @Test
    void deleteDish() {
        long dishId = 19L;
        Dish dish2 = new Dish(19L,"Отбивная куриная", 7.0,"gQrt-56Jj-ib","https://youtube.com");

        Mockito.doNothing().when(dishRepository).deleteById(dishId);


        assertAll(() -> dishService.deleteDish(dishId));

    }

    @Test
    void updateDish() {

        Dish dish2 = new Dish(19L,"Отбивная куриная", 7.0,"gQrt-56Jj-ib","https://youtube.com");
        Mockito.when(dishRepository.findById(19L)).thenReturn(Optional.ofNullable(dish2));
        Mockito.when(dishRepository.save(dish2)).thenReturn(dish2);

        Dish updateDish = dishService.updateDish(19L, dish2);

        assertThat(updateDish).isNotNull();

    }

    @Test
    void getByImageId() {
        Dish dish2 = new Dish(19L,"Отбивная куриная", 7.0,"gQrt-56Jj-ib","https://youtube.com");
        Mockito.when(dishRepository.getDishByImageId(dish2.getImageId())).thenReturn(Optional.ofNullable(dish2));

        Dish savedDish = dishService.getByImageId(dish2.getImageId());

        assertThat(savedDish).isEqualTo(dish2);
        assertThat(savedDish).isNotNull();

    }
}