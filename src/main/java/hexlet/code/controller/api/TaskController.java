package hexlet.code.controller.api;

import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskParamsDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.repository.TaskRepository;
import hexlet.code.specification.TaskSpecification;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class TaskController {

    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    private final TaskSpecification specBuilder;


    @GetMapping(path = "/tasks")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<List<TaskDTO>> index(TaskParamsDTO taskParamsDTO) {
        var filter = specBuilder.build(taskParamsDTO);
        List<TaskDTO> tasks = taskRepository.findAll(filter).stream()
                .map(taskMapper::map)
                .toList();

        return ResponseEntity
                .ok()
                .header("X-Total-Count", String.valueOf(tasks.size()))
                .body(tasks);

    }

    @PostMapping("/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO create(@Valid @RequestBody TaskCreateDTO data) {
        var task = taskMapper.map(data);
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    @PutMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO update(@Valid @RequestBody TaskUpdateDTO data, @PathVariable Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
        taskMapper.update(data, task);
        taskRepository.save(task);
        var taskDTO = taskMapper.map(task);
        return taskDTO;
    }

    @GetMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO show(@PathVariable Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
        return taskMapper.map(task);
    }

    @DeleteMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
        taskRepository.delete(task);
    }
}
