package co.edu.cesde.ga.domain.model;

public class Program {
    private Long programId;
    private String code;
    private String name;

    public Program() {}
    public Program(Long programId, String code, String name) {
        this.programId = programId;
        this.code = code;
        this.name = name;
    }

    public Long getProgramId() { return programId; }
    public void setProgramId(Long programId) { this.programId = programId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
