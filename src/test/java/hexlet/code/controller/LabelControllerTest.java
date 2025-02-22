package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.controller.Util.ModelGenerator;
import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelsRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LabelControllerTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ModelGenerator modelGenerator;

    private Task taskTest;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;
    private User testUser;
    private Task testTask;
    private TaskStatus testStatus;
    private Label testLabel;
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private LabelsRepository labelsRepository;

    @Autowired
    private LabelMapper labelMapper;


    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity())
                .build();

        testUser = Instancio.of(modelGenerator.getUserModel()).create();
        token = jwt().jwt(builder -> builder.subject(testUser.getEmail()));
        testTask = Instancio.of(modelGenerator.getTaskModel()).create();
        testStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();
        testLabel = Instancio.of(modelGenerator.getLabelModel()).create();
        taskRepository.save(testTask);
        userRepository.save(testUser);
        taskStatusRepository.save(testStatus);
        labelsRepository.save(testLabel);
    }

    @BeforeEach
    public void clean() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
        taskStatusRepository.deleteAll();
        labelsRepository.deleteAll();
    }

    @Test
    public void testShow() throws Exception {

        var request = get("/api/labels/{id}", testLabel.getId()).with(token);

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testIndex() throws Exception {

        var result = mockMvc.perform(get("/api/labels").with(token))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThatJson(body).isArray();
    }

    @Test
    public void testCreate() throws Exception {
        var dto = new LabelCreateDTO();
        Label label = Instancio.of(modelGenerator.getLabelModel()).create();

        dto.setName(label.getName());

        var request = post("/api/labels")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var savedLabel = labelsRepository.findByName(dto.getName()).orElseThrow();

        assertThat(savedLabel.getName()).isEqualTo(dto.getName());
    }

    @Test
    public void testUpdate() throws Exception {

        var dto = labelMapper.map(testLabel);
        dto.setName("new label");

        var request = put("/api/labels/{id}", testLabel.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var label = labelsRepository.findById(dto.getId()).orElseThrow();

        assertThat(label.getName()).isEqualTo(dto.getName());
    }

    @Test
    public void testDelete() throws Exception {

        var request = delete("/api/labels/{id}", testLabel.getId()).with(token);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertFalse(labelsRepository.findById(testLabel.getId()).isPresent());
    }

}
