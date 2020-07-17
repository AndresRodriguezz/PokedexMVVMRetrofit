package co.and.pokedexmvvmretrofitglide.interfaces;

import androidx.lifecycle.LiveData;
import co.and.pokedexmvvmretrofitglide.models.DataPokemon;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PokeApi {
    @GET("pokemon")
    Call<DataPokemon> getListPokemons(@Query("offset") int offset, @Query("limit") int limit);
}