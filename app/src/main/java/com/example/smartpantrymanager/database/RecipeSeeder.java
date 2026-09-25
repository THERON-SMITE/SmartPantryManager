package com.example.smartpantrymanager.database;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;

public class RecipeSeeder {

    public static void seedRecipes(SQLiteDatabase db) {

        // Check whether recipes have already been added
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM recipes",
                null
        );

        cursor.moveToFirst();

        int recipeCount = cursor.getInt(0);

        cursor.close();

        // Do not add duplicate recipes
        if (recipeCount > 0) {
            return;
        }

        // Add recipes to the database
        addRecipe(
                db,
                "Bobotie",
                "Fry the mince with onion and curry spices. Add chutney " +
                        "and bread. Place in a baking dish. Mix eggs and milk " +
                        "and pour over the mince. Bake until the topping is set."
        );

        addRecipe(
                db,
                "Milk Tart",
                "Prepare the pastry base. Heat milk with sugar and butter. " +
                        "Mix flour and eggs separately. Combine with the warm " +
                        "milk mixture and cook until thick. Pour into the base " +
                        "and sprinkle with cinnamon."
        );

        addRecipe(
                db,
                "Malva Pudding",
                "Mix flour, sugar and egg. Add milk and apricot jam. " +
                        "Bake until golden. Pour the warm butter and cream " +
                        "sauce over the pudding before serving."
        );

        addRecipe(
                db,
                "Bunny Chow",
                "Cook the curry with onion, potato and tomato. " +
                        "Cut the centre from a bread loaf and fill it with " +
                        "the curry. Serve warm."
        );

        addRecipe(
                db,
                "Boerewors and Pap",
                "Cook the boerewors until browned and cooked through. " +
                        "Prepare the maize meal pap and serve together."
        );

        addRecipe(
                db,
                "Chicken Curry",
                "Fry the onion with curry spices. Add chicken and tomato. " +
                        "Add potato and cook until the chicken and potato " +
                        "are fully cooked."
        );

        addRecipe(
                db,
                "Beef Curry",
                "Fry the onion with curry spices. Add the beef and tomato. " +
                        "Cook slowly until the beef is tender."
        );

        addRecipe(
                db,
                "Vetkoek with Mince",
                "Prepare and fry the vetkoek. Cook the mince with onion " +
                        "and tomato. Cut the vetkoek open and fill with mince."
        );

        addRecipe(
                db,
                "Chicken Potjie",
                "Brown the chicken. Add onion, potato, carrot and tomato. " +
                        "Cook slowly in a pot until all ingredients are tender."
        );

        addRecipe(
                db,
                "Beef Potjie",
                "Brown the beef. Add onion, potato, carrot and tomato. " +
                        "Cook slowly until the beef and vegetables are tender."
        );

        addRecipe(
                db,
                "Samp and Beans",
                "Soak the samp and beans. Cook with onion and tomato " +
                        "until soft. Season and serve."
        );

        addRecipe(
                db,
                "Chakalaka and Pap",
                "Fry onion, carrot and pepper. Add tomato and spices " +
                        "and cook until the vegetables are tender. Serve with pap."
        );

        addRecipe(
                db,
                "Tomato Bredie",
                "Brown the beef. Add onion, tomato and potato. " +
                        "Cook slowly until the meat and vegetables are tender."
        );

        addRecipe(
                db,
                "Waterblommetjie Stew",
                "Cook the beef with onion and potato. Add cleaned " +
                        "waterblommetjies and cook until tender."
        );

        addRecipe(
                db,
                "Cape Malay Chicken",
                "Fry onion with curry spices. Add chicken, potato, " +
                        "tomato and apricot jam. Cook until the chicken is done."
        );

        addRecipe(
                db,
                "Butternut Soup",
                "Cook butternut, onion and potato in stock until soft. " +
                        "Blend until smooth and add cream before serving."
        );

        addRecipe(
                db,
                "Vetkoek",
                "Prepare the dough using flour, yeast, sugar and water. " +
                        "Allow the dough to rise. Shape and deep fry until golden."
        );

        addRecipe(
                db,
                "Koeksisters",
                "Prepare the dough using flour, sugar, egg and butter. " +
                        "Shape and fry until golden. Dip the koeksisters " +
                        "in prepared syrup."
        );

