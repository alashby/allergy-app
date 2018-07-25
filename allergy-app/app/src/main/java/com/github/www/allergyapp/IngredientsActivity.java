package com.github.www.allergyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class IngredientsActivity extends AppCompatActivity {

    private ArrayList<Allergen> allergens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        Bundle bundle = getIntent().getExtras();
        allergens = new ArrayList<Allergen>();

        // TODO: Remove commented
        ArrayList<String> texts = bundle.getStringArrayList("INGREDIENTS");
        /*for (int i = 0; i < bundle.getStringArrayList("NAMES").size(); i++) {
            Allergen allergen = new Allergen(bundle.getStringArrayList("NAMES").get(i),
                    bundle.getIntegerArrayList("LEVELS").get(i));
            allergens.add(allergen);
        }
        */

        /* For all allergens found in the global selected allergeies list, add them and their
        children based on their map data to the allergens array list.
         */
        for (int i = 0; i < GlobalAllergens.selectedAllergies.size(); i++) {
            allergens.add(GlobalAllergens.allergenMap.getAllergenByName(GlobalAllergens.selectedAllergies.get(i).get_name()));
            ArrayList<Allergen> children = GlobalAllergens.allergenMap.getAllChildAllergies(allergens.get(i));
            for (int k = 0; k < children.size(); k++) {
                if (!children.get(0).get_name().equals("no child allergies")) {
                    allergens.add(children.get(k));
                }
            }
        }

        ArrayList<String> blocks = new ArrayList();
        for (int i = 0; i < texts.size(); i++) {
            blocks.addAll(splitBlocks(texts.get(i)));
        }

        for (int i = 0; i < blocks.size(); i++) {
            assessWarning(blocks.get(i), allergens);
        }
    }


    private ArrayList<String> formatIngredients(String text) {
        ArrayList<String> ingredients = new ArrayList(Arrays.asList(text.split(":")));
        ingredients.remove(0);
        text = ingredients.toString();
        text = text.substring(1,text.length()-1).replaceAll("\n", " ");
        ingredients = new ArrayList(Arrays.asList(text.split(",|\\.")));
        return ingredients;
    }

    private void assessWarning(String text, ArrayList<Allergen> allergens) {
        TextView warning = findViewById(R.id.warning);
        ArrayList<String> minorWarnings = new ArrayList<String>();
        ArrayList<String> moderateWarnings = new ArrayList<String>();
        ArrayList<String> majorWarnings = new ArrayList<String>();
        for (int i = 0; i < allergens.size(); i++) {
            if (text.contains(allergens.get(i).get_name())) {
                if (text.startsWith("contains") || text.startsWith("ingredients")) {
                    if (!majorWarnings.contains(allergens.get(i).get_name())) {
                        majorWarnings.add(allergens.get(i).get_name());
                    }
                }
                switch (allergens.get(i).get_level()) {
                    case 1:
                        if (text.startsWith("may contain")) {
                            if (!moderateWarnings.contains(allergens.get(i).get_name())) {
                                moderateWarnings.add(allergens.get(i).get_name());
                            }
                        }
                        else if (text.startsWith("processes") ||
                                text.startsWith("uses")) {
                            if (!minorWarnings.contains(allergens.get(i).get_name())) {
                                minorWarnings.add(allergens.get(i).get_name());
                            }
                        } break;
                    case 2:
                        if (text.startsWith("may contain") ||
                                text.startsWith("processes") ||
                                text.startsWith("uses")) {
                            if (!moderateWarnings.contains(allergens.get(i).get_name())) {
                                moderateWarnings.add(allergens.get(i).get_name());
                            }
                        }
                    case 3:
                        if (text.startsWith("may contain")) {
                            if (!majorWarnings.contains(allergens.get(i).get_name())) {
                                majorWarnings.add(allergens.get(i).get_name());
                            }
                        }
                        else if (text.startsWith("processes") ||
                                text.startsWith("uses")) {
                            if (!moderateWarnings.contains(allergens.get(i).get_name())) {
                                moderateWarnings.add(allergens.get(i).get_name());
                            }
                        }
                }
            }
        }
        if (!majorWarnings.isEmpty()) {
            warning.setText("Warning! Contains " + majorWarnings.toString() + "!");
            warning.setTextColor(getResources().getColor(R.color.colorRed));
        }
        else if (!moderateWarnings.isEmpty()) {
            warning.setText("Caution, may contain " + moderateWarnings.toString() + ".");
            warning.setTextColor(getResources().getColor(R.color.colorYellow));
        }
        else if (!minorWarnings.isEmpty()) {
            warning.setText("Harmless. Produced in facility that also uses " + minorWarnings.toString() + ".");
            warning.setTextColor(getResources().getColor(R.color.colorGreen));
        }
    }

    /*TODO: Example if something says "ingredients: something may contain something else", add both
    "ingredients: something" and "may contain something else" as separately instead of just
    "may contain something else"*/
    private ArrayList<String> splitBlocks(String block) {
        ArrayList<String> blocks = new ArrayList<>();
        if (allergens.stream().anyMatch(s -> block.toLowerCase().contains(s.get_name()))) {
            if (block.toLowerCase().contains("ingredients") &&
                    !block.toLowerCase().trim().startsWith("ingredients")) {
                blocks.addAll(splitBlocks(block.toLowerCase().substring(
                        block.toLowerCase().indexOf("ingredients") - 1).trim()));
                blocks.addAll(splitBlocks(block.toLowerCase().substring(0,
                        block.toLowerCase().indexOf("ingredients")).trim()));

            } else if (block.toLowerCase().contains("contains") &&
                    !block.toLowerCase().trim().startsWith("contains")) {
                blocks.addAll(splitBlocks(block.toLowerCase().substring(
                        block.toLowerCase().indexOf("contains") - 1).trim()));
                blocks.addAll(splitBlocks(block.toLowerCase().substring(0,
                        block.toLowerCase().indexOf("contains")).trim()));

            } else if (block.toLowerCase().contains("may contain") &&
                    !block.toLowerCase().trim().startsWith("may contain")) {
                blocks.addAll(splitBlocks(block.toLowerCase().substring(
                        block.toLowerCase().indexOf("may contain") - 1).trim()));
                blocks.addAll(splitBlocks(block.toLowerCase().substring(0,
                        block.toLowerCase().indexOf("may contain")).trim()));

            } else if (block.toLowerCase().contains("processes") &&
                    !block.toLowerCase().trim().startsWith("processes")) {
                blocks.addAll(splitBlocks(block.toLowerCase().substring(
                        block.toLowerCase().indexOf("processes") - 1).trim()));
                blocks.addAll(splitBlocks(block.toLowerCase().substring(0,
                        block.toLowerCase().indexOf("processes")).trim()));

            } else if (block.toLowerCase().contains("uses") &&
                    !block.toLowerCase().trim().startsWith("uses")) {
                blocks.addAll(splitBlocks(block.toLowerCase().substring(
                        block.toLowerCase().indexOf("uses") - 1).trim()));
                blocks.addAll(splitBlocks(block.toLowerCase().substring(0,
                        block.toLowerCase().indexOf("uses")).trim()));
            }
            // Ingredients indicator begins block and contains no other ingredients indicator
            if (blocks.size() == 0) {
                blocks.add(block);
            }
        }
        return blocks;
    }
}
