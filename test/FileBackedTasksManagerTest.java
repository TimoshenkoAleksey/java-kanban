import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.model.exception.ManagerSaveException;
import project.model.task.Epic;
import project.model.task.Status;
import project.model.task.Task;
import project.service.FileBackedTasksManager;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @BeforeEach
    void BeforeEach() {
        taskManager = new FileBackedTasksManager(new File(
                "src/resources/tasks.csv"));
        historyManager = taskManager.getHistoryManager();
    }

    @Test
    void verificationSavingAndRestoringStateForTwoTasks() {
        int task1 = taskManager.createTask(new Task(Status.NEW, "Задача",
                "Создать задачу для тестирования работоспособности приложения",
                15L, LocalDateTime.of(2023, 02, 24, 12, 0)));
        int task2 = taskManager.createTask(new Task(Status.NEW, "Футбол",
                "Посмотреть финал чемпионата мира по футболу",
                15L, LocalDateTime.of(2023, 02, 23, 12, 0)));
        FileBackedTasksManager fileBackedTasksManager2;
        try {
            fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(
                    new File("src/resources/tasks.csv"));
        } catch (IOException e) {
            throw new ManagerSaveException("Какая то ошибка.");
        }
        assertEquals(taskManager.getAllTasks().size(), fileBackedTasksManager2.getAllTasks().size());
    }

    @Test
    void verificationSavingAndRestoringStateForTwoTasksWithHistory() {
        int task1 = taskManager.createTask(new Task(Status.NEW, "Задача",
                "Создать задачу для тестирования работоспособности приложения",
                15L, LocalDateTime.of(2023, 02, 24, 12, 0)));
        int task2 = taskManager.createTask(new Task(Status.NEW, "Футбол",
                "Посмотреть финал чемпионата мира по футболу",
                15L, LocalDateTime.of(2023, 02, 23, 12, 0)));
        taskManager.getById(task1);
        FileBackedTasksManager fileBackedTasksManager2;
        try {
            fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(
                    new File("src/resources/tasks.csv"));
        } catch (IOException e) {
            throw new ManagerSaveException("Какая то ошибка.");
        }
        assertEquals(taskManager.getAllTasks().size(), fileBackedTasksManager2.getAllTasks().size());
        assertEquals(historyManager.getHistory().size(),
                fileBackedTasksManager2.getHistoryManager().getHistory().size());
    }

    @Test
    void verificationSavingAndRestoringStateForEmptyTasksList() {
        int task1 = taskManager.createTask(new Task(Status.NEW, "Задача",
                "Создать задачу для тестирования работоспособности приложения",
                15L, LocalDateTime.of(2023, 02, 24, 12, 0)));
        taskManager.deleteAllTasks();
        FileBackedTasksManager fileBackedTasksManager2;
        try {
            fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(
                    new File("src/resources/tasks.csv"));
        } catch (IOException e) {
            throw new ManagerSaveException("Какая то ошибка.");
        }
        assertEquals(taskManager.getAllTasks().size(), fileBackedTasksManager2.getAllTasks().size());
        assertEquals(historyManager.getHistory().size(),
                fileBackedTasksManager2.getHistoryManager().getHistory().size());
    }

    @Test
    void verificationSavingAndRestoringStateForEpicWithoutSubtasks() {
        int epic1 = taskManager.createEpic(new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту", 0, null));
        FileBackedTasksManager fileBackedTasksManager2;
        try {
            fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(
                    new File("src/resources/tasks.csv"));
        } catch (IOException e) {
            throw new ManagerSaveException("Какая то ошибка.");
        }
        assertEquals(taskManager.getAllTasks().size(), fileBackedTasksManager2.getAllTasks().size());
        assertEquals(historyManager.getHistory().size(),
                fileBackedTasksManager2.getHistoryManager().getHistory().size());
    }

    @Test
    void verificationSavingAndRestoringStateForEmptyHistoryList() {
        int task1 = taskManager.createTask(new Task(Status.NEW, "Задача",
                "Создать задачу для тестирования работоспособности приложения",
                15L, LocalDateTime.of(2023, 02, 24, 12, 0)));
        int task2 = taskManager.createTask(new Task(Status.NEW, "Футбол",
                "Посмотреть финал чемпионата мира по футболу",
                15L, LocalDateTime.of(2023, 02, 23, 12, 0)));
        FileBackedTasksManager fileBackedTasksManager2;
        try {
            fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(
                    new File("src/resources/tasks.csv"));
        } catch (IOException e) {
            throw new ManagerSaveException("Какая то ошибка.");
        }
        assertEquals(taskManager.getAllTasks().size(), fileBackedTasksManager2.getAllTasks().size());
        assertEquals(0, fileBackedTasksManager2.getHistoryManager().getHistory().size());
    }

    @Test
    void historyToStringOneTaskInHistory() {
        int task1 = taskManager.createTask(new Task(Status.NEW, "Задача",
                "Создать задачу для тестирования работоспособности приложения",
                15L, LocalDateTime.of(2023, 02, 24, 12, 0)));
        taskManager.getById(task1);
        String str = taskManager.historyToString(historyManager);
        assertNotEquals("", str);
        assertNotNull(str);
    }

    @Test
    void historyToStringTwoTasksInHistory() {
        int task1 = taskManager.createTask(new Task(Status.NEW, "Задача",
                "Создать задачу для тестирования работоспособности приложения",
                15L, LocalDateTime.of(2023, 02, 24, 12, 0)));
        int task2 = taskManager.createTask(new Task(Status.NEW, "Футбол",
                "Посмотреть финал чемпионата мира по футболу",
                15L, LocalDateTime.of(2023, 02, 23, 12, 0)));
        taskManager.getById(task1);
        taskManager.getById(task2);
        String str = taskManager.historyToString(historyManager);
        String[] strings = str.split(",");
        assertEquals(2, strings.length);
    }

    @Test
    void historyToStringForEmptyHistory() {
        int task1 = taskManager.createTask(new Task(Status.NEW, "Задача",
                "Создать задачу для тестирования работоспособности приложения",
                15L, LocalDateTime.of(2023, 02, 24, 12, 0)));
        int task2 = taskManager.createTask(new Task(Status.NEW, "Футбол",
                "Посмотреть финал чемпионата мира по футболу",
                15L, LocalDateTime.of(2023, 02, 23, 12, 0)));
        String str = taskManager.historyToString(historyManager);
        assertEquals("", str);
    }

    @Test
    void historyFromStringTest() {
        String value = "1,2,3,4,5";
        List<Integer> list = taskManager.historyFromString(value);
        assertNotNull(list);
        assertEquals(5, list.size());
    }
}
