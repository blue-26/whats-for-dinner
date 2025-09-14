package com.brw.demo;

import com.brw.demo.model.DayTheme;
import com.brw.demo.repository.DayThemeRepository;
import com.brw.demo.service.MealService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DataInitializer implements CommandLineRunner {

    private final DayThemeRepository dayThemeRepository;
    private final MealService mealService;

    public DataInitializer(DayThemeRepository dayThemeRepository, MealService mealService) {
        this.dayThemeRepository = dayThemeRepository;
        this.mealService = mealService;
    }

    @Override
    public void run(String... args) throws Exception {
        createThemeAndMeals("Monday", "Breakfast Bonanza", List.of(
                Map.of("name", "Pancakes", "ingredients", Map.of("Flour", "2 cups", "Eggs", "2", "Milk", "1.5 cups"), "instructions", "Mix ingredients and cook on a griddle."),
                Map.of("name", "Omelette", "ingredients", Map.of("Eggs", "3", "Cheese", "1/2 cup", "Veggies", "1 cup"), "instructions", "Whisk eggs, pour into pan, add fillings, and fold."),
                Map.of("name", "Breakfast Burrito", "ingredients", Map.of("Tortilla", "1", "Eggs", "2 scrambled", "Sausage", "2 oz"), "instructions", "Fill tortilla with scrambled eggs and sausage, then roll.")
        ));

        createThemeAndMeals("Tuesday", "Taco Tuesday", List.of(
                Map.of("name", "Beef Tacos", "ingredients", Map.of("Ground Beef", "1 lb", "Taco Shells", "12", "Lettuce", "1 cup"), "instructions", "Cook beef, season, and serve in taco shells with toppings."),
                Map.of("name", "Chicken Tacos", "ingredients", Map.of("Chicken Breast", "1 lb", "Tortillas", "12", "Salsa", "1 cup"), "instructions", "Grill chicken, shred, and serve in tortillas with salsa."),
                Map.of("name", "Fish Tacos", "ingredients", Map.of("Tilapia", "1 lb", "Cabbage Slaw", "2 cups", "Lime", "1"), "instructions", "Bake fish, flake, and serve in tortillas with slaw and a squeeze of lime.")
        ));

        createThemeAndMeals("Wednesday", "Pasta Paradise", List.of(
                Map.of("name", "Spaghetti Bolognese", "ingredients", Map.of("Ground Beef", "1 lb", "Spaghetti", "1 lb", "Tomato Sauce", "24 oz"), "instructions", "Cook beef, add sauce, and serve over cooked spaghetti."),
                Map.of("name", "Chicken Alfredo", "ingredients", Map.of("Fettuccine", "1 lb", "Chicken", "1 lb", "Alfredo Sauce", "16 oz"), "instructions", "Cook pasta and chicken, then combine with Alfredo sauce."),
                Map.of("name", "Pesto Pasta", "ingredients", Map.of("Penne", "1 lb", "Pesto", "1 cup", "Cherry Tomatoes", "1 pint"), "instructions", "Cook pasta, toss with pesto and halved cherry tomatoes.")
        ));

        createThemeAndMeals("Thursday", "Hearty Homestyle", List.of(
                Map.of("name", "Steak and Baked Potato", "ingredients", Map.of("Ribeye Steak", "8 oz", "Potato", "1 large", "Sour Cream", "2 tbsp"), "instructions", "Grill steak to desired doneness. Bake potato and serve with toppings."),
                Map.of("name", "Shepherd's Pie", "ingredients", Map.of("Ground Lamb", "1 lb", "Mashed Potatoes", "3 cups", "Mixed Veggies", "2 cups"), "instructions", "Cook lamb and veggies, top with mashed potatoes, and bake until golden."),
                Map.of("name", "Pot Roast", "ingredients", Map.of("Beef Chuck Roast", "3 lbs", "Potatoes", "2 lbs", "Carrots", "1 lb"), "instructions", "Sear roast, then slow cook with potatoes and carrots for several hours.")
        ));

        createThemeAndMeals("Friday", "Fun Friday", List.of(
                Map.of("name", "Burgers and Fries", "ingredients", Map.of("Ground Beef", "1 lb", "Buns", "4", "Potatoes", "2 lbs"), "instructions", "Form patties and grill. Cut potatoes and fry or bake. Serve on buns."),
                Map.of("name", "Loaded Nachos", "ingredients", Map.of("Tortilla Chips", "1 bag", "Cheese Sauce", "1 cup", "Ground Beef", "1 lb"), "instructions", "Layer chips, beef, and cheese. Bake until bubbly and serve with toppings."),
                Map.of("name", "Chicken Wings", "ingredients", Map.of("Chicken Wings", "2 lbs", "BBQ Sauce", "1 cup", "Ranch Dressing", "1/2 cup"), "instructions", "Bake or fry wings until crispy, toss in sauce, and serve with ranch.")
        ));

        createThemeAndMeals("Saturday", "Italian Night", List.of(
                Map.of("name", "Pepperoni Pizza", "ingredients", Map.of("Pizza Dough", "1", "Marinara", "1 cup", "Pepperoni", "4 oz"), "instructions", "Roll out dough, add sauce, cheese, and pepperoni. Bake at 450°F for 12-15 minutes."),
                Map.of("name", "Lasagna", "ingredients", Map.of("Lasagna Noodles", "9", "Ricotta Cheese", "2 cups", "Ground Beef", "1 lb"), "instructions", "Layer noodles, cheese, and meat sauce. Bake for 45 minutes."),
                Map.of("name", "Shrimp Scampi", "ingredients", Map.of("Linguine", "1 lb", "Shrimp", "1 lb", "Garlic", "4 cloves"), "instructions", "Sauté shrimp and garlic in butter, toss with cooked linguine.")
        ));

        createThemeAndMeals("Sunday", "Backyard BBQ", List.of(
                Map.of("name", "BBQ Ribs", "ingredients", Map.of("Pork Ribs", "1 rack", "BBQ Sauce", "2 cups", "Corn on the Cob", "4 ears"), "instructions", "Slow cook ribs, then grill with BBQ sauce. Serve with grilled corn."),
                Map.of("name", "Pulled Pork Sandwiches", "ingredients", Map.of("Pork Shoulder", "4 lbs", "Buns", "8", "Coleslaw", "2 cups"), "instructions", "Slow cook pork until tender, shred, and serve on buns with coleslaw."),
                Map.of("name", "Grilled Chicken", "ingredients", Map.of("Chicken Thighs", "8", "BBQ Rub", "1/4 cup", "Potato Salad", "2 cups"), "instructions", "Apply rub to chicken and grill until cooked through. Serve with potato salad.")
        ));
    }

    private void createThemeAndMeals(String dayName, String themeDescription, List<Map<String, Object>> mealsData) {
        DayTheme theme = dayThemeRepository.findByName(dayName)
                .orElseGet(() -> dayThemeRepository.save(new DayTheme(dayName, themeDescription)));

        mealsData.forEach(mealData -> {
            String mealName = (String) mealData.get("name");
            if (mealService.findByName(mealName).isEmpty()) {
                @SuppressWarnings("unchecked")
                Map<String, String> ingredients = (Map<String, String>) mealData.get("ingredients");
                String instructions = (String) mealData.get("instructions");
                mealService.createMeal(mealName, ingredients, instructions, theme);
            }
        });
    }
}
