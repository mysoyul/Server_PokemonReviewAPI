package com.pokemonreview.api.models;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum PokemonType {
    NORMAL, FIRE, WATER, ELECTRIC, GRASS,
    ICE, FIGHTING, POISON, GROUND, FLYING,
    PSYCHIC, BUG, ROCK, GHOST, DRAGON,
    DARK, STEEL, FAIRY;

    public static List<String> nameList() {
        return Arrays.stream(PokemonType.values())
                .map(value -> value.name())
                .collect(Collectors.toList());
    }
}