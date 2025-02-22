package hexlet.code.controller.api;

import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.dto.label.LabelUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.repository.LabelsRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class LabelController {

    private final LabelsRepository labelsRepository;
    private final LabelMapper labelMapper;

    @GetMapping("/labels")
    public ResponseEntity<List<LabelDTO>> index() {
        var labels = labelsRepository.findAll();
        var result = labels.stream()
                .map(labelMapper::map)
                .toList();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(labels.size()))
                .body(result);
    }

    @PostMapping("/labels")
    @ResponseStatus(HttpStatus.CREATED)
    public LabelDTO create(@Valid @RequestBody LabelCreateDTO data) {
        var label = labelMapper.map(data);
        labelsRepository.save(label);
        return labelMapper.map(label);
    }

    @PutMapping("/labels/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LabelDTO update(@Valid @RequestBody LabelUpdateDTO data, @PathVariable Long id) {
        var label = labelsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
        labelMapper.update(data, label);
        labelsRepository.save(label);
        var taskDTO = labelMapper.map(label);
        return taskDTO;
    }

    @GetMapping("/labels/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LabelDTO show(@PathVariable Long id) {
        var label = labelsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
        return labelMapper.map(label);
    }

    @DeleteMapping("/labels/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        var task = labelsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
        labelsRepository.delete(task);
    }
}
