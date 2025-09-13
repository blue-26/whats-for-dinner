package com.brw.demo.service;

import com.brw.demo.model.Ingredient;
import com.brw.demo.model.Instruction;
import com.brw.demo.model.Meal;
import com.brw.demo.repository.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MealService {

    private final MealRepository mealRepository;

    @Autowired
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
    public Meal createSpaghettiMeal() {
        // 1. Create the main Meal object
        Meal spaghetti = new Meal();
        spaghetti.setName("Spaghetti");

        // 2. Create the list of Ingredients
        List<Ingredient> ingredients = new ArrayList<>();

        Ingredient pasta = new Ingredient();
        pasta.setName("Pasta");
        pasta.setQuantity("1 lb");
        pasta.setMeal(spaghetti); // Link ingredient to the meal
        ingredients.add(pasta);

        Ingredient sauce = new Ingredient();
        sauce.setName("Sauce");
        sauce.setQuantity("24 oz");
        sauce.setMeal(spaghetti); // Link ingredient to the meal
        ingredients.add(sauce);

        Ingredient parmesan = new Ingredient();
        parmesan.setName("Parmesan");
        parmesan.setQuantity("1/2 cup, grated");
        parmesan.setMeal(spaghetti); // Link ingredient to the meal
        ingredients.add(parmesan);

        // 3. Create the list of Instructions
        List<Instruction> instructions = new ArrayList<>();

        Instruction step1 = new Instruction();
        step1.setStep("1. Cook pasta");
        step1.setMeal(spaghetti); // Link instruction to the meal
        instructions.add(step1);

        Instruction step2 = new Instruction();
        step2.setStep("2. Heat sauce");
        step2.setMeal(spaghetti); // Link instruction to the meal
        instructions.add(step2);

        Instruction step3 = new Instruction();
        step3.setStep("3. Serve with parmesan");
        step3.setMeal(spaghetti); // Link instruction to the meal
        instructions.add(step3);

        // 4. Set the lists on the Meal object
        spaghetti.setIngredients(ingredients);
        spaghetti.setInstructions(instructions);

        // 5. Save the Meal object and cascade to ingredients and instructions
        return mealRepository.save(spaghetti);
    }
}
