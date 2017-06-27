package com.programming.kantech.bakingmagic.app.data.retrofit;


import com.programming.kantech.bakingmagic.app.data.model.pojo.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by patrick keogh on 2017-06-15.
 */

public interface ApiInterface {

    // Path for the recipes
    String GET_RECIPES = "android-baking-app-json/";


    @GET(GET_RECIPES)
    Call<List<Recipe>> getRecipes();


}
