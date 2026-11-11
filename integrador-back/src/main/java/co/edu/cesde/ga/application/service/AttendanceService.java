package co.edu.cesde.ga.application.service;

import co.edu.cesde.ga.application.dto.AttendanceDTO;
import co.edu.cesde.ga.application.dto.AttendanceRecordDTO;
import co.edu.cesde.ga.application.dto.AttendanceSummaryDTO;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {

    // Registra la lista de asistencia de una clase. Si ya existia un registro de ese estudiante
    // en esa fecha, lo actualiza. Es todo o nada.
    List<AttendanceDTO> registerSession(Long groupSubjectId, LocalDate classDate, List<AttendanceRecordDTO> records);

    // Si classDate es null devuelve todas las fechas de esa materia del grupo.
    List<AttendanceDTO> findBySession(Long groupSubjectId, LocalDate classDate);

    // Si groupSubjectId es null resume todas las materias.
    List<AttendanceSummaryDTO> summary(Long groupSubjectId);

    void delete(Long id);
}
