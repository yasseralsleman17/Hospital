package com.example.hospital.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class ViewAppointment extends AppCompatActivity {

    LinearLayout appointment;

    String user_id;
    Button button_refresh;
    ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointment);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_id = extras.getString("user_id");
        }
        appointment = findViewById(R.id.appointment);
        button_refresh = findViewById(R.id.button_refresh);

        View appointment_title = getLayoutInflater().inflate(R.layout.appointment_card, null);

        ConstraintLayout constraintLayout = appointment_title.findViewById(R.id.constraintLayout);
        Button button_edit = appointment_title.findViewById(R.id.button_edit);
        Button button_delete = appointment_title.findViewById(R.id.button_delete);
        constraintLayout.removeView(button_edit);
        constraintLayout.removeView(button_delete);


        imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        button_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appointment.removeAllViews();
                appointment.addView(appointment_title);
                get_appointment();

            }
        });


        appointment.addView(appointment_title);
        get_appointment();
    }

    private void get_appointment() {

        final String REQUEST_URL = Var.get_all_appointment;

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
                                view_appointment(obj.getJSONObject(i));
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
                MyData.put("user_id", user_id);


                return MyData;
            }
        };
        requestQueue.add(jsonRequest);


    }


    private void view_appointment(JSONObject jsonObject) throws JSONException {

        JSONObject data = new JSONObject(jsonObject.getString("data"));

        String appointment_id = data.getString("id");

        View view = getLayoutInflater().inflate(R.layout.appointment_card, null);

        TextView textView9 = view.findViewById(R.id.textView9);
        TextView textView10 = view.findViewById(R.id.textView10);
        TextView textView11 = view.findViewById(R.id.textView11);
        TextView textView12 = view.findViewById(R.id.textView12);


        Button button_edit = view.findViewById(R.id.button_edit);
        Button button_delete = view.findViewById(R.id.button_delete);

        textView9.setText(data.getString("clinic"));
        textView10.setText(data.getString("doctor"));
        textView11.setText(data.getString("date"));
        textView12.setText(data.getString("time"));


        button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditAppointment.class);
                intent.putExtra("appointment_id", appointment_id);
                startActivity(intent);
            }
        });

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String REQUEST_URL = Var.delete_appointment;
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest jsonRequest = new StringRequest(
                        Request.Method.POST, REQUEST_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("response", response);
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    Log.d("response", obj.getString("code"));
                                    if (obj.getString("code").equals("-1")) {
                                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                                    } else { Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show(); }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show(); } }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }}
                ) {
                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        int mStatusCode = response.statusCode;
                        Log.d("mStatusCode", String.valueOf(mStatusCode));

                        return super.parseNetworkResponse(response);

                    }

                    protected Map<String, String> getParams() {
                        Map<String, String> MyData = new HashMap<String, String>();
                        MyData.put("appointment_id", appointment_id);


                        return MyData;
                    }
                };
                requestQueue.add(jsonRequest);


                appointment.removeView(view);

            }
        });

        appointment.addView(view);


    }

}