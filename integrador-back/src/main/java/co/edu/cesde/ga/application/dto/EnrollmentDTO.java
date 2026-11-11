package co.edu.cesde.ga.application.dto;

import java.time.LocalDateTime;

public class EnrollmentDTO {
    private Long enrollmentId;
    private Long studentId;
    private Long groupId;
    private String status;
    private LocalDateTime enrolledAt;

    public EnrollmentDTO() {}
    public EnrollmentDTO(Long enrollmentId, Long studentId, Long groupId, String status, LocalDateTime enrolledAt) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.groupId = groupId;
        this.status = status;
        this.enrolledAt = enrolledAt;
    }

    public Long getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(Long enrollmentId) { this.enrollmentId = enrollmentId; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getGroupId() { return groupId; }
    public void setGroupId(Long groupId) { this.groupId = groupId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(LocalDateTime enrolledAt) { this.enrolledAt = enrolledAt; }
}
