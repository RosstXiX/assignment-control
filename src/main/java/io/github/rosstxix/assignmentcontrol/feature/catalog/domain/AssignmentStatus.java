package io.github.rosstxix.assignmentcontrol.feature.catalog.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "assignment_statuses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;
}
