package com.github.www.allergyapp;

import java.util.List;

/**
 * Globally accessed data class to contain the allergen map and users selected allergies.
 * Allows for ease of access instead of tedious bundling.
 */
public class GlobalAllergens {
    static AllergenMap allergenMap;
    static List<Allergen> selectedAllergies;
}
