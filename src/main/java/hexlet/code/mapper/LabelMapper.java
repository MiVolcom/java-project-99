package hexlet.code.mapper;

import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.dto.label.LabelUpdateDTO;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelsRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

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

    public abstract LabelDTO map(Label label);

    public abstract void update(LabelUpdateDTO labelUpdateDTO, @MappingTarget Label label);

}
