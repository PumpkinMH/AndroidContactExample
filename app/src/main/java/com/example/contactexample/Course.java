package com.example.contactexample;

public class Course {
    private String shortName;
    private String fullName;
    private int credit;
    private long schoolId;
    private long courseId;

    public Course(String shortName, String fullName, int credit, long schoolId) {
        if(shortName == null || shortName.isEmpty()) {
            throw new IllegalArgumentException("Short name cannot be empty");
        }
        if(fullName == null || fullName.isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty");
        }
        if(credit < 1) {
            throw new IllegalArgumentException("Credit cannot be less than one");
        }
        if(schoolId < 0) {
            throw new IllegalArgumentException("School id cannot be negative");
        }
        this.shortName = shortName;
        this.fullName = fullName;
        this.credit = credit;
        this.schoolId = schoolId;
        this.courseId = -1;
    }

    public Course(String shortName, String fullName, int credit, long schoolId, long courseId) {
        this(shortName, fullName, credit, schoolId);
        if(courseId < 0) {
            throw new IllegalArgumentException("Course id cannot be negative");
        }
        this.courseId = courseId;
    }

    public String getShortName() {
        return shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public int getCredit() {
        return credit;
    }

    public long getSchoolId() {
        return schoolId;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        if(this.courseId == -1) {
            this.courseId = courseId;
        } else {
            throw new IllegalStateException("Course id already set");
        }
    }
}
