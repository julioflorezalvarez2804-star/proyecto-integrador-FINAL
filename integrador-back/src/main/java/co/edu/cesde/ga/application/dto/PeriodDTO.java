package co.edu.cesde.ga.application.dto;

import java.time.LocalDate;

public class PeriodDTO {
    private Long periodId;
    private String code;
    private LocalDate startDate;
    private LocalDate endDate;

    public PeriodDTO() {}
    public PeriodDTO(Long periodId, String code, LocalDate startDate, LocalDate endDate) {
        this.periodId = periodId;
        this.code = code;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getPeriodId() { return periodId; }
    public void setPeriodId(Long periodId) { this.periodId = periodId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
}
