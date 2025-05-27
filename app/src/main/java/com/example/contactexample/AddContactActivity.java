package com.example.contactexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddContactActivity extends AppCompatActivity {

    private EditText contactName;
    private EditText contactAge;
    private EditText contactSchools;
    private Spinner contactNationality;
    private RadioGroup contactGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_contact);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        contactName = findViewById(R.id.contactName);
        contactAge = findViewById(R.id.contactAge);
        contactSchools = findViewById(R.id.contactSchools);
        contactNationality = findViewById(R.id.contactNationality);
        contactGender = findViewById(R.id.contactGender);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("Add Contact");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayAdapter<Nationality> nationalityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Nationality.values());
        nationalityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        contactNationality.setAdapter(nationalityAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_contact_menubar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.createContact) {
            createContact();
            return true;
        } else if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }

    private void createContact() {
        String name = contactName.getText().toString();
        String age = contactAge.getText().toString();

        String schools = contactSchools.getText().toString();
        String[] tempSchools = schools.split(";");
        School[] schoolsArray = new School[tempSchools.length];
        for (int i = 0; i < tempSchools.length; i++) {
            schoolsArray[i] = new School(tempSchools[i]);
        }

        Nationality nationality = (Nationality) contactNationality.getSelectedItem();

        Gender gender;
        int genderId = contactGender.getCheckedRadioButtonId();
        if(genderId == R.id.contactGenderMale) {
            gender = Gender.MALE;
        } else if(genderId == R.id.contactGenderFemale) {
            gender = Gender.FEMALE;
        } else if(genderId == R.id.contactGenderOther) {
            gender = Gender.OTHER;
        } else {
            Toast.makeText(this, getString(R.string.contact_error), Toast.LENGTH_SHORT).show();
            return;
        }

        Contact contact;
        try {
            contact = new Contact(name, age, schoolsArray, nationality, gender);
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, getString(R.string.contact_error), Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Contact Created", Toast.LENGTH_SHORT).show();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("contact", contact);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}