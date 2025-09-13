package com.brw.demo.service;

import com.brw.demo.model.DayTheme;
import com.brw.demo.model.Ingredient;
import com.brw.demo.model.Meal;
import com.brw.demo.repository.MealRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MealService {

    private final MealRepository mealRepository;

    public MealService(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    public Optional<Meal> findByName(String name) {
        return mealRepository.findByName(name);
    }

    public Meal save(Meal meal) {
        return mealRepository.save(meal);
    }

    public Optional<Meal> findById(Long id) {
        return mealRepository.findById(id);
    }

    @Transactional
    public Meal createMeal(String name, Map<String, String> ingredientsData, String instructionSteps, DayTheme theme) {
        Meal meal = new Meal();
        meal.setName(name);
        meal.setDayTheme(theme);

        List<Ingredient> ingredients = ingredientsData.entrySet().stream()
                .map(entry -> {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setName(entry.getKey());
                    ingredient.setQuantity(entry.getValue());
                    ingredient.setMeal(meal);
                    return ingredient;
                })
                .collect(Collectors.toList());

        meal.setIngredients(ingredients);
        meal.setInstructions(instructionSteps);

        return mealRepository.save(meal);
    }
}


