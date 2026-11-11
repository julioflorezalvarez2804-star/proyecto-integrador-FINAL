package co.edu.cesde.ga.application.service.impl;

import co.edu.cesde.ga.application.dto.GradeDTO;
import co.edu.cesde.ga.application.service.GradeService;
import co.edu.cesde.ga.domain.exception.BusinessException;
import co.edu.cesde.ga.domain.exception.GradeAlreadyExistsException;
import co.edu.cesde.ga.domain.exception.GradeNotFoundException;
import co.edu.cesde.ga.domain.exception.GroupSubjectNotFoundException;
import co.edu.cesde.ga.domain.exception.StudentNotFoundException;
import co.edu.cesde.ga.domain.model.Grade;
import co.edu.cesde.ga.domain.model.GroupSubject;
import co.edu.cesde.ga.domain.repository.EnrollmentRepository;
import co.edu.cesde.ga.domain.repository.GradeRepository;
import co.edu.cesde.ga.domain.repository.GroupSubjectRepository;
import co.edu.cesde.ga.domain.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeServiceImpl implements GradeService {

    private static final String ACTIVE = "ACTIVE";
    private static final String COMPLETED = "COMPLETED";

    private final GradeRepository gradeRepository;
    private final GroupSubjectRepository groupSubjectRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;

    public GradeServiceImpl(GradeRepository gradeRepository, GroupSubjectRepository groupSubjectRepository,
                            StudentRepository studentRepository, EnrollmentRepository enrollmentRepository) {
        this.gradeRepository = gradeRepository;
        this.groupSubjectRepository = groupSubjectRepository;
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    public GradeDTO assignGrade(Long groupSubjectId, Long studentId, double finalScore, String observation) {
        validateScore(finalScore);

        GroupSubject groupSubject = groupSubjectRepository.findById(groupSubjectId)
                .orElseThrow(() -> new GroupSubjectNotFoundException(groupSubjectId));
        if (!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException(studentId);
        }

        // Regla de negocio: solo se califica a estudiantes matriculados en el grupo de la materia
        Long groupId = groupSubject.getGroupId();
        boolean enrolled = enrollmentRepository.existsByStudentIdAndGroupIdAndStatus(studentId, groupId, ACTIVE)
                || enrollmentRepository.existsByStudentIdAndGroupIdAndStatus(studentId, groupId, COMPLETED);
        if (!enrolled) {
            throw new BusinessException("El estudiante no esta matriculado en el grupo de esta materia");
        }

        if (gradeRepository.findByGroupSubjectIdAndStudentId(groupSubjectId, studentId).isPresent()) {
            throw new GradeAlreadyExistsException(groupSubjectId, studentId);
        }

        Grade grade = new Grade(null, groupSubjectId, studentId, finalScore, observation);
        return toDTO(gradeRepository.save(grade));
    }

    @Override
    public GradeDTO findById(Long id) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new GradeNotFoundException(id));
        return toDTO(grade);
    }

    @Override
    public List<GradeDTO> findAll() {
        return gradeRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public GradeDTO update(Long id, double finalScore, String observation) {
        validateScore(finalScore);
        Grade existing = gradeRepository.findById(id)
                .orElseThrow(() -> new GradeNotFoundException(id));
        Grade grade = new Grade(id, existing.getGroupSubjectId(), existing.getStudentId(), finalScore, observation);
        return toDTO(gradeRepository.save(grade));
    }

    @Override
    public void delete(Long id) {
        if (!gradeRepository.existsById(id)) {
            throw new GradeNotFoundException(id);
        }
        gradeRepository.deleteById(id);
    }

    private void validateScore(double finalScore) {
        if (finalScore < 0.0 || finalScore > 5.0) {
            throw new BusinessException("La nota debe estar entre 0.0 y 5.0");
        }
    }

    private GradeDTO toDTO(Grade grade) {
        return new GradeDTO(grade.getGradeId(), grade.getGroupSubjectId(), grade.getStudentId(),
                grade.getFinalScore(), grade.getObservation());
    }
}
