package microservice.employee_service.Model;

import at.backend.drugstore.microservice.common_classes.DTOs.Employee.EmployeeInsertDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.employee_service.Model.enums.Genre;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Data
@NoArgsConstructor
@Table(name = "employees")
public class Employee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "company_email")
    private String companyEmail;

    @Column(name = "company_phone")
    private String companyPhone;

    @Column(name = "hired_at")
    private LocalDateTime hiredAt;

    @Column(name = "fired_at")
    private LocalDateTime firedAt;

    @Column(name = "is_employee_active")
    private boolean isEmployeeActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;

    @OneToOne(mappedBy = "employee")
    private PhoneNumber phoneNumber;

    public Employee(EmployeeInsertDTO employeeInsertDTO) {
        this.firstName = employeeInsertDTO.getFirstName();
        this.lastName = employeeInsertDTO.getLastName();
        this.birthDate = employeeInsertDTO.getBirthDate();
        this.hiredAt = employeeInsertDTO.getHiredAt();
        this.isEmployeeActive = true;
        this.genre = Genre.valueOf(employeeInsertDTO.getGenre());
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

}
