package com.example.contactexample;

import java.io.Serializable;

public class School implements Serializable {
    private final String name;

    public School(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
