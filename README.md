# Smart Pantry Manager
## Description

Smart Pantry Manager is an Android application written in Java. It helps a user reduce food waste by keeping track of the ingredients they already have at home and suggesting recipes that can be cooked with those ingredients only.
A recipe is only suggested when every ingredient it needs is in the pantry, with the required quantity. No shopping trip is needed for any recipe on the suggestions list.

## Features

- Add, edit and delete pantry items. Each item has a name, quantity, unit and an optional expiry date.
- The pantry list is displayed in a RecyclerView and is loaded from the database.
- 20 South African recipes are added to the database the first time the app runs.
- The Suggested Recipes screen lists only the recipes that can be made with the current pantry.
- A separate "Almost there" list shows recipes that are missing exactly one ingredient, it indicates that ingredient.
- The recipe detail screen shows the full ingredient list and the method.
- A message is displayed when no recipes match the pantry.
- Items that have expired are shown in red, and items that expire within 3 days are shown in amber.
- The Settings screen allows the user to switch the expiry alerts on or off and to choose between Metric and Imperial units.
- A bottom navigation bar is available on every screen.
- All input on the Add/Edit Ingredient screen is validated before it is saved.

## Screens

| Screen | Purpose |
|---|---|
| Pantry | Lists all pantry items, with Edit and Delete buttons for each item |
Add/Edit Ingredient | Screen for creating a new item or changing an existing item
Suggested Recipes | Recipes that pass the strict-matching rule, followed by the "Almost there" list
Recipe Detail | Ingredients and method for the selected recipe
Settings | Expiry alerts switch and preferred unit system

## Strict-matching rule

The matching logic is in `services/RecipeMatcher.java`. A recipe is suggested only if every required ingredient passes all three checks below. If one ingredient fails, the recipe is not suggested.

1. **Name.** The ingredient names are compared after they are changed to lower case and common plural endings are removed. "Tomatoes" in the pantry therefore matches "tomato" in a recipe.
2. **Unit.** The units must measure the same thing. Weight units (g, oz, lb) match each other, and volume units (ml, teaspoon, tablespoon, cups) match each other. Other units, such as whole and slices, only match themselves.
3. **Quantity.** Both quantities are converted to grams or millilitres and then compared. The pantry quantity must be equal to or more than the quantity the recipe needs. For example, 2 lb of boerewors is enough for a recipe that needs 500 g.

A recipe that fails because of exactly one ingredient is shown in the "Almost there" list. This list is kept separate from the main suggestions.

## Database

The app uses **SQLite**, implemented with `SQLiteOpenHelper`.

### Why SQLite was chosen

- The pantry belongs to one user on one device, so the data does not need to be stored on a server or shared between devices.
- The app works without an internet connection.
- The data is relational. One recipe has many ingredients, which is stored in two linked tables.
- SQLite is built into Android, so no account, hosting or extra configuration is needed to run the app.

### Tables

| Table | Columns |
| --- | --- |
`pantry` | id, name, quantity, unit, expiryDate
`recipes` | id, name, instructions
`recipe_ingredients` | id, recipeId, ingredientName, requiredQuantity, unit

`recipeId` in `recipe_ingredients` links each ingredient to its recipe in the `recipes` table.
The pantry supports full CRUD: items can be created, viewed, updated and deleted, and the data is still available after the app is closed and opened again.
The two settings are stored with SharedPreferences, because they are simple values and not records.

## Project structure

app/src/main/java/com/example/smartpantrymanager/
    MainActivity.java                Creates the database and opens the pantry screen
    PantryActivity.java              Pantry list
    AddEditIngredientActivity.java   Add or edit a pantry item
    SuggestedRecipesActivity.java    Suggested recipes and "Almost there" recipes
    RecipeDetailActivity.java        Ingredients and method of one recipe
    SettingsActivity.java            Expiry alerts and unit system
    NavigationHelper.java            Bottom navigation bar used by every screen
    adapters/
        PantryAdapter.java           Displays pantry items in the RecyclerView
        RecipeAdapter.java           Displays recipes in the RecyclerView
    database/
        DatabaseHelper.java          Creates the SQLite tables
        PantryDAO.java               CRUD methods for pantry items
        RecipeDAO.java               Reads recipes and their ingredients
        RecipeSeeder.java            Adds the 20 recipes on first run
    models/
        PantryItem.java
        Recipe.java
        RecipeIngredient.java
    services/
        RecipeMatcher.java           Strict-matching logic

## Setup and run instructions

### Requirements

- Android Studio (a recent version)
- An Android emulator or a physical device with Android 7.0 (API 24) or newer

### Steps

1. Clone the repository: git clone https://github.com/THERON-SMITE/SmartPantryManager.git
2. Open Android Studio, select **File > Open** and choose the `SmartPantryManager` folder.
3. Wait for the Gradle sync to finish.
4. Select an emulator or connect a device.
5. Click **Run**.

No API keys, accounts or internet connection are needed. The database is created and the recipes are added automatically the first time the app is opened.

## How to test the strict-matching rule

1. On the Pantry screen, add `boerewors` with a quantity of 500 and the unit g.
2. Open **Recipes**. "Boerewors and Pap" is not suggested, but it is shown in the "Almost there" list with maize meal as the missing ingredient.
3. Go back to the Pantry and add `maize meal` with a quantity of 250 and the unit g.
4. Open **Recipes** again. "Boerewors and Pap" is now listed under Suggested Recipes.
5. Delete `maize meal` from the pantry, or change its quantity to less than 250. The recipe is removed from the suggestions.