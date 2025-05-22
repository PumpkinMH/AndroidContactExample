package com.example.contactexample;

import java.io.Serializable;
import java.util.ArrayList;

public class Contact implements Serializable {
    private final String name;
    private final String age;
    private final String[] schools;
    private final Nationality nationality;
    private final Gender gender;

    public Contact(String name, String age, String[] schools, Nationality nationality, Gender gender) {
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

        if(schools == null || schools.length == 0 || schools[0].isEmpty()) {
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
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String[] getSchools() {
        return schools;
    }

    public Nationality getNationality() {
        return nationality;
    }

    public Gender getGender() {
        return gender;
    }

    @Override
    public String toString() {
        return name;
    }
}
