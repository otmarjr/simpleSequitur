package simpleSequitur;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author otmarpereira
 */
public class alphabet {

    Set<String> elements;
    Map<Integer, String> elementsValue; // Created to support old version where value is integer.

    private alphabet(Set<String> elements) {
        this.elements = elements;
        this.mapElementsToIntegerValues();
    }

    private void mapElementsToIntegerValues() {
        int key = 0;
        this.elementsValue = new HashMap<>();

        for (String element : this.elements) {
            this.elementsValue.put(key, element);
            key++;
        }
    }

    public boolean containsElement(String value){
        return this.elements.contains(value);
    }
    
    public String getValue(int valueCode) {
        return this.elementsValue.getOrDefault(valueCode, null);
    }

    public Integer getValue(String value) {
        if (this.elements.contains(value)) {
            for (Integer k : elementsValue.keySet()) {
                if (elementsValue.get(k).equals(value)) {
                    return k;
                }
            }
        }
        return null;
    }

    private static alphabet last = null;

    public static alphabet create(Set<String> elements) {
        alphabet sigma = new alphabet(elements);
        last = sigma;
        return last;
    }

    public static alphabet current() {
        return last;
    }
}
