package hexlet.code.mapper;

import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.dto.label.LabelUpdateDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.repository.LabelsRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class LabelMapper {
    @Autowired
    private LabelsRepository labelsRepository;

    public abstract Label map(LabelCreateDTO labelCreateDTO);

    @Mapping(source = "tasks", target = "taskIds", qualifiedByName = "tasksToIds")
    public abstract LabelDTO map(Label label);

    public abstract void update(LabelUpdateDTO labelUpdateDTO, @MappingTarget Label label);

    @Named("tasksToIds")
    Set<Long> tasksToIds(Set<Task> tasks) {
        return tasks == null ? null : tasks.stream()
                .map(Task::getId)
                .collect(Collectors.toSet());
    }
}
