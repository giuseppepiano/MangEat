package com.giu7.mangeat.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.giu7.mangeat.R;
import com.giu7.mangeat.Utils;
import com.giu7.mangeat.datamodels.Restaurant;
import com.giu7.mangeat.services.RestController;
import com.giu7.mangeat.ui.adapters.RestaurantAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String VIEW_MODE = "viewMode";

    private RecyclerView restaurantRV;
    private RecyclerView.LayoutManager layoutManager;
    private RestaurantAdapter adapter;
    private ArrayList<Restaurant> arrayList = new ArrayList<>();

    private SharedPreferences prefs;

    private RestController restController;


    /*private ArrayList<Restaurant> getData(){
        arrayList = new ArrayList<>();
        arrayList.add(new Restaurant("Mc Donald's", "Via Tiburtina 10", 5.00f, "http://www.lamescolanza.com/wp-content/uploads/2017/01/McDonalds.png"));
        arrayList.add(new Restaurant("Burger King", "Via Sandro Sandri 100", 3.00f, "https://media-cdn.tripadvisor.com/media/photo-s/11/0f/6d/9c/burger-king.jpg"));
        arrayList.add(new Restaurant("KFC", "Via Italia 7", 10.00f, "https://www.hadalsame.com/wp-content/uploads/2018/08/b-163.jpg"));

        return arrayList;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        restaurantRV = findViewById(R.id.places_rv);

        layoutManager = new LinearLayoutManager(this);
        adapter = new RestaurantAdapter(this);

        prefs = getSharedPreferences(Utils.PACKAGE_NAME,MODE_PRIVATE);
        if (prefs != null) {
            adapter.setGridMode(prefs.getBoolean(VIEW_MODE,true));
        }

        restaurantRV.setLayoutManager(layoutManager);
        restaurantRV.setAdapter(adapter);

        restController = new RestController(this);
        restController.getRequest(Restaurant.ENDPOINT,this,this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor preferencesEditor = prefs.edit();
        preferencesEditor.putBoolean(VIEW_MODE, adapter.isGridMode());
        preferencesEditor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.view_mode){
            setLayoutManager();
            return true;
        }
        else if (item.getItemId()==R.id.login_menu){
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        }
        else if (item.getItemId()==R.id.checkout_menu){
            startActivity(new Intent(this, CheckoutActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setLayoutManager(){
        layoutManager = new LinearLayoutManager(this);
        adapter.setGridMode(!adapter.isGridMode());
        restaurantRV.setLayoutManager(layoutManager);
        restaurantRV.setAdapter(adapter);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(TAG,error.getMessage());
        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(String response) {
        try{
            JSONArray jsonArray = new JSONArray(response);
            for (int i=0; i<jsonArray.length(); i++){
                arrayList.add(new Restaurant(jsonArray.getJSONObject(i)));
            }
            adapter.setData(arrayList);
        } catch (JSONException e){
            Log.e(TAG,e.getMessage());
        }
    }
}
