package co.and.pokedexmvvmretrofitglide.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import co.and.pokedexmvvmretrofitglide.R;

public class PokemonHolder extends ViewHolder {
    public final ImageView ivCategoryThumb;
    public final TextView tvCategoryName;
    public PokemonHolder(@NonNull View itemView) {
        super(itemView);
        ivCategoryThumb = itemView.findViewById(R.id.ivCategoryThumb);
        tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
    }
}