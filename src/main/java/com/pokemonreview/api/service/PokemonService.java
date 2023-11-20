package com.pokemonreview.api.service;

import com.pokemonreview.api.dto.PageResponse;
import com.pokemonreview.api.dto.PokemonDto;

import java.util.List;

public interface PokemonService {
    PokemonDto createPokemon(PokemonDto pokemonDto);
    PageResponse<?> getAllPokemon(int pageNo, int pageSize);
    PokemonDto getPokemonById(int id);
    PokemonDto updatePokemon(PokemonDto pokemonDto, int id);
    void deletePokemonId(int id);
}