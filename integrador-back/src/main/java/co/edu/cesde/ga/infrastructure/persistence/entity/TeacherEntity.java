package co.edu.cesde.ga.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "teacher")
public class TeacherEntity extends PersonEntity {
}
