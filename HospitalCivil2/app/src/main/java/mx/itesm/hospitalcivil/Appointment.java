package mx.itesm.hospitalcivil;
import java.io.Serializable;
import java.util.Date;

public class Appointment implements Serializable {
    private String patientID;
    private String description;
    private String authorID;
    private String name;
    private int age;
    private String gender;
    private String[] allergies;
    private String docID;

    public Appointment(String docID, String description, String authorID, String name, Date birthDate,
                       String gender, String[] allergies, String patientID) {
        this.docID = docID;
        this.description = description;
        this.authorID = authorID;
        this.name = name;
        this.age = getAge(birthDate);
        this.gender = gender;
        this.allergies = allergies;
        this.patientID = patientID;
    }
    private int getAge(Date birthDate) {
        Date today = new Date();
        long diff = today.getTime() - birthDate.getTime();
        diff /= (365 * 24 * 60 * 60);
        diff /= 1000;
        return (int)diff;
    }
    public String getPatientID() {
        return patientID;
    }

    public String getDocID(){
        return this.docID;
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

    public String getAuthorId() {
        return this.authorID;
    }

    public void setAuthorId(String id) {
        this.authorID = id;
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
