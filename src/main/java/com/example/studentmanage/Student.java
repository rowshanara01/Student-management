package com.example.studentmanage;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.DoubleProperty;

// JavaFX Properties
public class Student {

    private final StringProperty name;
    private final StringProperty id;
    private final StringProperty dept;
    private final StringProperty email;
    private final DoubleProperty cgpa;

    public Student(String name, String id, String dept, String email, double cgpa) {
        this.name = new SimpleStringProperty(name);
        this.id = new SimpleStringProperty(id);
        this.dept = new SimpleStringProperty(dept);
        this.email = new SimpleStringProperty(email);
        this.cgpa = new SimpleDoubleProperty(cgpa);
    }

    // Getters and Setters (JavaFX style)

    public String getName() { return name.get(); }
    public StringProperty nameProperty() { return name; }
    public void setName(String name) { this.name.set(name); }

    public String getId() { return id.get(); }
    public StringProperty idProperty() { return id; }

    public String getDept() { return dept.get(); }
    public StringProperty deptProperty() { return dept; }
    public void setDept(String dept) { this.dept.set(dept); }

    public String getEmail() { return email.get(); }
    public StringProperty emailProperty() { return email; }
    public void setEmail(String email) { this.email.set(email); }

    public double getCgpa() { return cgpa.get(); }
    public DoubleProperty cgpaProperty() { return cgpa; }
    public void setCgpa(double cgpa) { this.cgpa.set(cgpa); }
}