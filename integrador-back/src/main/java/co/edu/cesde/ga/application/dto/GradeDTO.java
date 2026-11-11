package co.edu.cesde.ga.application.dto;

public class GradeDTO {
    private Long gradeId;
    private Long groupSubjectId;
    private Long studentId;
    private double finalScore;
    private String observation;

    public GradeDTO() {}
    public GradeDTO(Long gradeId, Long groupSubjectId, Long studentId, double finalScore, String observation) {
        this.gradeId = gradeId;
        this.groupSubjectId = groupSubjectId;
        this.studentId = studentId;
        this.finalScore = finalScore;
        this.observation = observation;
    }

    public Long getGradeId() { return gradeId; }
    public void setGradeId(Long gradeId) { this.gradeId = gradeId; }
    public Long getGroupSubjectId() { return groupSubjectId; }
    public void setGroupSubjectId(Long groupSubjectId) { this.groupSubjectId = groupSubjectId; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public double getFinalScore() { return finalScore; }
    public void setFinalScore(double finalScore) { this.finalScore = finalScore; }
    public String getObservation() { return observation; }
    public void setObservation(String observation) { this.observation = observation; }
}
