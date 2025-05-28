package com.example.contactexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ViewContactActivity extends AppCompatActivity {

    private TextView nameDisplay;
    private TextView ageDisplay;
    private TextView schoolDisplay;
    private TextView nationalityDisplay;
    private TextView genderDisplay;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_contact);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setSupportActionBar(findViewById(R.id.toolbar3));
        getSupportActionBar().setTitle("View Contact");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameDisplay = findViewById(R.id.nameDisplay);
        ageDisplay = findViewById(R.id.ageDisplay);
        schoolDisplay = findViewById(R.id.schoolDisplay);
        nationalityDisplay = findViewById(R.id.nationalityDisplay);
        genderDisplay = findViewById(R.id.genderDisplay);

        contact = (Contact) getIntent().getSerializableExtra("contact");
        nameDisplay.setText(getString(R.string.name_display, contact.getName()));
        ageDisplay.setText(getString(R.string.age_display, contact.getAge()));

        StringBuilder schools = new StringBuilder();
        for(School school : contact.getSchools()) {
            schools.append(school.getName().stripTrailing().stripLeading()).append(", ");
        }
        schools.replace(schools.length() - 2, schools.length(), "");
        schoolDisplay.setText(getString(R.string.school_display, schools.toString()));

        nationalityDisplay.setText(getString(R.string.nationality_display, contact.getNationality()));
        genderDisplay.setText(getString(R.string.gender_display, contact.getGender()));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if(item.getItemId() == R.id.deleteContact) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("contact", contact);
            setResult(RESULT_OK, returnIntent);
            finish();
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_contact_menubar, menu);
        return true;
    }
}