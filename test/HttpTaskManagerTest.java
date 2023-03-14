import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.model.task.Epic;
import project.model.task.Status;
import project.model.task.Subtask;
import project.model.task.Task;
import project.server.KVServer;
import project.service.HttpTaskManager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    static KVServer kvServer;
    private String url;

    @BeforeAll
    static void globalSetUp() {
        try {
            kvServer = new KVServer();
            kvServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() {
        url = "http://localhost:";
        taskManager = new HttpTaskManager(url);
        taskManager.deleteAllTasks();
    }

    @Test
    public void shouldSaveAndLoad() {
        int taskId1 = taskManager.createTask(new Task(Status.NEW, "Задача",
                "Создать задачу для тестирования работоспособности приложения",
                15L, LocalDateTime.of(2023, Month.JULY, 24, 12, 0)));
        int taskId2 = taskManager.createTask(new Task(Status.NEW, "Футбол",
                "Посмотреть финал чемпионата мира по футболу",
                15L, LocalDateTime.of(2023, Month.JULY, 23, 12, 0)));
        int epicId = taskManager.createEpic(new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту", 0, LocalDateTime.now()));
        taskManager.createSubTask(new Subtask(Status.NEW, epicId, "Подзадача 1 эпика 1",
                "Победить всех злодеев в мире",
                15L, LocalDateTime.of(2023, 2, 23, 13, 0)));
        taskManager.getById(epicId);
        taskManager.getById(taskId1);
        taskManager.getById(taskId2);
        HttpTaskManager newManager = new HttpTaskManager(url);
        newManager.load();
        assertEquals(taskManager.getHistory(), newManager.getHistory());
        assertEquals(taskManager.getEpics(), newManager.getEpics());
        assertEquals(taskManager.getSubtasks(), newManager.getSubtasks());
        assertEquals(taskManager.getTasks(), newManager.getTasks());
        kvServer.stop();
    }
}
