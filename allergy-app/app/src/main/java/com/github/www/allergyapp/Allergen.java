package com.github.www.allergyapp;

public class Allergen {
    private String _name;
    private int _level;
    //adding parent id
    private String parentId;

    public Allergen() {
        this._name = "";
        this._level = 1;
        //add default id of none
        this.parentId="none";
    }

    public Allergen(String name, int level) {
        this._name = name.trim().toLowerCase() ;
        this._level = level;
        //add default id of none
        this.parentId="none";
    }

    public Allergen(String name, int level, String parent_id) {
        this._name = name.trim().toLowerCase();
        this._level = level;
        this.parentId = parent_id;
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

    public String get_parent_id(){return parentId;}

    public void set_parent_id(String id) {this.parentId = id;}
}
