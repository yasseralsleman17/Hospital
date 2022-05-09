package com.example.hospital;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hospital.User.UserHomePage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ForgetPassword extends AppCompatActivity {


    private EditText email;
    private String user_name_txt;

    private Button button_next;

    Random r;

    int min = 11111, max = 99999, code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);


        email = (EditText) findViewById(R.id.editText_email);

        button_next = (Button) findViewById(R.id.button_send);


        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                r = new Random();

                user_name_txt = email.getText().toString().trim();

                boolean flag = true;


                if (user_name_txt.isEmpty()) {
                    email.setError("This field is required");
                    flag = false;
                }


                if (!flag) {
                    return;
                }

                code = r.nextInt((max - min) + 1) + min;

                final String REQUEST_URL = Var.send_code;
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest jsonRequest = new StringRequest(
                        Request.Method.POST,
                        REQUEST_URL,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                try {
                                    Log.d("responseresponse",response);
                                    JSONObject obj = new JSONObject(response);
                                    Log.d("response", obj.getString("code"));
                                    if (obj.getString("code").equals("-1")) {
                                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(ForgetPassword.this, obj.getString("message")+" to "+user_name_txt, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), ResetPasword.class);
                                        startActivity(intent);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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
                        MyData.put("email", user_name_txt);
                        MyData.put("code", String.valueOf(code));
                        return MyData;
                    }
                };
                jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                requestQueue.add(jsonRequest);


            }
        });
    }
}