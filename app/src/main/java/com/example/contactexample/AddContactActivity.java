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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class AddContactActivity extends AppCompatActivity {

    private EditText contactName;
    private EditText contactAge;
    private Spinner contactSchools;
    private Spinner contactNationality;
    private RadioGroup contactGender;
    private ArrayList<School> schools;
    private ArrayList<Course> courses;
    private long[] selectedCoursesIds;

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

        Intent intent = getIntent();
        schools = (ArrayList<School>) intent.getSerializableExtra("schools");
        courses = (ArrayList<Course>) intent.getSerializableExtra("courses");

        contactName = findViewById(R.id.contactName);
        contactAge = findViewById(R.id.contactAge);
        contactSchools = findViewById(R.id.contactSchools);
        contactNationality = findViewById(R.id.contactNationality);
        contactGender = findViewById(R.id.contactGender);

        selectedCoursesIds = new long[0];

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("Add Contact");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayAdapter<Nationality> nationalityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Nationality.values());
        nationalityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        contactNationality.setAdapter(nationalityAdapter);

        ArrayAdapter<School> schoolArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, schools);
        schoolArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        contactSchools.setAdapter(schoolArrayAdapter);
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
        } else if(item.getItemId() == R.id.selectCourse) {
            if(contactSchools.getSelectedItem() == null) {
                Toast.makeText(this, "Please select a school first", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                School school = (School) contactSchools.getSelectedItem();
                long schoolId = school.getId();
                ArrayList<Course> availableCourses = new ArrayList<Course>();
                for(Course course : courses) {
                    if(course.getSchoolId() == schoolId) {
                        availableCourses.add(course);
                    }
                }

                if(availableCourses.isEmpty()) {
                    Toast.makeText(this, "No courses available for this school", Toast.LENGTH_SHORT).show();
                    return true;
                }

                CharSequence[] courseNames = new CharSequence[availableCourses.size()];
                for(int i = 0; i < availableCourses.size(); i++) {
                    courseNames[i] = availableCourses.get(i).getShortName();
                }

                boolean[] checkedCourses = new boolean[availableCourses.size()];
                for(int i = 0; i < availableCourses.size(); i++) {
                    checkedCourses[i] = false;
                    for(long courseId : selectedCoursesIds) {
                        if(availableCourses.get(i).getCourseId() == courseId) {
                            checkedCourses[i] = true;
                            break;
                        }
                    }
                }

                ArrayList<Course> selectedCourses = new ArrayList<Course>();
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle("Select Courses")
                        .setMultiChoiceItems(courseNames, checkedCourses, (dialog, which, isChecked) -> {
                            if(isChecked) {
                                selectedCourses.add(availableCourses.get(which));
                            } else {
                                selectedCourses.remove(availableCourses.get(which));
                            }
                        })
                        .setPositiveButton("OK", (dialog, which) -> {
                            if(!selectedCourses.isEmpty()) {
                                selectedCoursesIds = new long[selectedCourses.size()];
                                for(int i = 0; i < selectedCourses.size(); i++) {
                                    selectedCoursesIds[i] = selectedCourses.get(i).getCourseId();
                                }
                                contactSchools.setEnabled(false);
                            } else {
                                selectedCoursesIds = new long[0];
                                contactSchools.setEnabled(true);
                            }
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            return true;
        }
        else if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }

    private void createContact() {
        String name = contactName.getText().toString();
        String age = contactAge.getText().toString();

        // Modify this to allow multi selection
        School school = (School) contactSchools.getSelectedItem();
        long[] schoolsId = {school.getId()};
        long[] coursesId = selectedCoursesIds;

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
            contact = new Contact(name, age, nationality, gender, schoolsId, coursesId);
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, getString(R.string.contact_error), Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Contact Created", Toast.LENGTH_SHORT).show();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("contact", contact);

        // create.
        //save to DB
        // then locally.
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}