package hexlet.code.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class TaskCreateDTO {
    private Integer index;
    @NotBlank
    private String title;
    private String content;
    @NotNull
    private String status;
    @JsonProperty("assignee_id")
    private long assigneeId;
    private Set<Long> taskLabelIds = new HashSet<>();
}
