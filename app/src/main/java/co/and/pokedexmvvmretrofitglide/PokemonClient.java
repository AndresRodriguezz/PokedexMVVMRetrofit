package co.and.pokedexmvvmretrofitglide;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PokemonClient {
    private static final String BASE_URL = "https://pokeapi.co/api/v2/";

    public static Retrofit getPokemonClient(){

    return new Retrofit.Builder().baseUrl(BASE_URL)
            //.client()
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    }
}