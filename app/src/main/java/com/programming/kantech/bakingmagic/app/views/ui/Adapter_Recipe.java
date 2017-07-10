package com.programming.kantech.bakingmagic.app.views.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.programming.kantech.bakingmagic.app.R;
import com.programming.kantech.bakingmagic.app.data.model.pojo.Recipe;
import com.programming.kantech.bakingmagic.app.utils.Utils_General;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.text.TextUtils.isEmpty;

/**
 * Created by patrick keogh on 2017-06-24.
 *
 */

public class Adapter_Recipe extends RecyclerView.Adapter<Adapter_Recipe.ViewHolder_Recipes> {

    private List<Recipe> mRecipes;
    private Context mContext;

    final private Adapter_Recipe.OnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface OnClickHandler {
        void onClick(Recipe recipe);
    }

    public Adapter_Recipe(Context context, List<Recipe> recipes, Adapter_Recipe.OnClickHandler clickHandler) {
        this.mRecipes = recipes;
        this.mContext = context;
        this.mClickHandler = clickHandler;
    }

    @Override
    public ViewHolder_Recipes onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, null);
        return new ViewHolder_Recipes(layoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder_Recipes holder, int position) {

        Recipe recipe = mRecipes.get(position);

        holder.tv_name.setText(recipe.getName());

        if(isEmpty(recipe.getImage())){
            //holder.iv_image.setVisibility(View.GONE);

            Picasso.with(mContext)
                    .load(R.drawable.cake)
                    .resize(80, 80)
                    .placeholder(R.drawable.image)
                    .error(R.drawable.image)
                    .into(holder.iv_image);
        }else{
            Picasso.with(mContext)
                    .load(recipe.getImage())
                    .resize(40, 40)
                    .placeholder(R.drawable.image)
                    .error(R.drawable.image)
                    .into(holder.iv_image);
        }
    }

    @Override
    public int getItemCount() {
        if (mRecipes == null) return 0;
        return mRecipes.size();
    }

    private Recipe getItem(int position) {
        if (mRecipes == null) return null;
        return mRecipes.get(position);
    }
    public void clearRecipes() {
        mRecipes = new ArrayList<>();
    }

    class ViewHolder_Recipes extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tv_name;
        ImageView iv_image;

        ViewHolder_Recipes(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv_name = (TextView)itemView.findViewById(R.id.tv_recipe_name);
            iv_image = (ImageView)itemView.findViewById(R.id.iv_recipe_image);
        }

        @Override
        public void onClick(View view) {
            //Utils_General.showToast(view.getContext(), "Recipe clicked");
            mClickHandler.onClick(getItem(getAdapterPosition()));
        }
    }
}
