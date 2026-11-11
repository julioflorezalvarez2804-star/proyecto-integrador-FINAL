package co.edu.cesde.ga.domain.model;

public class Student extends Persons {
    private String birthDate;

    public Student() {
        super();
    }

    public Student(Long userID, String code, String documentNumber, String firstName, String lastName, Boolean status, String birthDate) {
        super(userID, code, documentNumber, firstName, lastName, status);
        this.birthDate = birthDate;
    }

    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
}
