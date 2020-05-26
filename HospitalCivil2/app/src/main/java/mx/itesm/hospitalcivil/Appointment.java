package mx.itesm.hospitalcivil;

import android.media.Image;

import java.io.Serializable;

public class Appointment implements Serializable {
    private String patientID;
    private String description;
    private String id;
    private String name;
    private int age;
    private String gender;
    private String allergies[];

    public Appointment(String description, String id, String name, int age, String gender, String[] allergies, String patientID) {
        this.description = description;
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.allergies = allergies;
        this.patientID = patientID;
    }
    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String[] getAllergies() {
        return allergies;
    }

    public void setAllergies(String[] allergies) {
        this.allergies = allergies;
    }
}
