# Proyecto Integrador CESDE - Sistema de Gestion Academica

## Antes de ejecutar

1. Crea la base de datos en phpMyAdmin:
   - Nombre: `academic_management_db`
   - Cotejamiento: `utf8mb4_general_ci`

2. Asegurate de tener un usuario de MySQL con permisos sobre esa base
   (puedes usar el mismo usuario que ya tenias configurado en el otro proyecto).

3. Edita `src/main/resources/application.yml` y coloca tu usuario y contrasena reales:
   ```yaml
   spring:
     datasource:
       username: TU_USUARIO
       password: TU_CONTRASENA
   ```

4. Ejecuta la aplicacion. Hibernate creara todas las tablas automaticamente
   (`ddl-auto: update`), incluida la tabla intermedia `user_role` que genera
   la relacion @ManyToMany entre `users` y `role`.

## Arquitectura

```
domain/model          -> POJOs de dominio, sin anotaciones JPA
domain/repository     -> Puertos (interfaces)
domain/exception      -> ResourceNotFoundException, BusinessException

application/dto       -> Objetos de transporte (DTO)
application/service   -> Casos de uso (interfaces + impl)

infrastructure/persistence/entity   -> Entidades JPA (@Entity)
infrastructure/persistence/jpa      -> Interfaces Spring Data (JpaRepository)
infrastructure/persistence/adapter  -> Implementan los puertos del dominio usando JPA

presentation/controller   -> Controladores REST
presentation/exception    -> Manejador global de errores
```

## Relaciones JPA aplicadas

- `@ManyToOne` / `@OneToMany`: Group->Program, Group->Period, Subject->Program,
  GroupSubject->Group/Subject/Teacher, Enrollment->Student/Group, Grade->GroupSubject/Student.
- `@ManyToMany` real: `User` <-> `Role`, mapeado con `@JoinTable(name = "user_role")`.
  JPA crea la tabla intermedia automaticamente, sin necesidad de una entidad propia.

## Endpoints principales

- CRUD estandar en `/api/students`, `/api/teachers`, `/api/programs`, `/api/periods`,
  `/api/groups`, `/api/subjects`, `/api/group-subjects`, `/api/users`, `/api/roles`.
- `POST /api/enrollments?studentId=1&groupId=1` -> matricula (valida duplicados).
- `PATCH /api/enrollments/{id}/cancel` -> cancela una matricula.
- `POST /api/grades?groupSubjectId=1&studentId=1&finalScore=4.5&observation=...` -> asigna/actualiza nota.
- `POST /api/user-roles?userId=1&roleId=1` -> asigna rol a usuario.
- `DELETE /api/user-roles?userId=1&roleId=1` -> quita rol.
- `GET /api/user-roles/user/{userId}` -> lista roles de un usuario.
