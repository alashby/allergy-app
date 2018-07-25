package com.github.www.allergyapp;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AllergenMap {
    private Map<String, Allergen> mappedAllergies= new HashMap<>();

    public AllergenMap() {
        mappedAllergies = new HashMap<>();
    }

    public int size() {
        return mappedAllergies.size();
    }
    /**
     * This method will accept a string number to represent the id or "key"
     * and will take in the allergen as the value
     */
    public void addAllergen(String key, Allergen value)
    {
        this.mappedAllergies.put(key,value);
    }

    /**
     * This method will accept a string number to represent the id or "key"
     * and will take in the allergen as the value
     */
    public void addAllergenNoIdNoParentId(String parentAllergyName, Allergen value)
    {
        //make the next key value one more than the total number of allergens
        int keyValue = this.mappedAllergies.size() +1;

        //create temp allergen to traverse through map
        Allergen tempAllergen = new Allergen(parentAllergyName,1);

        //set the parent id as the key of the allergy that was searched
        value.set_parent_id(getAllergyKey(tempAllergen));

        //assign new allergy to the map
        this.mappedAllergies.put(Integer.toString(keyValue),value);
    }



    /**
     * Method takes an allergy and traverses the map and returns the allergens Key
     */
    public String returnAllergenKey(String name)
    {
        for(Map.Entry<String,Allergen> entry : this.mappedAllergies.entrySet())
        {
            if(name.equals(entry.getValue().get_name()))
            {
                return entry.getKey();
            }
        }

        return "Allergy Not Found";
    }



    /**
     * method will accept a parent id and return all
     * allergens that are grouped by the same parent id
     * will return no child allergies if no matching id's
     * TODO: Add comment about change to Allergen output
     */
    public ArrayList<Allergen> getAllChildAllergies(Allergen allergy)
    {
        //temporary arraylist to store the allergies
        ArrayList<Allergen> tempAllergies= new ArrayList<>();

        //get the allergies keyValue
        String allergyKeyValue = getAllergyKey(allergy);

        //traverse the map to check each allergy and it's parent id
        for(Map.Entry<String,Allergen> entry : this.mappedAllergies.entrySet())
        {
            //adds allergy to list if the original Key value matches this allergies parentId
            if(allergyKeyValue.equals(entry.getValue().get_parent_id()))
            {
                tempAllergies.add(entry.getValue());
            }
        }
        //handle empty arraylist
        if(tempAllergies.size()==0)
        {
            tempAllergies.add(new Allergen("No child allergies",0));
        }
        return tempAllergies;
    }

    /**
     *
     * Takes an allergy and searches each item in the map
     * when the correct name is found it returns the key value
     * that is used to identify it
     */
    public String getAllergyKey(Allergen allergy)
    {
        for(Map.Entry<String,Allergen> entry : this.mappedAllergies.entrySet())
        {
            if(allergy.get_name().equals(entry.getValue().get_name()))
            {
                return entry.getKey();
            }
        }
            return "No Allergy";
    }

    /**
     *
     * Find an allergen's parent id based on given string.
     */
    public String getParentId(String allergenName) {
        for(Map.Entry<String,Allergen> entry : this.mappedAllergies.entrySet())
        {
            if(allergenName.equals(entry.getValue().get_name()))
            {
                return entry.getValue().get_parent_id();
            }
        }
        return "Allergy Not Found";
    }

    /**
     *
     * Modify the severity level of an allergen based on its name.
     * Good for adding, editing, and deleting allergens.
     *
     */
    public void setAllergyLevel(String allergenName, int level) {
        for(Map.Entry<String,Allergen> entry : this.mappedAllergies.entrySet())
        {
            if(allergenName.equals(entry.getValue().get_name()))
            {
                String parentId = getParentId(allergenName);
                if (!parentId.equals("Allergy Not Found")) {
                    mappedAllergies.put(entry.getKey(), new Allergen(allergenName, level, getParentId(allergenName)));
                }
                else {
                    mappedAllergies.put(entry.getKey(), new Allergen(allergenName, level));
                }
            }
        }
    }

    public Allergen getAllergenByName(String allergenName) {
        for(Map.Entry<String,Allergen> entry : this.mappedAllergies.entrySet())
        {
            if(allergenName.equals(entry.getValue().get_name()))
            {
                return entry.getValue();
            }
        }
        return new Allergen("Allergy Not Found", 0);
    }

}
