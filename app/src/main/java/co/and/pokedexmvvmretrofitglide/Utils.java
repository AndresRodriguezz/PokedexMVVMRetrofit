package co.and.pokedexmvvmretrofitglide;

import co.and.pokedexmvvmretrofitglide.interfaces.PokeApi;

public class Utils {

    public static PokeApi getApi(){
        return PokemonClient.getPokemonClient().create(PokeApi.class);
    }
}
