package com.example.contactexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ViewContactActivity extends AppCompatActivity {

    private TextView nameDisplay;
    private TextView ageDisplay;
    private TextView schoolDisplay;
    private TextView nationalityDisplay;
    private TextView genderDisplay;
    private TextView courseDisplay;
    private Contact contact;
    private ArrayList<School> schools;
    private ArrayList<Course> courses;
    private ActivityResultLauncher<Intent> startForEditResult;

    public static final int RESULT_CODE_DELETE = 101;
    public static final int RESULT_CODE_EDIT = 102;

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

        schools = (ArrayList<School>) getIntent().getSerializableExtra("schools");
        courses = (ArrayList<Course>) getIntent().getSerializableExtra("courses");

        setSupportActionBar(findViewById(R.id.toolbar3));
        getSupportActionBar().setTitle("View Contact");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameDisplay = findViewById(R.id.nameDisplay);
        ageDisplay = findViewById(R.id.ageDisplay);
        schoolDisplay = findViewById(R.id.schoolDisplay);
        nationalityDisplay = findViewById(R.id.nationalityDisplay);
        genderDisplay = findViewById(R.id.genderDisplay);
        courseDisplay = findViewById(R.id.courseDisplay);

        contact = (Contact) getIntent().getSerializableExtra("contact");

        updateLabels(contact);

        startForEditResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent intent = result.getData();
                Contact newContact = (Contact) intent.getSerializableExtra("contact");
                DBHandler db = new DBHandler(this);
                db.editContact(contact, newContact);
                newContact.setId(contact.getId());
                this.contact = newContact;
                updateLabels(contact);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("editContact", contact);
                setResult(RESULT_CODE_EDIT, returnIntent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if(item.getItemId() == R.id.deleteContact) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("deleteContact", contact);
            setResult(RESULT_CODE_DELETE, returnIntent);
            finish();
            return true;
        } else if(item.getItemId() == R.id.editContact) {
            Intent intent = new Intent(this, EditContactActivity.class);
            intent.putExtra("contact", contact);
            intent.putExtra("schools", schools);
            startForEditResult.launch(intent);
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

    private void updateLabels(Contact sourceContact) {
        nameDisplay.setText(getString(R.string.name_display, sourceContact.getName()));
        ageDisplay.setText(getString(R.string.age_display, sourceContact.getAge()));

        StringBuilder schoolsString = new StringBuilder();
        for(School school : this.schools) {
            for(long schoolId : sourceContact.getSchoolIds()) {
                if(schoolId == school.getId()) {
                    schoolsString.append(school.getName()).append(", ");
                }
            }
        }
        schoolsString.replace(schoolsString.length() - 2, schoolsString.length(), "");
        schoolDisplay.setText(getString(R.string.school_display, schoolsString.toString()));

        nationalityDisplay.setText(getString(R.string.nationality_display, sourceContact.getNationality()));
        genderDisplay.setText(getString(R.string.gender_display, sourceContact.getGender()));

        StringBuilder coursesString = new StringBuilder();
        for(Course course : this.courses) {
            for(long courseId : sourceContact.getCourseIds()) {
                if(courseId == course.getCourseId()) {
                    coursesString.append(course.getShortName()).append(", ");
                }
            }
        }
        coursesString.replace(coursesString.length() - 2, coursesString.length(), "");
        courseDisplay.setText(getString(R.string.course_display, coursesString.toString()));
    }
}