import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.model.task.Epic;
import project.model.task.Status;
import project.model.task.Subtask;
import project.model.task.Task;
import project.server.HttpTaskServer;
import project.server.KVServer;
import project.service.Managers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class HttpTaskServerTest {
    private static KVServer kvServer;
    private static HttpTaskServer taskServer;
    private static Gson gson = Managers.getGson();
    private static final String TASK_URL = "http://localhost:8080/tasks/task/";
    private static final String EPIC_URL = "http://localhost:8080/tasks/epic/";
    private static final String SUBTASK_URL = "http://localhost:8080/tasks/subtask/";
    private static final String HISTORY_URL = "http://localhost:8080/tasks/history";
    private static final String PRIORITIZED_TASKS_URL = "http://localhost:8080/tasks/";
    private static final String EPIC_SUBTASKS_URL = "http://localhost:8080/tasks/subtask/epic/?id=";

    @BeforeAll
    static void startServer() {
        try {
            kvServer = new KVServer();
            kvServer.start();
            taskServer = new HttpTaskServer();
            taskServer.start();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void stopServer() {
        kvServer.stop();
        taskServer.stop();
    }

    @BeforeEach
    void BeforeEach() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(TASK_URL);
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
            url = URI.create(EPIC_URL);
            request = HttpRequest.newBuilder().uri(url).DELETE().build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
            url = URI.create(SUBTASK_URL);
            request = HttpRequest.newBuilder().uri(url).DELETE().build();
            client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldGetTasks() {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(TASK_URL);
        Task task = new Task(Status.NEW, "Задача",
                "Создать задачу для тестирования работоспособности приложения",
                15L, LocalDateTime.of(2023, 2, 24, 12, 0));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .build();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
            request = HttpRequest.newBuilder().uri(uri).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            List<Task> tasks = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>() {
            }.getType());
            assertEquals(1, tasks.size());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldGetEpics() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_URL);
        Epic epic = new Epic(Status.NEW, "Эпик 1", "Осуществить мечту", 0,
                LocalDateTime.now());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
            request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldGetSubtasksTest() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_URL);
        Epic epic = new Epic(Status.NEW, "Эпик 1", "Осуществить мечту", 0, null);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();

        try {
            HttpResponse<String> postResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, postResponse.statusCode(), "POST запрос");
            if (postResponse.statusCode() == 201) {
                int epicId = Integer.parseInt(postResponse.body());
                epic.setId(epicId);
                Subtask subtask = new Subtask(Status.NEW, epicId, "Подзадача 1 эпика 1",
                        "Победить всех злодеев в мире",
                        15L, LocalDateTime.of(2023, 2, 23, 13, 0));
                url = URI.create(SUBTASK_URL);

                request = HttpRequest.newBuilder()
                        .uri(url)
                        .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))
                        .build();

                client.send(request, HttpResponse.BodyHandlers.ofString());
                request = HttpRequest.newBuilder().uri(url).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                assertEquals(200, response.statusCode());
                JsonArray arrayTasks = JsonParser.parseString(response.body()).getAsJsonArray();
                assertEquals(1, arrayTasks.size());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldGetTaskById() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(TASK_URL);
        Task task = new Task(Status.NEW, "Задача",
                "Создать задачу для тестирования работоспособности приложения",
                15L, LocalDateTime.of(2023, 2, 24, 12, 0));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .build();

        try {
            HttpResponse<String> postResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, postResponse.statusCode(), "POST запрос");
            if (postResponse.statusCode() == 200) {
                int id = Integer.parseInt(postResponse.body());
                task.setId(id);
                url = URI.create(TASK_URL + "?id=" + id);
                request = HttpRequest.newBuilder().uri(url).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                assertEquals(200, response.statusCode());
                Task responseTask = gson.fromJson(response.body(), Task.class);
                assertEquals(task, responseTask);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldGetEpicById() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_URL);
        Epic epic = new Epic(Status.NEW, "Эпик 1", "Осуществить мечту", 0,
                LocalDateTime.now());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();

        try {
            HttpResponse<String> postResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, postResponse.statusCode());
            if (postResponse.statusCode() == 200) {
                int id = Integer.parseInt(postResponse.body());
                epic.setId(id);
                url = URI.create(EPIC_URL + "?id=" + id);
                request = HttpRequest.newBuilder().uri(url).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                assertEquals(200, response.statusCode());
                Epic responseTask = gson.fromJson(response.body(), Epic.class);
                assertEquals(epic, responseTask);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldGetSubtaskById() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_URL);
        Epic epic = new Epic(Status.NEW, "Эпик 1", "Осуществить мечту", 0, LocalDateTime.now());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();

        try {
            HttpResponse<String> postResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, postResponse.statusCode(), "POST запрос");
            if (postResponse.statusCode() == 200) {
                int epicId = Integer.parseInt(postResponse.body());
                epic.setId(epicId);
                Subtask subtask = new Subtask(Status.NEW, epicId, "Подзадача 1 эпика 1",
                        "Победить всех злодеев в мире",
                        15L, LocalDateTime.of(2023, 2, 23, 13, 0));
                url = URI.create(SUBTASK_URL);

                request = HttpRequest.newBuilder()
                        .uri(url)
                        .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))
                        .build();
                postResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

                assertEquals(200, postResponse.statusCode(), "POST запрос");
                if (postResponse.statusCode() == 200) {
                    int id = Integer.parseInt(postResponse.body());
                    subtask.setId(id);
                    url = URI.create(SUBTASK_URL + "?id=" + id);
                    request = HttpRequest.newBuilder().uri(url).GET().build();
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    assertEquals(200, response.statusCode());
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldUpdateTask() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(TASK_URL);
        Task task = new Task(Status.NEW, "Задача",
                "Создать задачу для тестирования работоспособности приложения",
                15L, LocalDateTime.of(2023, 2, 24, 12, 0));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .build();

        try {
            HttpResponse<String> postResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (postResponse.statusCode() == 201) {
                int id = Integer.parseInt(postResponse.body());
                task.setStatus(Status.IN_PROGRESS);
                request = HttpRequest.newBuilder()
                        .uri(url)
                        .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                        .build();
                client.send(request, HttpResponse.BodyHandlers.ofString());

                url = URI.create(TASK_URL + "?id=" + id);
                request = HttpRequest.newBuilder().uri(url).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                assertEquals(200, response.statusCode());
                Task responseTask = gson.fromJson(response.body(), Task.class);
                assertEquals(task, responseTask);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldUpdateEpic() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_URL);
        Epic epic = new Epic(Status.NEW, "Эпик 1", "Осуществить мечту", 0, null);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();

        try {
            HttpResponse<String> postResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (postResponse.statusCode() == 201) {
                int id = Integer.parseInt(postResponse.body());
                epic.setStatus(Status.IN_PROGRESS);
                request = HttpRequest.newBuilder()
                        .uri(url)
                        .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                        .build();
                client.send(request, HttpResponse.BodyHandlers.ofString());

                url = URI.create(EPIC_URL + "?id=" + id);
                request = HttpRequest.newBuilder().uri(url).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                assertEquals(200, response.statusCode());
                Epic responseTask = gson.fromJson(response.body(), Epic.class);
                assertEquals(epic, responseTask);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldUpdateSubtask() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_URL);
        Epic epic = new Epic(Status.NEW, "Эпик 1", "Осуществить мечту", 0,
                LocalDateTime.now());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();

        try {
            HttpResponse<String> postResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, postResponse.statusCode(), "POST запрос");
            if (postResponse.statusCode() == 200) {
                Subtask subtask = new Subtask(Status.NEW, epic.getId(), "Подзадача 1 эпика 1",
                        "Победить всех злодеев в мире",
                        15L, LocalDateTime.of(2023, 2, 23, 13, 0));
                url = URI.create(SUBTASK_URL);
                request = HttpRequest.newBuilder()
                        .uri(url)
                        .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))
                        .build();
                postResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (postResponse.statusCode() == 200) {
                    subtask.setStatus(Status.IN_PROGRESS);
                    request = HttpRequest.newBuilder()
                            .uri(url)
                            .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))
                            .build();
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    url = URI.create(SUBTASK_URL);
                    request = HttpRequest.newBuilder().uri(url).GET().build();
                    HttpResponse<String> response2 = client.send(request, HttpResponse.BodyHandlers.ofString());
                    assertEquals(200, response.statusCode());
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldDeleteTasks() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(TASK_URL);
        Task task = new Task(Status.NEW, "Задача",
                "Создать задачу для тестирования работоспособности приложения",
                15L, LocalDateTime.of(2023, 2, 24, 12, 0));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .build();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
            request = HttpRequest.newBuilder().uri(url).DELETE().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            request = HttpRequest.newBuilder().uri(url).GET().build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonArray arrayTasks = JsonParser.parseString(response.body()).getAsJsonArray();
            assertEquals(0, arrayTasks.size());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldDeleteEpics() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_URL);
        Epic epic = new Epic(Status.NEW, "Эпик 1", "Осуществить мечту", 0, null);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
            request = HttpRequest.newBuilder().uri(url).DELETE().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            request = HttpRequest.newBuilder().uri(url).GET().build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            JsonArray arrayTasks = JsonParser.parseString(response.body()).getAsJsonArray();
            assertEquals(0, arrayTasks.size());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldDeleteSubtasks() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(SUBTASK_URL);
        Epic epic = new Epic(Status.NEW, "Эпик 1", "Осуществить мечту", 0, null);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();

        try {
            HttpResponse<String> postResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, postResponse.statusCode(), "POST запрос");
            if (postResponse.statusCode() == 201) {
                int epicId = Integer.parseInt(postResponse.body());
                epic.setId(epicId);
                Subtask subtask = new Subtask(Status.NEW, epicId, "Подзадача 1 эпика 1",
                        "Победить всех злодеев в мире",
                        15L, LocalDateTime.of(2023, 02, 23, 13, 0));
                url = URI.create(SUBTASK_URL);

                request = HttpRequest.newBuilder()
                        .uri(url)
                        .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))
                        .build();

                client.send(request, HttpResponse.BodyHandlers.ofString());
                request = HttpRequest.newBuilder().uri(url).DELETE().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                assertEquals(204, response.statusCode());
                request = HttpRequest.newBuilder().uri(url).GET().build();
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
                assertEquals(200, response.statusCode());
                JsonArray arrayTasks = JsonParser.parseString(response.body()).getAsJsonArray();
                assertEquals(0, arrayTasks.size());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldDeleteTaskById() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(TASK_URL);
        Task task = new Task(Status.NEW, "Задача",
                "Создать задачу для тестирования работоспособности приложения",
                15L, LocalDateTime.of(2023, 2, 24, 12, 0));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .build();
        try {
            HttpResponse<String> postResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            int id = Integer.parseInt(postResponse.body());
            url = URI.create(TASK_URL + "?id=" + id);
            request = HttpRequest.newBuilder().uri(url).DELETE().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldDeleteEpicById() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_URL);
        Epic epic = new Epic(Status.NEW, "Эпик 1", "Осуществить мечту", 0, null);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();
        try {
            HttpResponse<String> postResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, postResponse.statusCode(), "POST запрос");
            if (postResponse.statusCode() == 200) {
                int id = Integer.parseInt(postResponse.body());
                url = URI.create(EPIC_URL + "?id=" + id);
                request = HttpRequest.newBuilder().uri(url).DELETE().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                assertEquals(200, response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldDeleteSubtaskById() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_URL);
        Epic epic = new Epic(Status.NEW, "Эпик 1", "Осуществить мечту", 0, null);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();

        try {
            HttpResponse<String> postResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, postResponse.statusCode(), "POST запрос");
            if (postResponse.statusCode() == 200) {
                Subtask subtask = new Subtask(Status.NEW, epic.getId(), "Подзадача 1 эпика 1",
                        "Победить всех злодеев в мире",
                        15L, LocalDateTime.of(2023, 2, 23, 13, 0));
                url = URI.create(SUBTASK_URL);

                request = HttpRequest.newBuilder()
                        .uri(url)
                        .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))
                        .build();
                postResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

                assertEquals(200, postResponse.statusCode(), "POST запрос");
                if (postResponse.statusCode() == 201) {
                    int id = Integer.parseInt(postResponse.body());
                    subtask.setId(id);
                    url = URI.create(SUBTASK_URL + "?id=" + id);
                    request = HttpRequest.newBuilder().uri(url).DELETE().build();
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    assertEquals(200, response.statusCode());
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldGetHistory() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(TASK_URL);
        Task task = new Task(Status.NEW, "Задача",
                "Создать задачу для тестирования работоспособности приложения",
                15L, LocalDateTime.of(2023, 2, 24, 12, 0));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int id = Integer.parseInt(response.body());
            url = URI.create(TASK_URL + "?id=" + id);
            request = HttpRequest.newBuilder().uri(url).GET().build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                url = URI.create(HISTORY_URL);
                request = HttpRequest.newBuilder().uri(url).GET().build();
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
                assertEquals(200, response.statusCode());
                assertFalse(response.body().isEmpty());
            }
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldGetSortedTasks() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(TASK_URL);
        Task task = new Task(Status.NEW, "Задача",
                "Создать задачу для тестирования работоспособности приложения",
                15L, LocalDateTime.of(2023, 2, 24, 12, 0));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int id = Integer.parseInt(response.body());

            Task task2 = new Task(Status.NEW, "Задача",
                    "Создать задачу для тестирования работоспособности приложения",
                    15L, LocalDateTime.of(2022, 2, 24, 12, 0));
            request = HttpRequest.newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int id2 = Integer.parseInt(response.body());

            Task task3 = new Task(Status.NEW, "Задача",
                    "Создать задачу для тестирования работоспособности приложения",
                    15L, LocalDateTime.of(2024, 2, 24, 12, 0));
            request = HttpRequest.newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int id3 = Integer.parseInt(response.body());

            url = URI.create(TASK_URL + "?id=" + id);
            request = HttpRequest.newBuilder().uri(url).GET().build();
            client.send(request, HttpResponse.BodyHandlers.ofString());

            url = URI.create(TASK_URL + "?id=" + id2);
            request = HttpRequest.newBuilder().uri(url).GET().build();
            client.send(request, HttpResponse.BodyHandlers.ofString());

            url = URI.create(TASK_URL + "?id=" + id3);
            request = HttpRequest.newBuilder().uri(url).GET().build();
            client.send(request, HttpResponse.BodyHandlers.ofString());

            url = URI.create(PRIORITIZED_TASKS_URL);
            request = HttpRequest.newBuilder().uri(url).GET().build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode());
            assertFalse(response.body().isEmpty());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldGetAllSubtasksByEpic() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_URL);
        Epic epic = new Epic(Status.NEW, "Эпик 1", "Осуществить мечту", 0, null);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int id = Integer.parseInt(response.body());

            Subtask subtask = new Subtask(Status.NEW, id, "Подзадача 1 эпика 1",
                    "Победить всех злодеев в мире",
                    15L, LocalDateTime.of(2023, 02, 23, 13, 0));
            url = URI.create(SUBTASK_URL);

            request = HttpRequest.newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            id = Integer.parseInt(response.body());

            url = URI.create(EPIC_SUBTASKS_URL + id);

            System.out.println(url);
            request = HttpRequest.newBuilder()
                    .uri(url)
                    .GET()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}