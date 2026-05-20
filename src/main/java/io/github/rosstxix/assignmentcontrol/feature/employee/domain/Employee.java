package io.github.rosstxix.assignmentcontrol.feature.employee.domain;

import io.github.rosstxix.assignmentcontrol.feature.catalog.domain.Department;
import io.github.rosstxix.assignmentcontrol.feature.catalog.domain.JobTitle;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String middleName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_title_id", nullable = false)
    private JobTitle jobTitle;

    @Builder.Default
    @Column(nullable = false)
    private String employmentStatus = "ACTIVE";

    @OneToOne(mappedBy = "employee")
    private Account account;
}
