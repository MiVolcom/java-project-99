package hexlet.code.component;

import hexlet.code.controller.api.TaskStatusController;
import hexlet.code.controller.api.UsersController;
import hexlet.code.dto.UserCreateDTO;
import hexlet.code.dto.taskStatus.TaskStatusCreateDTO;
import hexlet.code.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private final CustomUserDetailsService userService;

    @Autowired
    private final UsersController usersController;

    @Autowired
    private final TaskStatusController taskStatusController;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var admin = new UserCreateDTO();

        admin.setEmail("hexlet@example.com");
        admin.setFirstName("Admin");
        admin.setLastName("Admin");
        admin.setPassword("qwerty");
        usersController.create(admin);


        List<TaskStatusCreateDTO> taskStatuses = Arrays.asList(
                new TaskStatusCreateDTO("Draft", "draft"),
                new TaskStatusCreateDTO("ToReview", "to_review"),
                new TaskStatusCreateDTO("ToBeFixed", "to_be_fixed"),
                new TaskStatusCreateDTO("ToPublish", "to_publish"),
                new TaskStatusCreateDTO("Published", "published")
        );
        taskStatuses.forEach(taskStatusController::create);
    }
}
