package com.example.contactexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class EditContactActivity extends AppCompatActivity {

    private EditText contactName;
    private EditText contactAge;
    private Spinner contactSchools;
    private Spinner contactNationality;
    private RadioGroup contactGender;
    private Contact contact;
    private ArrayList<School> schools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_contact);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        schools = (ArrayList<School>) getIntent().getSerializableExtra("schools");

        contactName = findViewById(R.id.contactName);
        contactAge = findViewById(R.id.contactAge);
        contactSchools = findViewById(R.id.contactSchools);
        contactNationality = findViewById(R.id.contactNationality);
        contactGender = findViewById(R.id.contactGender);

        contact = (Contact) getIntent().getSerializableExtra("contact");
        contactName.setText(contact.getName());
        contactAge.setText(contact.getAge());

        contactNationality.setSelection(contact.getNationality().ordinal());
        Gender contactGender = contact.getGender();
        if(contactGender == Gender.MALE) {
            this.contactGender.check(R.id.contactGenderMale);
        } else if(contactGender == Gender.FEMALE) {
            this.contactGender.check(R.id.contactGenderFemale);
        } else if(contactGender == Gender.OTHER) {
            this.contactGender.check(R.id.contactGenderOther);
        }

        setSupportActionBar(findViewById(R.id.toolbar4));
        getSupportActionBar().setTitle("Edit Contact");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayAdapter<Nationality> nationalityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Nationality.values());
        nationalityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        contactNationality.setAdapter(nationalityAdapter);

        ArrayAdapter<School> schoolAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, schools);
        schoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        contactSchools.setAdapter(schoolAdapter);

        // Modify to enable more schools
        for(int i = 0; i < schools.size(); i++) {
            if (schools.get(i).getId() == contact.getSchoolIds()[0]) {
                contactSchools.setSelection(i);
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_contact_menubar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.saveContact) {
            editContact();
            return true;
        } else if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }

    private void editContact() {
        String name = contactName.getText().toString();
        String age = contactAge.getText().toString();

        // Modify to add more schools
        School school = (School) contactSchools.getSelectedItem();
        long[] schoolsArray = {school.getId()};

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

        Contact newContact;
        try {
            newContact = new Contact(name, age, nationality, gender, schoolsArray);
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, getString(R.string.contact_error), Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Contact Edited", Toast.LENGTH_SHORT).show();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("contact", newContact);

        // create.
        //save to DB
        // then locally.
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}