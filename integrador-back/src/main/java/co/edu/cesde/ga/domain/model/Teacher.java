package co.edu.cesde.ga.domain.model;

public class Teacher extends Persons {

    public Teacher() {
        super();
    }

    public Teacher(Long userID, String code, String documentNumber, String firstName, String lastName, Boolean status) {
        super(userID, code, documentNumber, firstName, lastName, status);
    }
}
