package co.edu.cesde.ga.application.dto;

public class TeacherDTO {
    private Long userID;
    private String code;
    private String documentNumber;
    private String firstName;
    private String lastName;
    private Boolean status;

    public TeacherDTO() {}
    public TeacherDTO(Long userID, String code, String documentNumber, String firstName, String lastName, Boolean status) {
        this.userID = userID;
        this.code = code;
        this.documentNumber = documentNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
    }

    public Long getUserID() { return userID; }
    public void setUserID(Long userID) { this.userID = userID; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDocumentNumber() { return documentNumber; }
    public void setDocumentNumber(String documentNumber) { this.documentNumber = documentNumber; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }
}
