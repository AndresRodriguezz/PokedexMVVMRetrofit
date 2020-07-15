package co.and.pokedexmvvmretrofitglide.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import co.and.pokedexmvvmretrofitglide.R;
import co.and.pokedexmvvmretrofitglide.models.Pokemon;

public class RecyclerPokemonsItems extends RecyclerView.Adapter<RecyclerPokemonsItems.ViewHolder> {
    private List<Pokemon> pokemonList;
    private Context context;

    public RecyclerPokemonsItems(List<Pokemon> pokemonList, Context context) {
        this.pokemonList = pokemonList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_pokemon,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pokemon pokemon = pokemonList.get(position);
        holder.txtPokemon.setText(pokemon.getName());

        Glide.with(context)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + pokemon.getNumber() + ".png")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imagePokemon);

    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    public void setDataPokemons(List<Pokemon> pokemons){
        this.pokemonList = pokemons;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imagePokemon;
        private TextView txtPokemon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imagePokemon = itemView.findViewById(R.id.categoryThumb);
            txtPokemon = itemView.findViewById(R.id.categoryName);
        }
    }
}
