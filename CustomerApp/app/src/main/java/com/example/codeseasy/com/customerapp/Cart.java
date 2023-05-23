package com.example.codeseasy.com.customerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Cart extends AppCompatActivity {

    TextView textViewCartData, textViewDisDur;
    SharedPreferences sharedPreferences;
    Button buttonConfirm, buttonRemove;

    ArrayList<String> arrayId, arraymenuId, arraynombre;

    int pricePerKM = 5;
    String urlConfirm =
            "https://webapitsbddandroid.azurewebsites.net/api/Commande/AddCommande";

    String urlRemove =
            "https://webapitsbddandroid.azurewebsites.net/api/Commande/DeletePanier?P_Id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        textViewCartData = findViewById(R.id.textCartData);
        textViewDisDur = findViewById(R.id.textDisDur);
        sharedPreferences = getSharedPreferences("CustomerApp", MODE_PRIVATE);
        buttonConfirm = findViewById(R.id.btnConfirmOrder);
        buttonRemove = findViewById(R.id.btnClearCart);
        arraymenuId = new ArrayList<>();
        arraynombre = new ArrayList<>();
        arrayId = new ArrayList<>();
        fetchData();
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendRequest(view, urlConfirm);
            }
        });

        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequestDelete(view, urlRemove);
            }
        });
    }

    public void sendRequestDelete(View v, String apiUrl) {
        Log.e("url", apiUrl);
        v.setEnabled(false);
        for (int i = 0; i < arrayId.size();i++) {
            String Url = apiUrl + arrayId.get(i);
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, Url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    v.setEnabled(true);
                    if (!response.equals("success")) {
                        Toast.makeText(getApplicationContext(), "Operation success", Toast.LENGTH_SHORT).show();
                        finish();
                    } else
                        Toast.makeText(getApplicationContext(), "Operation failed", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    v.setEnabled(true);
                    error.printStackTrace();
                }
            });
            queue.add(stringRequest);
        }
    }

    public void sendRequest(View v, String apiUrl) {
        Log.e("url", apiUrl);
        v.setEnabled(false);
        for (int i = 0; i < arraymenuId.size();i++) {
            String Url = apiUrl +"?identifiantId="+sharedPreferences.getString("id", "")+"&adresse=toto&menuId="+  arraymenuId.get(i);
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    v.setEnabled(true);
                    if (!response.equals("success")) {
                        Toast.makeText(getApplicationContext(), "Operation success", Toast.LENGTH_SHORT).show();
                        finish();
                    } else
                        Toast.makeText(getApplicationContext(), "Operation failed", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    v.setEnabled(true);
                    error.printStackTrace();
                }
            });
            queue.add(stringRequest);
        }
    }

    public void parseJSON(String data) {
        try {
            String newdata = "["+data+"]";
            JSONArray jsonArray = new JSONArray(newdata);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject stu = jsonArray.getJSONObject(i);
                String id = stu.getString("id");
                String name = stu.getString("titre");
                String des = stu.getString("description");
                String numItem = arraynombre.get(i);
                String price = stu.getString("prix");
                textViewCartData.append("Item: " + name + "\nPrice: " + price + "$\nNumber of Item: " + numItem + "\n\n");
            }

        } catch (JSONException e) {

            Log.e("error", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void test()
    {
        for (String stringmenuid: arraymenuId) {

            RequestQueue queue1 = Volley.newRequestQueue(this);



            StringRequest stringRequest1 = new StringRequest(Request.Method.GET,
                        "https://webapitsbddandroid.azurewebsites.net/api/Menu/GetMenuById?P_Id="
                                + stringmenuid, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        parseJSON(response);


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                queue1.add(stringRequest1);

        }
    }


    public void fetchData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "https://webapitsbddandroid.azurewebsites.net/api/Panier/GetPanierByAccountId?P_Id="
                        + sharedPreferences.getString("id", ""), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length();i++)
                    {
                        JSONObject stu = jsonArray.getJSONObject(i);
                        String id = stu.getString("id");
                        String menuId = stu.getString("menuId");
                        String nombre = stu.getString("nombre");
                        arrayId.add(id);
                        arraymenuId.add(menuId);
                        arraynombre.add(nombre);

                    }
                    test();

                }
                catch(JSONException e)
                {

                    e.printStackTrace();
                }
                //parseJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(stringRequest);

    }
}