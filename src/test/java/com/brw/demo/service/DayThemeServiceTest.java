package com.brw.demo.service;

import com.brw.demo.model.DayTheme;
import com.brw.demo.repository.DayThemeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DayThemeServiceTest {
    @Mock
    private DayThemeRepository dayThemeRepository;

    @InjectMocks
    private DayThemeService dayThemeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_returnsAllThemes() {
        DayTheme monday = new DayTheme("Monday", "Breakfast Bonanza");
        DayTheme tuesday = new DayTheme("Tuesday", "Taco Tuesday");
        when(dayThemeRepository.findAll()).thenReturn(Arrays.asList(monday, tuesday));
        List<DayTheme> result = dayThemeService.findAll();
        assertEquals(2, result.size());
        assertEquals("Monday", result.get(0).getName());
        assertEquals("Taco Tuesday", result.get(1).getThemeDescription());
    }

    @Test
    void findByName_returnsTheme() {
        DayTheme monday = new DayTheme("Monday", "Breakfast Bonanza");
        when(dayThemeRepository.findByName("Monday")).thenReturn(Optional.of(monday));
        Optional<DayTheme> result = dayThemeService.findByName("Monday");
        assertTrue(result.isPresent());
        assertEquals("Breakfast Bonanza", result.get().getThemeDescription());
    }

    @Test
    void findByName_returnsEmptyIfNotFound() {
        when(dayThemeRepository.findByName("Nonexistent")).thenReturn(Optional.empty());
        Optional<DayTheme> result = dayThemeService.findByName("Nonexistent");
        assertFalse(result.isPresent());
    }
}
