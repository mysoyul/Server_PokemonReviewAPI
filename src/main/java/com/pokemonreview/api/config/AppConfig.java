package com.pokemonreview.api.config;

import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.PokemonType;
import com.pokemonreview.api.models.Review;
import com.pokemonreview.api.repository.PokemonRepository;
import com.pokemonreview.api.repository.ReviewRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
//import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Configuration
//@Profile("prod")
public class AppConfig {
    @Bean
    public CommandLineRunner test(PokemonRepository pokemonRepository, ReviewRepository reviewRepository) {
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

            System.out.println("#### Review Insert 시작");
            pokemonRepository.findAll() //List<Pokemon>
                            .forEach(pokemon -> {
                                Review review = Review.builder()
                                        .title(pokemon.getName() + " title")
                                        .content(pokemon.getName() + " content")
                                        .stars(5)
                                        .pokemon(pokemon)
                                        .build();
                                reviewRepository.save(review);

                                Review review2 = Review.builder()
                                        .title(pokemon.getType().getName() + " title")
                                        .content(pokemon.getType().getName() + " content")
                                        .stars(9)
                                        .pokemon(pokemon)
                                        .build();
                                reviewRepository.save(review2);

                            });
            System.out.println("#### Review Insert 끝");

        };
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        //값이 null 이면 매핑 하지 않고 Skip 하는 설정
        modelMapper.getConfiguration().setSkipNullEnabled(true);

        return modelMapper;
    }
}
