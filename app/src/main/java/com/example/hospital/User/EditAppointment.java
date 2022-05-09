package com.example.hospital.User;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditAppointment extends AppCompatActivity {


    Spinner clinic_spinner, doctor_spinner,time_spinner;
    TextView booking_date, booking_time, clinic_tv;
    String date = "", time = "";
    EditText patient_name;
    Button edit_booking, cancel_booking;
    String[] clinic_items = new String[]{"Pulmonary", "Dermatology", "Dental", "Pediatric", "Cardiology", "Neurology", "Orthotic", "OB_gyn"};


    ArrayList<String> doctor_arrayList = new ArrayList<String>();
    ArrayList<String> time_arrayList = new ArrayList<String>();
    String doctor = "", clinic = "";

    String appointment_id;
    ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_appointment);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            appointment_id = extras.getString("appointment_id");
        }

        imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        clinic_spinner = findViewById(R.id.clinic_spinner);
        clinic_spinner.setVisibility(View.INVISIBLE);
        doctor_spinner = findViewById(R.id.doctor_spinner);


        time_spinner = findViewById(R.id.time_spinner);
        time_spinner.setVisibility(View.INVISIBLE);

        booking_date = findViewById(R.id.booking_date);
        clinic_tv = findViewById(R.id.clinic_tv);
        booking_time = findViewById(R.id.booking_time);
        patient_name = findViewById(R.id.patient_name);
        edit_booking = findViewById(R.id.edit_booking);
        cancel_booking = findViewById(R.id.cancel_booking);
        clinic_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clinic_tv.setVisibility(View.INVISIBLE);
                clinic_spinner.setVisibility(View.VISIBLE);


                ArrayAdapter<String> clinic_spinner_adapter = new ArrayAdapter<>(EditAppointment.this, android.R.layout.simple_spinner_dropdown_item, clinic_items);

                clinic_spinner.setAdapter(clinic_spinner_adapter);
                clinic_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        clinic = clinic_items[position];

                        get_doctor_specialty(clinic);


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });


                ;


            }
        });

        booking_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        booking_time.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                booking_time.setVisibility(View.INVISIBLE);
                time_spinner.setVisibility(View.VISIBLE);
            }
        });
        get_appointment(appointment_id);


        edit_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String patient_name_txt = patient_name.getText().toString().trim();
                boolean flag = true;

                if (patient_name_txt.equals("")) {
                    patient_name.setError("Patient name required");
                    flag = false;
                }
                if (date.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Choose Date ", Toast.LENGTH_LONG).show();

                    flag = false;
                }
                if (time.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Choose Time", Toast.LENGTH_LONG).show();
                    flag = false;
                }
                if (doctor.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Choose Doctor ", Toast.LENGTH_LONG).show();

                    flag = false;
                }
                if (clinic.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Choose Clinic", Toast.LENGTH_LONG).show();
                    flag = false;
                }
                if (!flag) return;

                final String REQUEST_URL = Var.edit_appointment;
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

                                        finish();
                                        startActivity(getIntent());
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
                        MyData.put("appointment_id", appointment_id);
                        MyData.put("patient_name", patient_name_txt);
                        MyData.put("date", date);
                        MyData.put("time", time);
                        MyData.put("doctor", doctor);
                        MyData.put("clinic", clinic);

                        return MyData;
                    }
                };
                requestQueue.add(jsonRequest);


            }
        });

        cancel_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void get_appointment(String appointment_id) {

        final String REQUEST_URL = Var.get_appointment;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonRequest = new StringRequest(
                Request.Method.POST, REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response tt", response);
                        JSONObject obj = null;
                        String[] doctor_items;
                        try {
                            obj = new JSONObject(response);
                            JSONObject data = obj.getJSONObject("data");

                            date = data.getString("date");
                            time = data.getString("time");
                            patient_name.setText(data.getString("patient_name"));
                            booking_date.setText(date);
                            booking_time.setText(time);
                            clinic = data.getString("clinic");
                            clinic_tv.setText(clinic);
                            doctor = data.getString("doctor");
                            doctor_items = new String[1];
                            doctor_items[0] = doctor;
                            ArrayAdapter<String> doctor_spinner_adapter = new ArrayAdapter<>(EditAppointment.this, android.R.layout.simple_spinner_dropdown_item, doctor_items);
                            doctor_spinner.setAdapter(doctor_spinner_adapter);

                            get_available_time( doctor,  date);
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
                MyData.put("appointment_id", appointment_id);


                return MyData;
            }
        };
        requestQueue.add(jsonRequest);


    }


    private void showDatePickerDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (datePicker, i, i1, i2) -> onDateSet(datePicker, i, i1, i2),
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH));


            datePickerDialog.show();
        }
    }

    private void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date = dayOfMonth + "/" + (month+1) + "/" + year;

        booking_date.setText(date);


        get_available_time(doctor, date);

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
                            doctor_arrayList = new ArrayList<String>();
                            String[] doctor_items;
                            for (int i = 0; i < obj.length(); i++) {
                                JSONObject jsondata;

                                jsondata = obj.getJSONObject(i);
                                JSONObject data = new JSONObject(jsondata.getString("data"));
                                doctor_arrayList.add(data.getString("name"));
                            }
                            doctor_items = new String[doctor_arrayList.size()];
                            for (int i = 0; i < doctor_arrayList.size(); i++) {
                                doctor_items[i] = doctor_arrayList.get(i);
                            }
                            ArrayAdapter<String> doctor_spinner_adapter = new ArrayAdapter<>(EditAppointment.this, android.R.layout.simple_spinner_dropdown_item, doctor_items);
                            doctor_spinner.setAdapter(doctor_spinner_adapter);
                            doctor_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                    doctor = doctor_items[position];

                                    if(!date.equals(""))
                                    {
                                        get_available_time(doctor, date);


                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });


                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
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


    private void get_available_time(String doctor, String date) {

        final String REQUEST_URL = Var.get_available_time;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonRequest = new StringRequest(
                Request.Method.POST, REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            time_arrayList = new ArrayList<String>();
                            String[] time_items;

                            String s_time = obj.getString("start_time");
                            String f_time = obj.getString("end_time");

                            JSONArray a_time = obj.getJSONArray("time_a");
                            ArrayList<String> array_a_time = new ArrayList<String>();

                            for (int i = 0; i < a_time.length(); i++) {
                                array_a_time.add(a_time.getJSONObject(i).getString("time"));


                            }
                            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

                            java.util.Calendar c = java.util.Calendar.getInstance();
                            java.util.Calendar c2 = java.util.Calendar.getInstance();

                            try {
                                c.setTime(dateFormat.parse(s_time));
                                c2.setTime(dateFormat.parse(f_time));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            while (c2.after(c)) {

                                if (!array_a_time.contains(String.valueOf(c.getTime().getHours()) + ":" + String.valueOf(c.getTime().getMinutes()))) {
                                    time_arrayList.add(String.valueOf(c.getTime().getHours()) + ":" + String.valueOf(c.getTime().getMinutes()));
                                }
                                c.add(java.util.Calendar.MINUTE, 30);

                            }


                            time_items = new String[time_arrayList.size()];
                            for (int i = 0; i < time_arrayList.size(); i++) {
                                time_items[i] = time_arrayList.get(i);
                            }

                            ArrayAdapter<String> time_spinner_adapter = new ArrayAdapter<>(EditAppointment.this, android.R.layout.simple_spinner_dropdown_item, time_items);
                            time_spinner.setAdapter(time_spinner_adapter);
                            time_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                    time = time_items[position];

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });


                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
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
                MyData.put("doctor", doctor);
                MyData.put("date", date);


                return MyData;
            }
        };
        requestQueue.add(jsonRequest);


    }


}