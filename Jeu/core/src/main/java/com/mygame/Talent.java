package com.mygame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Talent {
    // Branche de envie (en haut à droite)
    Dash("Dash", 1, Collections.emptyList(), 1703, 540),
    DoubleJump("Double jump", 2, Arrays.asList(Dash), 2092, 435),

    // Branche de luxures (à gauche)
    HPMax_1("Max HP I", 1, Collections.emptyList(), 980, 775),
    HPMax_2("Max HP II", 2, Arrays.asList(HPMax_1), 703, 775),
    HPMax_3("Max HP III", 2, Arrays.asList(HPMax_2), 485, 910),
    HPMax_4("Max HP IV", 2, Arrays.asList(HPMax_3), 440, 620),
    HPMax_5("Max HP V", 2, Arrays.asList(HPMax_4), 317, 775),

    // Branche de colère (à droite)
    Damage_1("Damage I", 1, Collections.emptyList(), 1778, 772),
    Damage_2("Damage II", 2, Arrays.asList(Damage_1), 2038, 772),
    Damage_3("Damage III", 3, Arrays.asList(Damage_2), 2293, 772);

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
