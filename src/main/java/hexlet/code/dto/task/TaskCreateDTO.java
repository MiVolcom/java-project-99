package hexlet.code.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskCreateDTO {
    private Integer index;
    private String title;
    private String content;
    private String status;
    @JsonProperty("assignee_id")
    private long assigneeId;
}
