package co.and.pokedexmvvmretrofitglide.interfaces;

import co.and.pokedexmvvmretrofitglide.models.DataPokemon;
import retrofit2.Call;
import retrofit2.http.GET;

public interface PokeApi {

    @GET("pokemon")
    Call<DataPokemon> getListPokemons();
}
