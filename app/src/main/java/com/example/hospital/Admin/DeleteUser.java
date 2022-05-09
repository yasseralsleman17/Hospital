package com.example.hospital.Admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
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

public class DeleteUser extends AppCompatActivity {
    ImageButton imageButton;
    LinearLayout linear_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);



        imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        linear_user = findViewById(R.id.linear_user);


        get_all_user();


    }


    private void get_all_user() {

        final String REQUEST_URL = Var.get_all_user;

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
                                view_user(obj.getJSONObject(i));
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


                return MyData;
            }
        };
        requestQueue.add(jsonRequest);


    }


    private void view_user(JSONObject jsonObject) throws JSONException {

        JSONObject data = new JSONObject(jsonObject.getString("data"));

        String user_id = data.getString("id");
        String have_appointment = data.getString("have_appointment");
        String have_familly_member = data.getString("have_familly_member");

        View view = getLayoutInflater().inflate(R.layout.user_card, null);

        TextView name = view.findViewById(R.id.name );
        TextView email = view.findViewById(R.id.email);
        TextView phone = view.findViewById(R.id.phone);
        TextView nid = view.findViewById(R.id.nid);


        Button button_edit = view.findViewById(R.id.button_edit);
        Button button_delete = view.findViewById(R.id.button_delete);

        name.setText(data.getString("name"));
        email.setText(data.getString("email"));
        phone.setText(data.getString("phone"));
        nid.setText(data.getString("nid"));


        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder popDialog = new AlertDialog.Builder(DeleteUser.this);


                popDialog.setTitle(" delete user");
                if(have_appointment.equals("yes")|| have_familly_member.equals("yes")) {

                    popDialog.setMessage("Are you sure you want to delete this user? his appointment and family member will be deleted ?!");

                } else
                {
                    popDialog.setMessage("Are you sure you want to delete this user ?! he has no appointment or family member");
                }

                popDialog.setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                delete(user_id);
                                dialog.dismiss();
                            }
                        }).setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                popDialog.create();
                popDialog.show();

            }
        });

        linear_user.addView(view);


    }


    public void delete(String user_id) {
        final String REQUEST_URL = Var.admin_delete_user;
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
                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();

                          finish();
                          startActivity(getIntent());
                            }
                        } catch (JSONException e) {

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
}