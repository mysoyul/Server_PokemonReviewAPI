package com.pokemonreview.api.service;

import com.pokemonreview.api.dto.PageResponse;
import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.PokemonType;
import com.pokemonreview.api.repository.PokemonRepository;
import com.pokemonreview.api.service.impl.PokemonServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PokemonServiceTests {
    @Mock
    private PokemonRepository pokemonRepository;
    @InjectMocks
    private PokemonServiceImpl pokemonService;
    
    @Test
    public void PokemonService_CreatePokemon_ReturnsPokemonDto() {
        Pokemon pokemon = Pokemon.builder()
                .name("pikachu")
                .type(PokemonType.ELECTRIC)
                .build();
        PokemonDto pokemonDto = PokemonDto.builder()
                .name("pikachu")
                .type(PokemonType.ELECTRIC)
                .build();

        when(pokemonRepository.save(Mockito.any(Pokemon.class)))
                .thenReturn(pokemon);

        PokemonDto savedPokemon = pokemonService.createPokemon(pokemonDto);

        Assertions.assertThat(savedPokemon).isNotNull();
        System.out.println("savedPokemon = " + savedPokemon);
    }

    @Test
    public void PokemonService_GetAllPokemon_ReturnsResponseDto() {
        List<Pokemon> pokemonList = IntStream.range(0, 20)
                .mapToObj(i -> Pokemon.builder()
                        .name("pikachu" + i)
                        .type(PokemonType.ELECTRIC)
                        .build())
                .collect(Collectors.toList());

        pokemonRepository.saveAll(pokemonList);

//        Page<Pokemon> pokemons = Mockito.mock(Page.class);
//        when(pokemonRepository.findAll(Mockito.any(Pageable.class)))
//                .thenReturn(pokemons);

        Pageable pageable =
                PageRequest.of(1, 10, Sort.by("id").descending());
        Page<Pokemon> pokemonPage =
                new PageImpl<>(pokemonList, pageable,20);
        when(pokemonRepository.findAll(pageable))
                .thenReturn(pokemonPage);

        PageResponse<?> pageResponse =
                pokemonService.getAllPokemon(1,10);

        Assertions.assertThat(pageResponse).isNotNull();
        Assertions.assertThat(pageResponse.getContent().size()).isEqualTo(20);
        Assertions.assertThat(pageResponse.getPageSize()).isEqualTo(10);
        System.out.println(pageResponse.getContent().size());
        System.out.println(pageResponse.getPageSize());
        System.out.println(pageResponse.getTotalPages());
    }
    @Test
    public void PokemonService_FindById_ReturnPokemonDto() {
        int pokemonId = 1;
        Pokemon pokemon = Pokemon.builder()
                .id(pokemonId)
                .name("pikachu")
                .type(PokemonType.ELECTRIC)
                .build();

        when(pokemonRepository.findById(pokemon.getId()))
                .thenReturn(Optional.of(pokemon));

        PokemonDto pokemonReturn = 
                pokemonService.getPokemonById(pokemon.getId());

        Assertions.assertThat(pokemonReturn).isNotNull();
        System.out.println("pokemonReturn = " + pokemonReturn);
    }

    @Test
    public void PokemonService_UpdatePokemon_ReturnPokemonDto() {
        int pokemonId = 1;
        Pokemon pokemon =
                Pokemon.builder()
                        .id(pokemonId)
                        .name("pikachu")
                        .type(PokemonType.ELECTRIC)
                        .build();

        PokemonDto pokemonDto =
                PokemonDto.builder()
                        .id(pokemonId)
                        .name("Raichu")
                        .type(PokemonType.NORMAL)
                        .build();

        when(pokemonRepository.findById(pokemonId))
                .thenReturn(Optional.of(pokemon));

        lenient().when(pokemonRepository.save(pokemon))
                .thenReturn(pokemon);

        PokemonDto updateReturn = 
                pokemonService.updatePokemon(pokemonDto, pokemonId);

        Assertions.assertThat(updateReturn).isNotNull();
        System.out.println("updateReturn = " + updateReturn);
    }

    @Test
    public void PokemonService_DeletePokemonById_ReturnVoid() {
        int pokemonId = 1;
        Pokemon pokemon =
                Pokemon.builder()
                        .id(pokemonId)
                        .name("pikachu")
                        .type(PokemonType.ELECTRIC)
                        .build();

        when(pokemonRepository.findById(pokemonId))
                .thenReturn(Optional.ofNullable(pokemon));
        doNothing().when(pokemonRepository).delete(pokemon);

        assertAll(() -> pokemonService.deletePokemonId(pokemonId));
    }
}