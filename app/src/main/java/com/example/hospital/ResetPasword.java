package com.example.hospital;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPasword extends AppCompatActivity {

    EditText editText_code, editText_password, editText_confirm_password;
    Button button_reset;
    TextInputLayout textInputLayout, textInputLayout8, textInputLayout7;

    String email, password_txt, confirm_password_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pasword);

        editText_code = findViewById(R.id.editText_code);

        editText_password = findViewById(R.id.editText_password);
        editText_confirm_password = findViewById(R.id.editText_confirm_password);

        textInputLayout = findViewById(R.id.textInputLayout);
        textInputLayout8 = findViewById(R.id.textInputLayout8);
        textInputLayout7 = findViewById(R.id.textInputLayout7);

        button_reset = findViewById(R.id.button_reset);


        editText_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().length() == 5) {


                    final String REQUEST_URL = Var.check_code;

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest jsonRequest = new StringRequest(
                            Request.Method.POST,
                            REQUEST_URL,
                            new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    try {

                                        JSONObject obj = new JSONObject(response);
                                        Log.d("response", obj.getString("code"));
                                        if (obj.getString("code").equals("-1")) {
                                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                                        } else {

                                            textInputLayout.setVisibility(View.VISIBLE);
                                            textInputLayout8.setVisibility(View.VISIBLE);
                                            button_reset.setVisibility(View.VISIBLE);
                                            textInputLayout7.setVisibility(View.INVISIBLE);

                                            email = obj.getString("email");

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
                            MyData.put("code", charSequence.toString());
                            return MyData;
                        }
                    };
                    jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    requestQueue.add(jsonRequest);


                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        textInputLayout.setVisibility(View.INVISIBLE);
        textInputLayout8.setVisibility(View.INVISIBLE);
        button_reset.setVisibility(View.INVISIBLE);
        button_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                password_txt = editText_password.getText().toString();
                confirm_password_txt = editText_confirm_password.getText().toString();

                boolean flag = true;


                if (password_txt.isEmpty()) {
                    editText_password.setError("This field is required");
                    flag = false;
                }
                if (confirm_password_txt.isEmpty()) {
                    editText_confirm_password.setError("This field is required");
                    flag = false;
                }
                if (!password_txt.equals(confirm_password_txt)) {
                    editText_confirm_password.setError("Passwords are not same");
                    editText_password.setError("Passwords are not same");
                    flag = false;
                }

                if (!flag) {
                    return;
                }

                final String REQUEST_URL = Var.reset_password;

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest jsonRequest = new StringRequest(
                        Request.Method.POST,
                        REQUEST_URL,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                try {

                                    JSONObject obj = new JSONObject(response);
                                    Log.d("response", obj.getString("code"));
                                    if (obj.getString("code").equals("1")) {
                                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();

                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);

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
                        MyData.put("email", email);
                        MyData.put("password", password_txt);
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