package com.example.hospital.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.hospital.R;

public class ClinicList extends AppCompatActivity {

    ImageView ortho, obgyn, pulmonary, dermatology, dental, pediatric, cardiolgy, neuology;

    String user_id;
    ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_list);


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


        ortho = findViewById(R.id.ortho);
        ortho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewClinic.class);
                intent.putExtra("clinic", "Orthotic");
                startActivity(intent);
            }
        });


        obgyn = findViewById(R.id.obgyn);
        obgyn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewClinic.class);
                intent.putExtra("clinic", "OB_gyn");
                startActivity(intent);
            }
        });

        pulmonary = findViewById(R.id.pulmonary);
        pulmonary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewClinic.class);
                intent.putExtra("clinic", "Pulmonary");
                startActivity(intent);
            }
        });
        dermatology = findViewById(R.id.dermatology);
        dermatology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewClinic.class);
                intent.putExtra("clinic", "Dermatology");
                startActivity(intent);
            }
        });
        dental = findViewById(R.id.dental);
        dental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewClinic.class);
                intent.putExtra("clinic", "Dental");
                startActivity(intent);
            }
        });
        pediatric = findViewById(R.id.pediatric);
        pediatric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewClinic.class);
                intent.putExtra("clinic", "Pediatric");
                startActivity(intent);
            }
        });
        cardiolgy = findViewById(R.id.cardiolgy);
        cardiolgy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewClinic.class);
                intent.putExtra("clinic", "Cardiology");
                startActivity(intent);
            }
        });
        neuology = findViewById(R.id.neuology);
        neuology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewClinic.class);
                intent.putExtra("clinic", "Neurology");
                startActivity(intent);
            }
        });

    }
}