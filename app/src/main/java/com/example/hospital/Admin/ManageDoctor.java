package com.example.hospital.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
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

public class ManageDoctor extends AppCompatActivity {


    LinearLayout linear_doctor_list;
    ImageView category1, category2, category3, category4;

    int[] images = {R.drawable.ic_1, R.drawable.pulmonary, R.drawable.dermatology, R.drawable.dental,
            R.drawable.pediatric, R.drawable.cardiolgy, R.drawable.neuology, R.drawable.ortho, R.drawable.obgyn};

    String[] category = {"all_doctor", "Pulmonary", "Dermatology", "Dental", "Pediatric", "Cardiology", "Neurology", "Orthotic", "OB_gyn"};


    int count = 0;

    ImageButton right, left, imageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_doctor);


        linear_doctor_list = findViewById(R.id.linear_doctor_list);

        category1 = findViewById(R.id.category1);
        category2 = findViewById(R.id.category2);
        category3 = findViewById(R.id.category3);
        category4 = findViewById(R.id.category4);

        category1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        category2.setScaleType(ImageView.ScaleType.CENTER_CROP);
        category3.setScaleType(ImageView.ScaleType.CENTER_CROP);
        category4.setScaleType(ImageView.ScaleType.CENTER_CROP);

        imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        right = findViewById(R.id.right);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count < 5) {
                    count++;

                    category1.setImageResource(images[count]);
                    category1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (count == 0)
                                getalldoctor();
                            else
                                get_doctor_specialty(category[count]);

                        }
                    });

                    category2.setImageResource(images[count + 1]);
                    category2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            get_doctor_specialty(category[count + 1]);

                        }
                    });
                    category3.setImageResource(images[count + 2]);
                    category3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            get_doctor_specialty(category[count + 2]);

                        }
                    });
                    category4.setImageResource(images[count + 3]);
                    category4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            get_doctor_specialty(category[count + 3]);

                        }
                    });


                }

            }
        });
        left = findViewById(R.id.left);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count > 0) {
                    count--;


                    category1.setImageResource(images[count]);
                    category1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (count == 0)
                                getalldoctor();
                            else
                                get_doctor_specialty(category[count]);

                        }
                    });

                    category2.setImageResource(images[count + 1]);

                    category2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            get_doctor_specialty(category[count + 1]);

                        }
                    });
                    category3.setImageResource(images[count + 2]);

                    category3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            get_doctor_specialty(category[count + 2]);

                        }
                    });
                    category4.setImageResource(images[count + 3]);

                    category4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            get_doctor_specialty(category[count + 3]);

                        }
                    });


                }

            }


        });

        getalldoctor();


    }


    private void getalldoctor() {

        final String REQUEST_URL = Var.get_doctor_url;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonRequest = new StringRequest(
                Request.Method.POST,
                REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);

                        JSONArray obj = null;
                        try {
                            obj = new JSONArray(response);


                            linear_doctor_list.removeAllViews();

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


                return MyData;
            }
        };
        requestQueue.add(jsonRequest);


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
                            linear_doctor_list.removeAllViews();
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
        String id = data.getString("id");
        String have_appointment = data.getString("have_appointment");
        View view = getLayoutInflater().inflate(R.layout.doctor_list_card_manage, null);

        TextView doctor_name = view.findViewById(R.id.doctor_name);
        TextView doctor_specialty = view.findViewById(R.id.doctor_specialty);
        TextView time = view.findViewById(R.id.time);

        String name=data.getString("name");

        doctor_name.setText(name);
        doctor_specialty.setText(data.getString("specialty"));
        time.setText(data.getString("start_time") + "  " + data.getString("end_time"));

        Button button_delete = view.findViewById(R.id.button_delete);
        Button button_edit = view.findViewById(R.id.button_edit);

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final AlertDialog.Builder popDialog = new AlertDialog.Builder(ManageDoctor.this);
                popDialog.setTitle(" delete doctor");
                if (have_appointment.equals("yes")) {

                    popDialog.setMessage("Are you sure you want to delete this doctor ?! there are some appointment will be deleted ?!");

                } else
                {
                popDialog.setMessage("Are you sure you want to delete this doctor ?! he has no appointment");
                }
                popDialog.setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                delete(id,name);
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

        button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ManageDoctor.this, EditDoctor.class).putExtra("id", id));

            }
        });

        linear_doctor_list.addView(view);
    }


    public void delete(String id,String name ) {
        final String REQUEST_URL = Var.delete_doctor;
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
                MyData.put("id", id);
                MyData.put("name", name);


                return MyData;
            }
        };
        requestQueue.add(jsonRequest);
    }


}