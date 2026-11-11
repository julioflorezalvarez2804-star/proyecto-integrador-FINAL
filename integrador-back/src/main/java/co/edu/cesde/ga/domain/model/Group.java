package co.edu.cesde.ga.domain.model;

public class Group {
    private Long groupId;
    private String code;
    private Long programId;
    private Long periodId;
    private String shift;

    public Group() {}
    public Group(Long groupId, String code, Long programId, Long periodId, String shift) {
        this.groupId = groupId;
        this.code = code;
        this.programId = programId;
        this.periodId = periodId;
        this.shift = shift;
    }

    public Long getGroupId() { return groupId; }
    public void setGroupId(Long groupId) { this.groupId = groupId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public Long getProgramId() { return programId; }
    public void setProgramId(Long programId) { this.programId = programId; }
    public Long getPeriodId() { return periodId; }
    public void setPeriodId(Long periodId) { this.periodId = periodId; }
    public String getShift() { return shift; }
    public void setShift(String shift) { this.shift = shift; }
}
