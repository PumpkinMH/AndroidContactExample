package com.example.contactexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class AddCourseActivity extends AppCompatActivity {

    private EditText courseFullName;
    private EditText courseShortName;
    private EditText courseCreditHours;
    private Spinner courseSchoolSpinner;
    private ArrayList<School> schools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_course);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        schools = (ArrayList<School>) intent.getSerializableExtra("schools");

        setSupportActionBar(findViewById(R.id.addCourseToolbar));
        getSupportActionBar().setTitle("Add Course");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        courseFullName = findViewById(R.id.courseFullName);
        courseShortName = findViewById(R.id.courseShortName);
        courseCreditHours = findViewById(R.id.courseCreditHours);
        courseSchoolSpinner = findViewById(R.id.courseSchoolSpinner);

        ArrayAdapter<School> schoolArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, schools);
        schoolArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_course_menubar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.addCourse) {
            createCourse();
            return true;
        } else if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }

    private void createCourse() {
        String fullName = courseFullName.getText().toString();
        String shortName = courseShortName.getText().toString();
        int creditHours = -1;
        try {
            creditHours = Integer.parseInt(courseCreditHours.getText().toString());
            if(creditHours < 1) {
                Toast.makeText(this, "Credit hours must be a positive integer", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Credit hours must be a positive integer", Toast.LENGTH_SHORT).show();
        }

        School school = (School) courseSchoolSpinner.getSelectedItem();
        long schoolId = school.getId();

        Course course;
        try {
            course = new Course(shortName, fullName, creditHours, schoolId);
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "Invalid course information", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Course Created", Toast.LENGTH_SHORT).show();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("course", course);

        // create.
        //save to DB
        // then locally.
        setResult(RESULT_OK, returnIntent);
        finish();
    }

}