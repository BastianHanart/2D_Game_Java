package com.mygame;

public enum Talent {
    HPMax_1 ("Max HP I", 1, null, 600, 300),
    HPMax_2 ("Max HP II", 2, HPMax_1, 625, 450),
    HPMax_3 ("Max HP III", 2, HPMax_2, 600, 600),
    HPMax_4 ("Max HP IV", 2, HPMax_3, 575, 750),
    HPMax_5 ("Max HP V", 2, HPMax_4, 600, 900),

    Damage_1 ("Damage I", 1, null, 1000, 300),
    Damage_2 ("Damage II", 2, Damage_1, 1025, 450),
    Damage_3 ("Damage III", 3, Damage_2, 1000, 600),
    Damage_4 ("Damage IV", 4, Damage_3, 975, 750),
    Damage_5 ("Damage V", 5, Damage_4, 1000, 900),
    
    Dash ("Dash", 1, null, 800, 325),
    DoubleJump ("Double jump", 2, Dash, 825, 475);

    public final String name;
    public final int cost;
    public final Talent prerequisite;
    public final float x;
    public final float y;

    Talent(String name, int cost, Talent prerequisite, float x, float y) {
        this.name = name; 
        this.cost = cost; 
        this.prerequisite = prerequisite;
        this.x = x; 
        this.y = y;
    }
}