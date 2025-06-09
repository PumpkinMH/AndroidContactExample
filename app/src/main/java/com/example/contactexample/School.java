package com.example.contactexample;

import java.io.Serializable;

public class School implements Serializable {
    private final String name;
    private final Nationality location;
    private long Id;

    public School(String name, Nationality location) {
        if(name == null) {
            throw new IllegalArgumentException("School name cannot be null");
        }
        if(location == null) {
            throw new IllegalArgumentException("School location cannot be null");
        }

        this.name = name;
        this.location = location;
    }

    public School(String name, Nationality location, long Id) {
        this(name, location);
        this.Id = Id;
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

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public Nationality getLocation() {
        return location;
    }
}
