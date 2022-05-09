package com.example.hospital.User;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hospital.MainActivity;
import com.example.hospital.R;
import com.example.hospital.Var;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

public class UserHomePage extends AppCompatActivity {


    ImageView imageView_doctor_list, imageView_clinic_list, imageView_book_app, imageView_view_app, imageView_list_member, imageView_profile;
    ListView simpleList;
    ImageView imageView_notification;
    LinearLayout linearLayout4;
    String user_id;
    Timer time = new Timer();
    String[] notifications = {};
    ArrayList<String> notifications_arrayList = new ArrayList<String>();

    Button button_logout;
    String fname_txt, lname_txt, phone_txt, nid_txt, email_txt, password_txt;

    @Override
    protected void onStop() {

        time.cancel();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_page);

        simpleList = findViewById(R.id.listview_item);
        linearLayout4 = findViewById(R.id.linearLayout4);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_id = extras.getString("user_id");
        }

        imageView_notification = findViewById(R.id.imageView_menu);
        imageView_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout4.removeView(imageView_notification);

                imageView_notification.setImageLevel(R.drawable.ic_baseline_notifications_24);
                linearLayout4.addView(imageView_notification);

                simpleList.setVisibility(View.VISIBLE);
                imageView_notification.setVisibility(View.INVISIBLE);
            }
        });

        imageView_doctor_list = findViewById(R.id.imageView_doctor_list);
        imageView_clinic_list = findViewById(R.id.imageView_clinic_list);
        imageView_book_app = findViewById(R.id.imageView_book_app);
        imageView_view_app = findViewById(R.id.imageView_view_app);
        imageView_profile = findViewById(R.id.imageView_profile);
        imageView_list_member = findViewById(R.id.imageView_list_member);

        getdata();

        imageView_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.edit_profile, null);

                final Dialog dialog = new Dialog(UserHomePage.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);


                EditText fname, lname, phone, nid, password, email;

                fname = view.findViewById(R.id.editText_first_name);
                lname = view.findViewById(R.id.editText_last_name);
                phone = view.findViewById(R.id.editText_phone);
                nid = view.findViewById(R.id.editText_national_id);
                password = view.findViewById(R.id.editText_password);
                email = view.findViewById(R.id.editText_email);

                fname.setText(fname_txt);
                lname.setText(lname_txt);
                phone.setText(phone_txt);
                nid.setText(nid_txt);
                password.setText(password_txt);
                email.setText(email_txt);


                Button save = view.findViewById(R.id.button_save);

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        fname_txt = fname.getText().toString();
                        lname_txt = lname.getText().toString();
                        phone_txt = phone.getText().toString();
                        nid_txt = nid.getText().toString();
                        password_txt = password.getText().toString();
                        email_txt = email.getText().toString();

                        boolean flag = true;
                        if (fname_txt.isEmpty()) {
                            fname.setError("This field is required");
                            flag = false;
                        }  if (email_txt.isEmpty()) {
                            email.setError("This field is required");
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


                        final String REQUEST_URL = Var.update_user_profile;


                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        StringRequest jsonRequest = new StringRequest(
                                Request.Method.POST,
                                REQUEST_URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject obj = new JSONObject(response);

                                            Toast.makeText(UserHomePage.this, obj.getString("message"), Toast.LENGTH_SHORT).show();

                                            dialog.dismiss();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    }
                                },

                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }

                                }) {
                            @Override
                            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                                int mStatusCode = response.statusCode;
                                Log.d("mStatusCode", String.valueOf(mStatusCode));


                                return super.parseNetworkResponse(response);

                            }

                            protected Map<String, String> getParams() {
                                Map<String, String> MyData = new HashMap<String, String>();

                                MyData.put("user_id", user_id);
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


                dialog.setContentView(view);

                TextView cancel = dialog.findViewById(R.id.cancel);


                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                    }
                });


                dialog.create();
                dialog.show();

            }
        });


        button_logout = findViewById(R.id.button_logout);

        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


        imageView_doctor_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DoctorList.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        imageView_clinic_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ClinicList.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);

            }
        });

        imageView_book_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BookAppointment.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);

            }
        });

        imageView_view_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewAppointment.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);

            }
        });

        imageView_list_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FamilyMember.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);

            }
        });


        getAllNotification();


    }

    private void getdata() {

        final String REQUEST_URL = Var.get_user_info;


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonRequest = new StringRequest(
                Request.Method.POST,
                REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);

                            if (obj.getString("code").equals("1")) {

                                JSONObject data = obj.getJSONObject("data");


                                fname_txt = data.getString("fname");
                                lname_txt = data.getString("lname");
                                phone_txt = data.getString("phone");
                                nid_txt = data.getString("nid");
                                email_txt = data.getString("email");
                                password_txt = data.getString("password");

                                Log.d("test ", obj.getString("code"));
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

                }) {
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


    private void getAllNotification() {


        final String REQUEST_URL = Var.get_notification;


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonRequest = new StringRequest(
                Request.Method.POST,
                REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray obj = new JSONArray(response);
                            Log.d("response", String.valueOf(obj));

                            for (int i = 0; i < obj.length(); i++) {

                                JSONObject jsondata;

                                jsondata = obj.getJSONObject(i);
                                notifications_arrayList.add(jsondata.getString("notification_text"));

                            }

                            notifications = new String[notifications_arrayList.size()];
                            for (int i = 0; i < notifications_arrayList.size(); i++) {

                                notifications[i] = notifications_arrayList.get(i);
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_view, R.id.textView, notifications);
                            simpleList.setAdapter(arrayAdapter);

                            time.scheduleAtFixedRate(new TimerTask() {
                                @Override
                                public void run() {
                                    getNewNotification();
                                }
                            }, 0, 5000);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                }) {
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

    private void getNewNotification() {

        final String REQUEST_URL = Var.get_new_notification;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonRequest = new StringRequest(
                Request.Method.POST,
                REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray obj = new JSONArray(response);
                            Log.d("response", String.valueOf(obj));

                            for (int i = 0; i < obj.length(); i++) {
                                linearLayout4.removeView(imageView_notification);

                                imageView_notification.setImageLevel(R.drawable.ic_baseline_notifications_active_24);
                                linearLayout4.addView(imageView_notification);
                                JSONObject jsondata;

                                jsondata = obj.getJSONObject(i);
                                notifications_arrayList.add(jsondata.getString("notification_text"));

                            }


                            notifications = new String[notifications_arrayList.size()];
                            for (int i = 0; i < notifications_arrayList.size(); i++) {
                                notifications[i] = notifications_arrayList.get(i);
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_view, R.id.textView, notifications);
                            simpleList.setAdapter(arrayAdapter);

                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }


                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                }) {
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

    private boolean isValidPassword(String password_txt) {
        Pattern PASSWORD_PATTERN
                = Pattern.compile(
                "[a-zA-Z0-9]{6,24}");

        return !TextUtils.isEmpty(password_txt) && PASSWORD_PATTERN.matcher(password_txt).matches();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        simpleList.setVisibility(View.INVISIBLE);
        imageView_notification.setVisibility(View.VISIBLE);
        return super.onTouchEvent(event);
    }
}