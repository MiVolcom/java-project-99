package hexlet.code.dto.taskStatus;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskStatusDTO {
    private long id;
    private String name;
    private String slug;
}
