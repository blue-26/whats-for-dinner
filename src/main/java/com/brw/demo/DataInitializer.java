package com.brw.demo;

import com.brw.demo.model.DayTheme;
import com.brw.demo.model.Meal;
import com.brw.demo.repository.DayThemeRepository;
import com.brw.demo.service.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class DataInitializer implements CommandLineRunner {

    private final DayThemeRepository dayThemeRepository;
    private final MealService mealService;

    @Autowired
    public DataInitializer(DayThemeRepository dayThemeRepository, MealService mealService) {
        this.dayThemeRepository = dayThemeRepository;
        this.mealService = mealService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create DayTheme objects for each day of the week
        Stream.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
                .forEach(day -> {
                    if (dayThemeRepository.findByName(day).isEmpty()) {
                        dayThemeRepository.save(new DayTheme(day));
                    }
                });

        // Create the Spaghetti meal and associate it with Monday
        if (mealService.findByName("Spaghetti").isEmpty()) {
            Meal spaghetti = mealService.createSpaghettiMeal();
            DayTheme monday = dayThemeRepository.findByName("Monday").orElseThrow();
            spaghetti.setDayTheme(monday);
            mealService.save(spaghetti);
        }
    }
}
