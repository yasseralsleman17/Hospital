package com.example.hospital.Admin;

import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddDoctor extends AppCompatActivity {


    EditText doctor_name;
    ImageButton add_start_time, add_end_time, button_back;
    TextView start_time, end_time;
    Button button_add_doctor;

    String start_time_txt = "", end_time_txt = "", doctor_specialty_txt = "Pulmonary";

    Spinner clinic_spinner;


    String[] clinic_items = new String[]{"Pulmonary", "Dermatology", "Dental", "Pediatric", "Cardiology", "Neurology", "Orthotic", "OB_gyn"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);

        doctor_name = findViewById(R.id.doctor_name);
        add_start_time = findViewById(R.id.add_start_time);
        add_end_time = findViewById(R.id.add_end_time);
        start_time = findViewById(R.id.start_time);
        end_time = findViewById(R.id.end_time);
        button_add_doctor = findViewById(R.id.button_add_doctor);
        button_back = findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        clinic_spinner = findViewById(R.id.clinic_spinner);

        ArrayAdapter<String> clinic_spinner_adapter = new ArrayAdapter<>(AddDoctor.this, android.R.layout.simple_spinner_dropdown_item, clinic_items);

        clinic_spinner.setAdapter(clinic_spinner_adapter);
        clinic_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                doctor_specialty_txt = clinic_items[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        add_start_time.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                showTimePickerDialog("start");
            }
        });

        add_end_time.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                showTimePickerDialog("end");

            }
        });


        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        button_add_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String doctor_name_txt;
                doctor_name_txt = doctor_name.getText().toString().trim();

                boolean flag = true;
                if (doctor_name_txt.equals("")) {
                    doctor_name.setError("This field is required");
                    flag = false;
                }
                if (doctor_specialty_txt.equals("")) {
                    Toast.makeText(getApplicationContext(), "choose Clinic name ", Toast.LENGTH_LONG).show();
                    flag = false;
                }
                if (start_time_txt.equals("")) {
                    Toast.makeText(getApplicationContext(), "choose start working time ", Toast.LENGTH_LONG).show();

                    flag = false;
                }
                if (end_time_txt.equals("")) {
                    Toast.makeText(getApplicationContext(), "choose end working time", Toast.LENGTH_LONG).show();
                    flag = false;
                }

                if (!flag) return;


                final String REQUEST_URL = Var.add_doctor_url;

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
                                    Log.d("response", obj.getString("code"));
                                    if (obj.getString("code").equals("-1")) {
                                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {
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
                        MyData.put("name", doctor_name_txt);
                        MyData.put("specialty", doctor_specialty_txt);
                        MyData.put("start_time", start_time_txt);
                        MyData.put("end_time", end_time_txt);
                        return MyData;
                    }
                };
                requestQueue.add(jsonRequest);
            }
        });


    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showTimePickerDialog(String type) {
        TimePickerDialog TimePickerDialog = new TimePickerDialog(
                this,
                (timePicker, i, i1) -> onTimeSet(timePicker, i, i1, type)
                ,
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                DateFormat.is24HourFormat(getApplicationContext()));

        TimePickerDialog.show();
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute, String type) {

        if (type.equals("start")) {
            start_time_txt = hourOfDay + ":" + minute;
            start_time.setText(start_time_txt);
        } else {
            end_time_txt = hourOfDay + ":" + minute;
            end_time.setText(end_time_txt);
        }
    }


}