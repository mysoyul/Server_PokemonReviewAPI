package com.pokemonreview.api.config;

import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.PokemonType;
import com.pokemonreview.api.repository.PokemonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
//import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Configuration
@Profile("test")
public class AppConfig {
    @Bean
    public CommandLineRunner test(PokemonRepository pokemonRepository) {
        return args -> {
            System.out.println("**** Pokemon Insert 시작");
            //pokemonRepository.deleteAll();
            List<Pokemon> pokemonList = IntStream.rangeClosed(1, 10)
                    .mapToObj(i -> Pokemon.builder()
                            .name("pikachu" + i)
                            .type(PokemonType.ELECTRIC)
                            .build())
                    .collect(Collectors.toList());

            pokemonRepository.saveAll(pokemonList);
            System.out.println("**** Pokemon Insert 끝");

        };
    }

//    @Bean
//    public ModelMapper modelMapper() {
//        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.getConfiguration().setSkipNullEnabled(true);
//
//        return modelMapper;
//    }
}
