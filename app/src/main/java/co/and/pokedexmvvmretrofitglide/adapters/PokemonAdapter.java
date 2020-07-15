package co.and.pokedexmvvmretrofitglide.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import co.and.pokedexmvvmretrofitglide.R;
import co.and.pokedexmvvmretrofitglide.managers.ImageManager;
import co.and.pokedexmvvmretrofitglide.models.Pokemon;
import co.and.pokedexmvvmretrofitglide.viewholders.PokemonHolder;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonHolder> {
    @NonNull
    private List<Pokemon> list = new ArrayList<>();

    public PokemonAdapter() {

    }

    @NonNull
    @Override
    public PokemonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pokemon, parent, false);
        return new PokemonHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonHolder holder, int position) {
        Pokemon pokemon = list.get(position);
        holder.tvCategoryName.setText(pokemon.getName());

        ImageManager.getInstance().setImage("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + pokemon.getNumber() + ".png", holder.ivCategoryThumb);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(@NonNull List<Pokemon> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}