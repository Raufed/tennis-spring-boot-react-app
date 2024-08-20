package by.payyzau.tennis.controller;

import by.payyzau.tennis.entity.Dish;
import by.payyzau.tennis.service.DishService;
import by.payyzau.tennis.service.DowloadService;
import by.payyzau.tennis.service.UploadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = MenuController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class MenuControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DishService dishService;
    @MockBean
    private UploadService uploadService;
    @MockBean
    private DowloadService dowloadService;
    private Dish dish;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public  void init() {
        dish = new Dish(19L,"Отбивная куриная", 7.0,"gQrt-56Jj-ib","https://youtube.com");

    }

    @Test
    public void MenuController_AddDish_ReturnCreated () throws Exception {
        given(dishService.saveDish(ArgumentMatchers.any())).willAnswer((invocation -> invocation.getArgument(0)));

        ResultActions response = mockMvc.perform(post("/api/v1/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dish)));

        response.andExpect(MockMvcResultMatchers.status().isCreated());
    }
}