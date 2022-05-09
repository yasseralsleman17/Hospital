package com.example.hospital.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.hospital.MainActivity;
import com.example.hospital.R;

public class AdminHomePage extends AppCompatActivity {


    ImageView doctor_add, manage_appointment,delete_user,doctor_manage;
Button button_logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);


        doctor_add = findViewById(R.id.doctor_add);

        doctor_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddDoctor.class));
            }
        });
        doctor_manage = findViewById(R.id.doctor_manage);

        doctor_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ManageDoctor.class));
            }
        });
        manage_appointment = findViewById(R.id.manage_appointment);

        manage_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ManageAppointment.class));
            }
        });
        delete_user = findViewById(R.id.delete_user);

        delete_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DeleteUser.class));
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

    }
}