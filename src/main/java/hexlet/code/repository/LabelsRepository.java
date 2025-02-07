package hexlet.code.repository;

import hexlet.code.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface LabelsRepository extends JpaRepository<Label, Long>, JpaSpecificationExecutor<Label> {
    Optional<Label> findByName(String name);
    List<Label> findAllByNameIn(Set<String> names);

}