        addRecipe(
                db,
                "Peppermint Crisp Tart",
                "Mix condensed milk and cream. Fold in crushed peppermint " +
                        "chocolate. Layer with biscuits and chill before serving."
        );

        addRecipe(
                db,
                "Braaivleis with Pap and Chakalaka",
                "Cook the meat over the braai. Prepare pap and chakalaka " +
                        "using maize meal, tomato, onion, carrot and pepper. " +
                        "Serve together."
        );
    }

    private static void addRecipe(
            SQLiteDatabase db,
            String name,
            String instructions) {

        // Store the recipe details
        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("instructions", instructions);

        long recipeId = db.insert(
                DatabaseHelper.TABLE_RECIPES,
                null,
                values
        );

        // Add the ingredients belonging to the recipe
        addIngredientsForRecipe(db, recipeId, name);
    }

    private static void addIngredientsForRecipe(
            SQLiteDatabase db,
            long recipeId,
            String recipeName) {

        switch (recipeName) {

            case "Bobotie":
                addIngredient(db, recipeId, "mince", 500, "g");
                addIngredient(db, recipeId, "onion", 1, "whole");
                addIngredient(db, recipeId, "egg", 2, "whole");
                addIngredient(db, recipeId, "milk", 250, "ml");
                addIngredient(db, recipeId, "bread", 2, "slices");
                addIngredient(db, recipeId, "chutney", 50, "g");
                addIngredient(db, recipeId, "curry powder", 10, "g");
                break;

            case "Milk Tart":
                addIngredient(db, recipeId, "milk", 1000, "ml");
                addIngredient(db, recipeId, "flour", 150, "g");
                addIngredient(db, recipeId, "sugar", 200, "g");
                addIngredient(db, recipeId, "egg", 2, "whole");
                addIngredient(db, recipeId, "butter", 50, "g");
                addIngredient(db, recipeId, "cinnamon", 5, "g");
                break;

            case "Malva Pudding":
                addIngredient(db, recipeId, "flour", 150, "g");
                addIngredient(db, recipeId, "sugar", 150, "g");
                addIngredient(db, recipeId, "egg", 2, "whole");
                addIngredient(db, recipeId, "milk", 250, "ml");
                addIngredient(db, recipeId, "apricot jam", 50, "g");
                addIngredient(db, recipeId, "butter", 100, "g");
                addIngredient(db, recipeId, "cream", 250, "ml");
                break;

            case "Bunny Chow":
                addIngredient(db, recipeId, "bread", 1, "whole");
                addIngredient(db, recipeId, "beef", 500, "g");
                addIngredient(db, recipeId, "onion", 1, "whole");
                addIngredient(db, recipeId, "potato", 2, "whole");
                addIngredient(db, recipeId, "tomato", 2, "whole");
                addIngredient(db, recipeId, "curry powder", 10, "g");
                break;

            case "Boerewors and Pap":
                addIngredient(db, recipeId, "boerewors", 500, "g");
                addIngredient(db, recipeId, "maize meal", 250, "g");
                break;

            case "Chicken Curry":
                addIngredient(db, recipeId, "chicken", 500, "g");
                addIngredient(db, recipeId, "onion", 1, "whole");
                addIngredient(db, recipeId, "tomato", 2, "whole");
                addIngredient(db, recipeId, "potato", 2, "whole");
                addIngredient(db, recipeId, "curry powder", 15, "g");
                break;

            case "Beef Curry":
                addIngredient(db, recipeId, "beef", 500, "g");
                addIngredient(db, recipeId, "onion", 1, "whole");
                addIngredient(db, recipeId, "tomato", 2, "whole");
                addIngredient(db, recipeId, "curry powder", 15, "g");
                break;

            case "Vetkoek with Mince":
                addIngredient(db, recipeId, "vetkoek", 4, "whole");
                addIngredient(db, recipeId, "mince", 400, "g");
                addIngredient(db, recipeId, "onion", 1, "whole");
                addIngredient(db, recipeId, "tomato", 2, "whole");
                break;

            case "Chicken Potjie":
                addIngredient(db, recipeId, "chicken", 750, "g");
                addIngredient(db, recipeId, "onion", 2, "whole");
                addIngredient(db, recipeId, "potato", 4, "whole");
                addIngredient(db, recipeId, "carrot", 4, "whole");
                addIngredient(db, recipeId, "tomato", 2, "whole");
                break;

            case "Beef Potjie":
                addIngredient(db, recipeId, "beef", 750, "g");
                addIngredient(db, recipeId, "onion", 2, "whole");
                addIngredient(db, recipeId, "potato", 4, "whole");
                addIngredient(db, recipeId, "carrot", 4, "whole");
                addIngredient(db, recipeId, "tomato", 2, "whole");
                break;

            case "Samp and Beans":
                addIngredient(db, recipeId, "samp", 300, "g");
                addIngredient(db, recipeId, "beans", 200, "g");
                addIngredient(db, recipeId, "onion", 1, "whole");
                addIngredient(db, recipeId, "tomato", 2, "whole");
                break;

            case "Chakalaka and Pap":
                addIngredient(db, recipeId, "maize meal", 250, "g");
                addIngredient(db, recipeId, "carrot", 2, "whole");
                addIngredient(db, recipeId, "pepper", 1, "whole");
                addIngredient(db, recipeId, "onion", 1, "whole");
                addIngredient(db, recipeId, "tomato", 2, "whole");
                break;

            case "Tomato Bredie":
                addIngredient(db, recipeId, "beef", 500, "g");
                addIngredient(db, recipeId, "tomato", 4, "whole");
                addIngredient(db, recipeId, "onion", 1, "whole");
                addIngredient(db, recipeId, "potato", 2, "whole");
                break;

            case "Waterblommetjie Stew":
                addIngredient(db, recipeId, "beef", 500, "g");
                addIngredient(db, recipeId, "waterblommetjies", 500, "g");
                addIngredient(db, recipeId, "onion", 1, "whole");
                addIngredient(db, recipeId, "potato", 2, "whole");
                break;

            case "Cape Malay Chicken":
                addIngredient(db, recipeId, "chicken", 500, "g");
                addIngredient(db, recipeId, "onion", 1, "whole");
                addIngredient(db, recipeId, "potato", 2, "whole");
                addIngredient(db, recipeId, "tomato", 2, "whole");
                addIngredient(db, recipeId, "apricot jam", 50, "g");
                addIngredient(db, recipeId, "curry powder", 10, "g");
                break;

            case "Butternut Soup":
                addIngredient(db, recipeId, "butternut", 1, "whole");
                addIngredient(db, recipeId, "onion", 1, "whole");
                addIngredient(db, recipeId, "potato", 2, "whole");
                addIngredient(db, recipeId, "cream", 250, "ml");
                break;

            case "Vetkoek":
                addIngredient(db, recipeId, "flour", 500, "g");
                addIngredient(db, recipeId, "yeast", 10, "g");
                addIngredient(db, recipeId, "sugar", 25, "g");
                addIngredient(db, recipeId, "water", 300, "ml");
                break;

            case "Koeksisters":
                addIngredient(db, recipeId, "flour", 500, "g");
                addIngredient(db, recipeId, "sugar", 100, "g");
                addIngredient(db, recipeId, "egg", 1, "whole");
                addIngredient(db, recipeId, "butter", 50, "g");
                addIngredient(db, recipeId, "water", 250, "ml");
                break;

            case "Peppermint Crisp Tart":
                addIngredient(db, recipeId, "condensed milk", 385, "g");
                addIngredient(db, recipeId, "cream", 500, "ml");
                addIngredient(db, recipeId, "peppermint crisp", 150, "g");
                addIngredient(db, recipeId, "biscuits", 200, "g");
                break;

            case "Braaivleis with Pap and Chakalaka":
                addIngredient(db, recipeId, "beef", 500, "g");
                addIngredient(db, recipeId, "maize meal", 250, "g");
                addIngredient(db, recipeId, "tomato", 2, "whole");
                addIngredient(db, recipeId, "onion", 1, "whole");
                addIngredient(db, recipeId, "carrot", 2, "whole");
                addIngredient(db, recipeId, "pepper", 1, "whole");
                break;
        }
    }

    private static void addIngredient(
            SQLiteDatabase db,
            long recipeId,
            String ingredientName,
            double quantity,
            String unit) {

        // Store the recipe ingredient requirement
        ContentValues values = new ContentValues();

        values.put("recipeId", recipeId);
        values.put("ingredientName", ingredientName);
        values.put("requiredQuantity", quantity);
        values.put("unit", unit);

        db.insert(
                DatabaseHelper.TABLE_RECIPE_INGREDIENTS,
                null,
                values
        );
    }
}