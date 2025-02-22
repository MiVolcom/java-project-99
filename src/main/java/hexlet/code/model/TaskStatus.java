package hexlet.code.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@ToString(includeFieldNames = true, onlyExplicitlyIncluded = true)
@Table(name = "task_statuses")
@EqualsAndHashCode(of = {"name"})
public class TaskStatus implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1)
    @Column(unique = true)
    @ToString.Include
    private String name;

    @Column(unique = true)
    @Size(min = 1)
    @ToString.Include
    private String slug;

    @CreatedDate
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "taskStatus", cascade = CascadeType.MERGE)
    private Set<Task> tasks = new HashSet<>();

}
