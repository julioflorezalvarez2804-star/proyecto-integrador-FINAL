package co.edu.cesde.ga.application.dto.response;

import co.edu.cesde.ga.application.dto.PeriodDTO;

import java.time.LocalDate;

public record PeriodResponseDTO(
        Long periodId,
        String code,
        LocalDate startDate,
        LocalDate endDate
) {
    public static PeriodResponseDTO from(PeriodDTO dto) {
        return new PeriodResponseDTO(dto.getPeriodId(), dto.getCode(), dto.getStartDate(), dto.getEndDate());
    }
}
