package com.brw.demo.controller;

import com.brw.demo.service.DayThemeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.mockito.Mockito.when;

@WebMvcTest(DayThemeController.class)
class DayThemeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DayThemeService dayThemeService;
    @Test
    void getAllThemes_returnsThemes() throws Exception {
        when(dayThemeService.findAll()).thenReturn(Arrays.asList(
                new com.brw.demo.model.DayTheme("Monday", "Breakfast Bonanza"),
                new com.brw.demo.model.DayTheme("Tuesday", "Taco Tuesday")
        ));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/themes"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
