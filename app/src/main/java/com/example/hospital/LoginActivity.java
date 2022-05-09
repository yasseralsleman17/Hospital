package com.example.hospital;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hospital.Admin.AdminHomePage;
import com.example.hospital.User.UserHomePage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private String user_name_txt, password_txt;

    private Button login;
    TextView tosignin,forget_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.editText_email);
        password = (EditText) findViewById(R.id.editText_password);

        login = (Button) findViewById(R.id.button_login);

        tosignin = (TextView) findViewById(R.id.tosignin);
        tosignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        forget_password = (TextView) findViewById(R.id.forget_password);
        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgetPassword.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user_name_txt = email.getText().toString();
                password_txt = password.getText().toString();

                boolean flag = true;


                if (user_name_txt.isEmpty()) {
                    email.setError("This field is required");
                    flag = false;
                }
                if (password_txt.isEmpty()) {
                    password.setError("This field is required");
                    flag = false;
                }

                if (!flag) {
                    return;
                }

                if (user_name_txt.equals("admin@gmail.com") && password_txt.equals("admin123")) {

                    startActivity(new Intent(getApplicationContext(), AdminHomePage.class));

                    return;
                }

                final String REQUEST_URL = Var.Signin_url;

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest jsonRequest = new StringRequest(
                        Request.Method.POST,
                        REQUEST_URL,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    Log.d("response", obj.getString("data"));
                                    if (obj.getString("code").equals("-1")) {
                                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(LoginActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();

                                        JSONObject data = obj.getJSONObject("data");

                                        Intent intent = new Intent(getApplicationContext(), UserHomePage.class);
                                        intent.putExtra("user_id", data.getString("id"));
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
                        MyData.put("password", password_txt);
                        return MyData;
                    }
                };
                requestQueue.add(jsonRequest);


            }
        });
    }
}