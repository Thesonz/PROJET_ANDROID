package com.example.codeseasy.com.customerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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

public class ListOrders extends AppCompatActivity {

    TextView textViewOrders;

    ArrayList<String> arrayId;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_orders);
        textViewOrders = findViewById(R.id.textOrders);
        sharedPreferences = getSharedPreferences("CustomerApp", MODE_PRIVATE);
        textViewOrders.setMovementMethod(new ScrollingMovementMethod());
        arrayId = new ArrayList<>();
        fetchData("https://webapitsbddandroid.azurewebsites.net/api/Commande/GetCommandeByAccountId?P_Id="
                +sharedPreferences.getString("id", ""));
    }

    public void parseJSON(String response){
        try {

            String data = "["+response+"]";
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject stu = jsonArray.getJSONObject(i);
                String id = stu.getString("id");
                    textViewOrders.append("Items: \n" + stu.getString("titre") + "\nPrice: "
                            + stu.getString("prix") + "\n");

                textViewOrders.append("\n\n");
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void test()
    {
        for (String stringid: arrayId) {

            RequestQueue queue1 = Volley.newRequestQueue(this);



            StringRequest stringRequest1 = new StringRequest(Request.Method.GET,
                    "https://webapitsbddandroid.azurewebsites.net/api/Menu/GetMenuById?P_Id="
                            + stringid, new Response.Listener<String>() {
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

    public void fetchData(String apiUrl) {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {


                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject stu = jsonArray.getJSONObject(i);
                        arrayId.add((stu.getString("menuId")));
                    }
                    test();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
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