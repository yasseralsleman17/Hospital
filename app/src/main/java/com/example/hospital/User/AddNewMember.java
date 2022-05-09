package com.example.hospital.User;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddNewMember extends AppCompatActivity {


    EditText editText_first_name, editText_last_name, editText_national_id;
    Button button_add_member;
    TextInputLayout textInputLayout8;
    String user_id;
    ImageButton imageButton;
    TextView editText_date_of_birth;
    ImageView date_icon;
    String  date="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_new_member);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_id = extras.getString("user_id");
        }

        button_add_member = findViewById(R.id.button_add_member);

        editText_first_name = findViewById(R.id.editText_first_name);
        editText_last_name = findViewById(R.id.editText_last_name);
        editText_national_id = findViewById(R.id.editText_national_id);
        editText_date_of_birth = findViewById(R.id.editText_date_of_birth);
        date_icon = findViewById(R.id.date_icon);

        date_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        editText_date_of_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        button_add_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editText_first_name_txt, editText_last_name_txt, editText_national_id_txt, editText_date_of_birth_txt;

                editText_first_name_txt = editText_first_name.getText().toString().trim();
                editText_last_name_txt = editText_last_name.getText().toString().trim();
                editText_national_id_txt = editText_national_id.getText().toString().trim();
                editText_date_of_birth_txt = editText_date_of_birth.getText().toString().trim();

                boolean flag = true;
                if (editText_first_name_txt.isEmpty()) {
                    editText_first_name.setError("This field is required");
                    flag = false;
                }
                if (editText_last_name_txt.isEmpty()) {
                    editText_last_name.setError("This field is required");
                    flag = false;
                }
                if (editText_national_id_txt.isEmpty()) {
                    editText_national_id.setError("This field is required");
                    flag = false;
                }

                if (date.equals("")) {
                    Toast.makeText(AddNewMember.this, "You should add date of birth", Toast.LENGTH_SHORT).show();
                    flag = false;
                }

                if (!flag) {
                    return;
                }

                final String REQUEST_URL = Var.add_family_member;

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
                        MyData.put("user_id", user_id);
                        MyData.put("first_name", editText_first_name_txt);
                        MyData.put("last_name", editText_last_name_txt);
                        MyData.put("national_id", editText_national_id_txt);
                        MyData.put("date_of_birth", editText_date_of_birth_txt);
                        return MyData;
                    }
                };
                requestQueue.add(jsonRequest);
            }
        });


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
         date = dayOfMonth + "/" +( month+1) + "/" + year;
        editText_date_of_birth.setText(date);
    }



}