package com.brw.demo.service;

import com.brw.demo.model.DayTheme;
import com.brw.demo.repository.DayThemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DayThemeService {

    private final DayThemeRepository dayThemeRepository;

    @Autowired
    public DayThemeService(DayThemeRepository dayThemeRepository) {
        this.dayThemeRepository = dayThemeRepository;
    }

    public Optional<DayTheme> findByName(String name) {
        return dayThemeRepository.findByName(name);
    }
}
