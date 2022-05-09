package com.example.hospital.User;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hospital.R;
import com.example.hospital.Var;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewClinic extends AppCompatActivity {


    String clinic;

    LinearLayout linear_doctor_list;
    ImageButton imageButton ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_clinic);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            clinic = extras.getString("clinic");
        }
        imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        linear_doctor_list = findViewById(R.id.linear_doctor_list);

        get_doctor_specialty(clinic);


    }


    private void get_doctor_specialty(String specialty) {

        final String REQUEST_URL = Var.get_doctor_specialty;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonRequest = new StringRequest(
                Request.Method.POST, REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        JSONArray obj = null;
                        try {
                            obj = new JSONArray(response);
                            for (int i = 0; i < obj.length(); i++) {
                                doctorlist(obj.getJSONObject(i));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        ) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                Log.d("mStatusCode", String.valueOf(mStatusCode));


                return super.parseNetworkResponse(response);

            }

            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("specialty", specialty);


                return MyData;
            }
        };
        requestQueue.add(jsonRequest);


    }

    private void doctorlist(JSONObject jsonObject) throws JSONException {

        JSONObject data = new JSONObject(jsonObject.getString("data"));
        View view = getLayoutInflater().inflate(R.layout.doctor_list_card, null);


        TextView doctor_name = view.findViewById(R.id.doctor_name);

        TextView doctor_specialty = view.findViewById(R.id.doctor_specialty);
        TextView time = view.findViewById(R.id.time);

        doctor_name.setText(data.getString("name"));
        doctor_specialty.setText(data.getString("specialty"));
        time.setText(data.getString("start_time") + "  " + data.getString("end_time"));

        linear_doctor_list.addView(view);

    }

}