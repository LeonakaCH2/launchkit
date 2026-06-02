package ch.leon.launchkit.generator;

import ch.leon.launchkit.generator.ProjectGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TodoSampleGenerator {

    public void generate(
            String projectName,
            String backend,
            String database,
            boolean docker,
            boolean ci
    ) throws IOException {

        if (!backend.equalsIgnoreCase("springboot")) {
            throw new IOException("Todo sample currently only supports Spring Boot backend.");
        }

        new ProjectGenerator().generate(
                projectName,
                backend,
                "none",
                database,
                docker,
                ci
        );

        Path projectRoot = Path.of(projectName);
        Path backendPath = projectRoot.resolve("backend");

        generateTodoFiles(backendPath);
    }

    private void generateTodoFiles(Path backendPath) throws IOException {
        Path todoPackage = backendPath.resolve("src/main/java/com/example/app/todo");
        Files.createDirectories(todoPackage);

        Files.writeString(todoPackage.resolve("Todo.java"), createTodoEntity());
        Files.writeString(todoPackage.resolve("TodoRepository.java"), createTodoRepository());
        Files.writeString(todoPackage.resolve("TodoController.java"), createTodoController());
    }

    private String createTodoEntity() {
        return """
                package com.example.app.todo;

                import jakarta.persistence.Entity;
                import jakarta.persistence.GeneratedValue;
                import jakarta.persistence.GenerationType;
                import jakarta.persistence.Id;
                import jakarta.validation.constraints.NotBlank;

                @Entity
                public class Todo {

                    @Id
                    @GeneratedValue(strategy = GenerationType.IDENTITY)
                    private Long id;

                    @NotBlank
                    private String title;

                    private boolean completed;

                    public Todo() {
                    }

                    public Todo(String title, boolean completed) {
                        this.title = title;
                        this.completed = completed;
                    }

                    public Long getId() {
                        return id;
                    }

                    public void setId(Long id) {
                        this.id = id;
                    }

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public boolean isCompleted() {
                        return completed;
                    }

                    public void setCompleted(boolean completed) {
                        this.completed = completed;
                    }
                }
                """;
    }

    private String createTodoRepository() {
        return """
                package com.example.app.todo;

                import org.springframework.data.jpa.repository.JpaRepository;

                public interface TodoRepository extends JpaRepository<Todo, Long> {
                }
                """;
    }

    private String createTodoController() {
        return """
                package com.example.app.todo;

                import jakarta.validation.Valid;
                import org.springframework.http.ResponseEntity;
                import org.springframework.web.bind.annotation.*;

                import java.util.List;

                @RestController
                @RequestMapping("/api/todos")
                public class TodoController {

                    private final TodoRepository todoRepository;

                    public TodoController(TodoRepository todoRepository) {
                        this.todoRepository = todoRepository;
                    }

                    @GetMapping
                    public List<Todo> findAll() {
                        return todoRepository.findAll();
                    }

                    @PostMapping
                    public Todo create(@Valid @RequestBody Todo todo) {
                        return todoRepository.save(todo);
                    }

                    @PutMapping("/{id}")
                    public ResponseEntity<Todo> update(
                            @PathVariable Long id,
                            @Valid @RequestBody Todo updatedTodo
                    ) {
                        return todoRepository.findById(id)
                                .map(todo -> {
                                    todo.setTitle(updatedTodo.getTitle());
                                    todo.setCompleted(updatedTodo.isCompleted());
                                    return ResponseEntity.ok(todoRepository.save(todo));
                                })
                                .orElse(ResponseEntity.notFound().build());
                    }

                    @DeleteMapping("/{id}")
                    public ResponseEntity<Void> delete(@PathVariable Long id) {
                        if (!todoRepository.existsById(id)) {
                            return ResponseEntity.notFound().build();
                        }

                        todoRepository.deleteById(id);
                        return ResponseEntity.noContent().build();
                    }
                }
                """;
    }
}