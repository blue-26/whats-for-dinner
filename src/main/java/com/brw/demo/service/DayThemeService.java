

package com.brw.demo.service;

import com.brw.demo.model.DayTheme;
import com.brw.demo.repository.DayThemeRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;

@Service
public class DayThemeService {

    private final DayThemeRepository dayThemeRepository;

    public DayThemeService(DayThemeRepository dayThemeRepository) {
        this.dayThemeRepository = dayThemeRepository;
    }

    public Optional<DayTheme> findByName(String name) {
        return dayThemeRepository.findByName(name);
    }

    public List<DayTheme> findAll() {
        return dayThemeRepository.findAll();
    }
}
