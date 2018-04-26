package com.udacity.sandwichclub;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    // Declaring all views that require population (see populateUI)
    // NOTE: Picasso is used to load sandwich image.
    private TextView mMainNameView;
    private TextView mAlsoKnownView;
    private TextView mIngredientsView;
    private TextView mOriginView;
    private TextView mDescriptionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Method used to populate views for Sandwich Detail Activity.
     * Precondition: parameter should be checked for null BEFORE being passed
     * to this method.
     * @param parsedSandwich non-null Sandwich object used to populate the views of
     *                       DetailActivity via its member variables.
     */
    private void populateUI(Sandwich parsedSandwich) {

        mMainNameView = findViewById(R.id.sandwich_main_name_view);
        mMainNameView.setText(parsedSandwich.getMainName());

        mAlsoKnownView = findViewById(R.id.also_known_tv);
        List<String> otherNameList = parsedSandwich.getAlsoKnownAs();

        if (otherNameList == null || otherNameList.isEmpty()) {
            mAlsoKnownView.setText("N/A");
        } else {
            for (String otherName : otherNameList) {
                mAlsoKnownView.append(otherName);

                // if is NOT last element of List
                if (!otherName.equals(otherNameList.get(otherNameList.size() - 1))) {
                    mAlsoKnownView.append(", ");
                }
            }
        }

        mIngredientsView = findViewById(R.id.ingredients_tv);
        List<String> ingredientList = parsedSandwich.getIngredients();

        if (ingredientList == null || ingredientList.isEmpty()) {
            mIngredientsView.setText("No ingredients found ... a sammy for ghosts maybe?");
            Log.e("No ingredients found: ", "No ingredients found for" + parsedSandwich);
        } else {
            for (String ingredient : ingredientList) {
                mIngredientsView.append(ingredient);

                // if is NOT last element of List
                if (!ingredient.equals(ingredientList.get(ingredientList.size() - 1))) {
                    mIngredientsView.append(", ");
                }
            }
        }

        mOriginView = findViewById(R.id.origin_tv);
        if (parsedSandwich.getPlaceOfOrigin().length() < 1) {
            mOriginView.setText("Unknown");
        } else {
            mOriginView.setText(parsedSandwich.getPlaceOfOrigin());
        }

        mDescriptionView = findViewById(R.id.description_tv);
        mDescriptionView.setText(parsedSandwich.getDescription());
    }
}
