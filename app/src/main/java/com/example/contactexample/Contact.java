package com.example.contactexample;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Contact implements Serializable {
    private final String name;
    private final String age;
    private final School[] schools;
    private final Nationality nationality;
    private final Gender gender;
    private long Id;

    public Contact(String name, String age, School[] schools, Nationality nationality, Gender gender) {
        if(name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;

        try {
            Integer.parseInt(age);
            this.age = age;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Age must be a number");
        }

        if(schools == null || schools.length == 0 || schools[0].getName().isEmpty()) {
            throw new IllegalArgumentException("Schools cannot be empty");
        }
        this.schools = schools;

        if(nationality == null) {
            throw new IllegalArgumentException("Nationality cannot be null");
        }
        this.nationality = nationality;

        if(gender == null) {
            throw new IllegalArgumentException("Gender cannot be null");
        }
        this.gender = gender;
        this.Id = -1;
    }
    public Contact(String name, String age, School[] schools, Nationality nationality, Gender gender, long Id) {
        this(name, age, schools, nationality, gender);
        this.Id = Id;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public School[] getSchools() {
        return schools;
    }

    public String[] getSchoolNames() {
        String[] schoolNames = new String[schools.length];
        for(int i = 0; i < schools.length; i++) {
            schoolNames[i] = schools[i].getName();
        }
        return schoolNames;
    }

    public String getSchoolNamesString() {
        StringBuilder schoolNames = new StringBuilder();
        for(int i = 0; i < schools.length; i++) {
            schoolNames.append(schools[i].getName()).append("|");
        }

        return schoolNames.toString();
    }

    public Nationality getNationality() {
        return nationality;
    }

    public Gender getGender() {
        return gender;
    }

    public long getId() {
        return Id;
    }

    public void setId(long Id) {
        if(this.Id == -1) {
            this.Id = Id;
        } else {
            throw new IllegalStateException("Id already set");
        }
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Contact contact = (Contact) obj;
        if(this.Id == -1 || contact.Id == -1) {
            return false;
        }
        return this.Id == contact.Id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id);
    }
}
