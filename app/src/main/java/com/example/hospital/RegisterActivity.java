package com.example.hospital;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText fname, lname, phone, nid, email, password;
    String fname_txt, lname_txt, phone_txt, nid_txt, email_txt, password_txt;

    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fname = (EditText) findViewById(R.id.editText_first_name);
        lname = (EditText) findViewById(R.id.editText_last_name);
        phone = (EditText) findViewById(R.id.editText_phone);
        nid = (EditText) findViewById(R.id.editText_national_id);
        email = (EditText) findViewById(R.id.editText_email);
        password = (EditText) findViewById(R.id.editText_password);

        register = (Button) findViewById(R.id.button_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fname_txt = fname.getText().toString();
                lname_txt = lname.getText().toString();
                phone_txt = phone.getText().toString();
                nid_txt = nid.getText().toString();
                email_txt = email.getText().toString();
                password_txt = password.getText().toString();

                boolean flag = true;
                if (fname_txt.isEmpty()) {
                    fname.setError("This field is required");
                    flag = false;
                }
                if (lname_txt.isEmpty()) {
                    lname.setError("This field is required");
                    flag = false;
                }
                if (password_txt.isEmpty()) {
                    password.setError("This field is required");
                    flag = false;
                }
                if (!isValidPassword(password_txt)) {
                    password.setError("invalid password");
                    flag = false;
                }
                if (email_txt.isEmpty()) {
                    email.setError("This field is required");
                    flag = false;
                }
                if (phone_txt.isEmpty()) {
                    phone.setError("This field is required");
                    flag = false;
                }
                if (nid_txt.isEmpty()) {
                    nid.setError("This field is required");
                    flag = false;
                }

                if (!flag) {
                    return;
                }

                final String REQUEST_URL = Var.Signup_url;

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest jsonRequest = new StringRequest(
                        Request.Method.POST,
                        REQUEST_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("response", response);
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    JSONObject data;
                                    Log.d("response", obj.getString("code"));
                                    if (obj.getString("code").equals("-1")) {
                                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                                        data = obj.getJSONObject("data");
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

                        MyData.put("fname", fname_txt);
                        MyData.put("lname", lname_txt);
                        MyData.put("phone", phone_txt);
                        MyData.put("nid", nid_txt);
                        MyData.put("email", email_txt);
                        MyData.put("password", password_txt);

                        return MyData;
                    }
                };
                requestQueue.add(jsonRequest);
            }
        });


    }


    private boolean isValidPassword(String password_txt) {
        Pattern PASSWORD_PATTERN
                = Pattern.compile(
                "[a-zA-Z0-9]{6,24}");

        return !TextUtils.isEmpty(password_txt) && PASSWORD_PATTERN.matcher(password_txt).matches();
    }

}