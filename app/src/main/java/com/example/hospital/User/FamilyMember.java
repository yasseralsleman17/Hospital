package com.example.hospital.User;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.hospital.R;
import com.example.hospital.Var;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FamilyMember extends AppCompatActivity {

    Button button_add_member;

    LinearLayout family_member;

    String user_id;
    ImageButton imageButton;

    @Override
    protected void onRestart() {
        super.onRestart();

        get_family_member();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_member);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_id = extras.getString("user_id");
        }

        imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        button_add_member = findViewById(R.id.button_add_member);

        family_member = findViewById(R.id.family_member);

        get_family_member();


        button_add_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddNewMember.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

    }

    private void get_family_member() {
        family_member.removeAllViews();
        final String REQUEST_URL = Var.get_family_member;

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
                                view_family(obj.getJSONObject(i));
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


    private void view_family(JSONObject jsonObject) throws JSONException {
        JSONObject data = jsonObject.getJSONObject("data");
        String id = data.getString("id");

        final String[] fname_txt = new String[1];
        final String[] lname_txt = new String[1];
        final String[] nid_txt = new String[1];
        final String[] date_txt = new String[1];

        fname_txt[0] = data.getString("first_name");
        lname_txt[0] = data.getString("last_name");
        nid_txt[0] = data.getString("national_id");
        date_txt[0] = data.getString("date_of_birth");

        String name_txt = data.getString("first_name") + " " + data.getString("last_name");

        View view = getLayoutInflater().inflate(R.layout.family_member_card, null);
        TextView name = view.findViewById(R.id.name);
        TextView date = view.findViewById(R.id.date);

        name.setText(name_txt);
        date.setText(date_txt[0]);

        Button button_edit = view.findViewById(R.id.button_edit);
        Button button_delete = view.findViewById(R.id.button_delete);

        button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.edit_family_member, null);

                final Dialog dialog = new Dialog(FamilyMember.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);


                EditText fname, lname, nid;
                TextView date;

                fname = view.findViewById(R.id.editText_first_name);
                lname = view.findViewById(R.id.editText_last_name);
                date = view.findViewById(R.id.editText_date_of_birth);
                nid = view.findViewById(R.id.editText_national_id);

                fname.setText(fname_txt[0]);
                lname.setText(lname_txt[0]);
                date.setText(date_txt[0]);
                nid.setText(nid_txt[0]);


                ImageView date_icon = view.findViewById(R.id.date_icon);

                date_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                DatePickerDialog datePickerDialog = new DatePickerDialog(
                                        FamilyMember.this,
                                        (datePicker, year, month, dayOfMonth) -> {
                                            date_txt[0] = dayOfMonth + "/" +( month+1) + "/" + year;
                                            date.setText(date_txt[0]);

                                        },
                                        Calendar.getInstance().get(Calendar.YEAR),
                                        Calendar.getInstance().get(Calendar.MONTH),
                                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));


                                datePickerDialog.show();
                            }





                    }
                });
                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            DatePickerDialog datePickerDialog = new DatePickerDialog(
                                    FamilyMember.this,
                                    (datePicker, year, month, dayOfMonth) -> {
                                        date_txt[0] = dayOfMonth + "/" +( month+1) + "/" + year;
                                        date.setText(date_txt[0]);

                                    },
                                    Calendar.getInstance().get(Calendar.YEAR),
                                    Calendar.getInstance().get(Calendar.MONTH),
                                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH));


                            datePickerDialog.show();
                        }



                    }
                });

                Button save = view.findViewById(R.id.button_save);

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        fname_txt[0] = fname.getText().toString();
                        lname_txt[0] = lname.getText().toString();
                        date_txt[0] = date.getText().toString();
                        nid_txt[0] = nid.getText().toString();




                        boolean flag = true;
                        if (fname_txt[0].isEmpty()) {
                            fname.setError("This field is required");
                            flag = false;
                        }

                        if (lname_txt[0].isEmpty()) {
                            lname.setError("This field is required");
                            flag = false;
                        }

                        if (nid_txt[0].isEmpty()) {
                            nid.setError("This field is required");
                            flag = false;
                        }

                        if (!flag) {
                            return;
                        }


                        final String REQUEST_URL = Var.update_family_member;


                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        StringRequest jsonRequest = new StringRequest(
                                Request.Method.POST,
                                REQUEST_URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject obj = new JSONObject(response);

                                            Toast.makeText(FamilyMember.this, obj.getString("message"), Toast.LENGTH_SHORT).show();

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

                                MyData.put("id", id);
                                MyData.put("fname", fname_txt[0]);
                                MyData.put("lname", lname_txt[0]);
                                MyData.put("date_of_birth", date_txt[0]);
                                MyData.put("nid", nid_txt[0]);



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

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String REQUEST_URL = Var.delete_family_member;

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

                                    if (obj.getString("code").equals("1")) {
                                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();

                                    finish();
                                    startActivity(getIntent());
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

                        MyData.put("id", id);


                        return MyData;
                    }
                };
                requestQueue.add(jsonRequest);


            }
        });



        family_member.addView(view);


    }


}