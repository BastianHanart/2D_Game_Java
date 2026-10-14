package com.mygame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Talent {

    Dash("Dash", 1, Collections.emptyList(), 1170, 740),
    DoubleJump("Double jump", 2, Arrays.asList(Dash), 1420, 820),

    HPMax_1("Max HP I", 1, Collections.emptyList(), 655, 560),
    HPMax_2("Max HP II", 2, Arrays.asList(HPMax_1), 465, 560),
    HPMax_3("Max HP III", 2, Arrays.asList(HPMax_2), 310, 450),
    HPMax_4("Max HP IV", 2, Arrays.asList(HPMax_3), 275, 680),
    HPMax_5("Max HP V", 2, Arrays.asList(HPMax_4), 185, 560),

    Damage_1("Damage I", 1, Collections.emptyList(), 1215, 560),
    Damage_2("Damage II", 2, Arrays.asList(Damage_1), 1385, 560),
    Damage_3("Damage III", 3, Arrays.asList(Damage_2, DoubleJump), 1570, 560);

    

    public final String name;
    public final int cost;
    public final float x;
    public final float y;
    public final ArrayList<Talent> prerequisite;

    Talent(String name, int cost, List<Talent> prerequisite, float x, float y) {
        this.name = name;
        this.cost = cost;
        this.prerequisite = new ArrayList<>(prerequisite);
        this.x = x;
        this.y = y;
    }
}