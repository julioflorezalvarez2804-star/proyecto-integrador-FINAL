package co.edu.cesde.ga.domain.repository;

import co.edu.cesde.ga.domain.model.GroupSubject;

import java.util.List;
import java.util.Optional;

public interface GroupSubjectRepository {
    GroupSubject save(GroupSubject groupSubject);
    Optional<GroupSubject> findById(Long id);
    List<GroupSubject> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
    boolean existsByGroupIdAndSubjectId(Long groupId, Long subjectId);
}
