package com.example.contactexample;

public enum Nationality {
    US("United States"),
    CANADA("Canada"),
    MEXICO("Mexico"),
    AUSTRALIA("Australia"),
    INDIA("India"),
    CHINA("China"),
    GERMANY("Germany"),
    FRANCE("France"),
    ITALY("Italy"),
    SPAIN("Spain"),
    BRAZIL("Brazil");

    private final String text;

    private Nationality(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
