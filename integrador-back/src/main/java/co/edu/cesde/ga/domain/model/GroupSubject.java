package co.edu.cesde.ga.domain.model;

public class GroupSubject {
    private Long groupSubjectId;
    private Long groupId;
    private Long subjectId;
    private Long teacherId;

    public GroupSubject() {}
    public GroupSubject(Long groupSubjectId, Long groupId, Long subjectId, Long teacherId) {
        this.groupSubjectId = groupSubjectId;
        this.groupId = groupId;
        this.subjectId = subjectId;
        this.teacherId = teacherId;
    }

    public Long getGroupSubjectId() { return groupSubjectId; }
    public void setGroupSubjectId(Long groupSubjectId) { this.groupSubjectId = groupSubjectId; }
    public Long getGroupId() { return groupId; }
    public void setGroupId(Long groupId) { this.groupId = groupId; }
    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }
}
