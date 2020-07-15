package co.and.pokedexmvvmretrofitglide.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import co.and.pokedexmvvmretrofitglide.R;
import co.and.pokedexmvvmretrofitglide.models.Pokemon;
import co.and.pokedexmvvmretrofitglide.viewholders.PokemonHolder;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonHolder> {
    @NonNull
    private final Context context;

    @NonNull
    private List<Pokemon> list = new ArrayList<>();

    public PokemonAdapter(@NonNull Context context) {
        this.context = context;
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

        Glide.with(context)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + pokemon.getNumber() + ".png")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivCategoryThumb);
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