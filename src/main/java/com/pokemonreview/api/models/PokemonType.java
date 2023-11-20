package com.pokemonreview.api.models;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
public enum PokemonType {
    NORMAL("NORMAL"), FIRE("FIRE"), WATER("WATER"),
    ELECTRIC("ELECTRIC"), GRASS("GRASS"), ICE("ICE"),
    FIGHTING("FIGHTING"), POISON(""), GROUND("GROUND"),
    FLYING("FLYING"), PSYCHIC("PSYCHIC"), BUG("BUG"),
    ROCK("ROCK"), GHOST("GHOST"), DRAGON("DRAGON"),
    DARK("DARK"), STEEL("STEEL"), FAIRY("FAIRY");

    private final String name;

    PokemonType(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return this.name;
    }

    public static List<String> nameList() {
        return Arrays.stream(PokemonType.values())
                //.map(value -> value.name())
                .map(Enum::name)
                .collect(Collectors.toList());  //List<String>
    }
}