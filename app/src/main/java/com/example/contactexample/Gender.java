package com.example.contactexample;

public enum Gender {
    MALE("Male"),FEMALE("Female"),OTHER("Other");
    private final String text;
    private Gender(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
