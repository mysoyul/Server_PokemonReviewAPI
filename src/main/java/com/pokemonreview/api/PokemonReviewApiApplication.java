package com.pokemonreview.api;

import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.PokemonType;
import com.pokemonreview.api.repository.PokemonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootApplication
public class PokemonReviewApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PokemonReviewApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner test(PokemonRepository pokemonRepository) {
		return args -> {
			System.out.println("**** Pokemon Insert 시작");
			pokemonRepository.deleteAll();
			List<Pokemon> pokemonList = IntStream.range(0, 10)
					.mapToObj(i -> Pokemon.builder()
							.name("pikachu" + i)
							.type(PokemonType.ELECTRIC)
							.build())
					.collect(Collectors.toList());

			pokemonRepository.saveAll(pokemonList);
			System.out.println("**** Pokemon Insert 끝");

		};
	}
}
