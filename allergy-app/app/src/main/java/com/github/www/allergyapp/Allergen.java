package com.github.www.allergyapp;

public class Allergen {
    private String _name;
    private int _level;

    public Allergen() {
        this._name = "";
        this._level = 1;
    }

    public Allergen(String name, int level) {
        this._name = name ;
        this._level = level;
    }

    public int get_level() {
        return _level;
    }

    public String get_name() {
        return _name;
    }

    public void set_level(int level) {
        this._level = level;
    }

    public void set_name(String name) {
        this._name = name;
    }
}
