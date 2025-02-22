package hexlet.code.controller.api;

import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.UserUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class UsersController {

    private final UserRepository userRepository;

    private final UserMapper userMapper;
    private final UserUtils userUtils;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> index() {
        var users = userRepository.findAll();
        var result = users.stream()
                .map(userMapper::map)
                .toList();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(users.size()))
                .body(result);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@Valid @RequestBody UserCreateDTO userData) {
        var user = userMapper.map(userData);
        userRepository.save(user);
        return userMapper.map(user);
    }

    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO update(@RequestBody UserUpdateDTO userData, @PathVariable Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        var authenticationUser = userUtils.getCurrentUser();
        if (!authenticationUser.getEmail().equals(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        userMapper.update(userData, user);
        userRepository.save(user);
        var userDTO = userMapper.map(user);
        return userDTO;
    }

    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO show(@PathVariable Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
        return userMapper.map(user);
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        var authenticationUser = userUtils.getCurrentUser();
        if (authenticationUser.getEmail().equals(user.getEmail())) {
            userRepository.deleteById(id);
        }
    }
}
