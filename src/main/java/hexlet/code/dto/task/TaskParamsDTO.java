package hexlet.code.dto.task;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskParamsDTO {
    private String titleCont;
    private long assigneeId;
    private String slug;
    private long labelId;
}
