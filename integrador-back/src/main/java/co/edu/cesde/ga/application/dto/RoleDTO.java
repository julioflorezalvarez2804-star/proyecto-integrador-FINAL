package co.edu.cesde.ga.application.dto;

public class RoleDTO {
    private Long roleId;
    private String name;
    private String description;

    public RoleDTO() {}
    public RoleDTO(Long roleId, String name, String description) {
        this.roleId = roleId;
        this.name = name;
        this.description = description;
    }

    public Long getRoleId() { return roleId; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
