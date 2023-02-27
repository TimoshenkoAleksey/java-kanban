import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.model.task.Epic;
import project.model.task.Status;
import project.model.task.Subtask;
import project.model.task.Task;
import project.service.HistoryManager;
import project.service.InMemoryTaskManager;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    private InMemoryTaskManager inMemoryTaskManager;
    private HistoryManager historyManager;

    @BeforeEach
    void BeforeEach() {
        inMemoryTaskManager = new InMemoryTaskManager();
        historyManager = inMemoryTaskManager.getHistoryManager();
    }

    @Test
    void addOneTask() {
        int task1 = inMemoryTaskManager.createTask(new Task(Status.NEW, "Задача",
                "Создать задачу для тестирования работоспособности приложения",
                15L, LocalDateTime.of(2023,02,24,12,0)));
        inMemoryTaskManager.getById(task1);
        assertEquals(1, inMemoryTaskManager.getHistoryManager().getHistory().size());
    }

    @Test
    void addTwoIdenticalTasks() {
        int task1 = inMemoryTaskManager.createTask(new Task(Status.NEW, "Задача",
                "Создать задачу для тестирования работоспособности приложения",
                15L, LocalDateTime.of(2023,02,24,12,0)));
        inMemoryTaskManager.getById(task1);
        inMemoryTaskManager.getById(task1);
        assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    void removeOneTaskFromEmptyList() {
        int task1 = inMemoryTaskManager.createTask(new Task(Status.NEW, "Задача",
                "Создать задачу для тестирования работоспособности приложения",
                15L, LocalDateTime.of(2023,02,24,12,0)));
        int task2 = inMemoryTaskManager.createTask(new Task(Status.NEW, "Футбол",
                "Посмотреть финал чемпионата мира по футболу",
                15L, LocalDateTime.of(2023,02,23,12,0)));
        int epic1 = inMemoryTaskManager.createEpic(new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту",0, null));
        int subtask1 = inMemoryTaskManager.createSubTask(new Subtask(Status.NEW, epic1, "Подзадача 1 эпика 1",
                "Победить всех злодеев в мире",
                15L, LocalDateTime.of(2023,02,23,13,0)));
        int subtask2 = inMemoryTaskManager.createSubTask(new Subtask(Status.NEW, epic1, "Лосьон для волос",
                "Выпустить собственную линейку лосьонов для волос",
                15L, LocalDateTime.of(2023,02,23,14,0)));
        int startSize = historyManager.getHistory().size();
        historyManager.remove(task1);
        assertTrue(historyManager.getHistory().isEmpty());
        assertEquals(startSize, historyManager.getHistory().size());
    }

    @Test
    void removeFirstTask() {
        int task1 = inMemoryTaskManager.createTask(new Task(Status.NEW, "Задача",
                "Создать задачу для тестирования работоспособности приложения",
                15L, LocalDateTime.of(2023,02,24,12,0)));
        int task2 = inMemoryTaskManager.createTask(new Task(Status.NEW, "Футбол",
                "Посмотреть финал чемпионата мира по футболу",
                15L, LocalDateTime.of(2023,02,23,12,0)));
        int epic1 = inMemoryTaskManager.createEpic(new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту",0, null));
        int subtask1 = inMemoryTaskManager.createSubTask(new Subtask(Status.NEW, epic1, "Подзадача 1 эпика 1",
                "Победить всех злодеев в мире",
                15L, LocalDateTime.of(2023,02,23,13,0)));
        int subtask2 = inMemoryTaskManager.createSubTask(new Subtask(Status.NEW, epic1, "Лосьон для волос",
                "Выпустить собственную линейку лосьонов для волос",
                15L, LocalDateTime.of(2023,02,23,14,0)));
        inMemoryTaskManager.getById(task1);
        inMemoryTaskManager.getById(task2);
        inMemoryTaskManager.getById(epic1);
        inMemoryTaskManager.getById(subtask1);
        inMemoryTaskManager.getById(subtask2);
        int startSize = historyManager.getHistory().size();
        historyManager.remove(task1);
        assertEquals(startSize - 1, historyManager.getHistory().size());
        assertFalse(historyManager.getHistory().contains(task1));
    }

    @Test
    void removeMiddleTask() {
        int task1 = inMemoryTaskManager.createTask(new Task(Status.NEW, "Задача",
                "Создать задачу для тестирования работоспособности приложения",
                15L, LocalDateTime.of(2023,02,24,12,0)));
        int task2 = inMemoryTaskManager.createTask(new Task(Status.NEW, "Футбол",
                "Посмотреть финал чемпионата мира по футболу",
                15L, LocalDateTime.of(2023,02,23,12,0)));
        int epic1 = inMemoryTaskManager.createEpic(new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту",0, null));
        int subtask1 = inMemoryTaskManager.createSubTask(new Subtask(Status.NEW, epic1, "Подзадача 1 эпика 1",
                "Победить всех злодеев в мире",
                15L, LocalDateTime.of(2023,02,23,13,0)));
        int subtask2 = inMemoryTaskManager.createSubTask(new Subtask(Status.NEW, epic1, "Лосьон для волос",
                "Выпустить собственную линейку лосьонов для волос",
                15L, LocalDateTime.of(2023,02,23,14,0)));
        inMemoryTaskManager.getById(task1);
        inMemoryTaskManager.getById(task2);
        inMemoryTaskManager.getById(epic1);
        inMemoryTaskManager.getById(subtask1);
        inMemoryTaskManager.getById(subtask2);
        int startSize = historyManager.getHistory().size();
        historyManager.remove(task2);
        assertEquals(startSize - 1, historyManager.getHistory().size());
        assertFalse(historyManager.getHistory().contains(task2));
    }

    @Test
    void removeLastTask() {
        int task1 = inMemoryTaskManager.createTask(new Task(Status.NEW, "Задача",
                "Создать задачу для тестирования работоспособности приложения",
                15L, LocalDateTime.of(2023,02,24,12,0)));
        int task2 = inMemoryTaskManager.createTask(new Task(Status.NEW, "Футбол",
                "Посмотреть финал чемпионата мира по футболу",
                15L, LocalDateTime.of(2023,02,23,12,0)));
        int epic1 = inMemoryTaskManager.createEpic(new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту",0, null));
        int subtask1 = inMemoryTaskManager.createSubTask(new Subtask(Status.NEW, epic1, "Подзадача 1 эпика 1",
                "Победить всех злодеев в мире",
                15L, LocalDateTime.of(2023,02,23,13,0)));
        int subtask2 = inMemoryTaskManager.createSubTask(new Subtask(Status.NEW, epic1, "Лосьон для волос",
                "Выпустить собственную линейку лосьонов для волос",
                15L, LocalDateTime.of(2023,02,23,14,0)));
        inMemoryTaskManager.getById(task1);
        inMemoryTaskManager.getById(task2);
        inMemoryTaskManager.getById(epic1);
        inMemoryTaskManager.getById(subtask1);
        inMemoryTaskManager.getById(subtask2);
        int startSize = historyManager.getHistory().size();
        historyManager.remove(subtask2);
        assertEquals(startSize - 1, historyManager.getHistory().size());
        assertFalse(historyManager.getHistory().contains(subtask2));
    }

    @Test
    void getHistoryFromEmptyList() {
        int task1 = inMemoryTaskManager.createTask(new Task(Status.NEW, "Задача",
                "Создать задачу для тестирования работоспособности приложения",
                15L, LocalDateTime.of(2023,02,24,12,0)));
        int task2 = inMemoryTaskManager.createTask(new Task(Status.NEW, "Футбол",
                "Посмотреть финал чемпионата мира по футболу",
                15L, LocalDateTime.of(2023,02,23,12,0)));
        int epic1 = inMemoryTaskManager.createEpic(new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту",0, null));
        int subtask1 = inMemoryTaskManager.createSubTask(new Subtask(Status.NEW, epic1, "Подзадача 1 эпика 1",
                "Победить всех злодеев в мире",
                15L, LocalDateTime.of(2023,02,23,13,0)));
        int subtask2 = inMemoryTaskManager.createSubTask(new Subtask(Status.NEW, epic1, "Лосьон для волос",
                "Выпустить собственную линейку лосьонов для волос",
                15L, LocalDateTime.of(2023,02,23,14,0)));
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void getHistoryFromNotEmptyList() {
        int task1 = inMemoryTaskManager.createTask(new Task(Status.NEW, "Задача",
                "Создать задачу для тестирования работоспособности приложения",
                15L, LocalDateTime.of(2023,02,24,12,0)));
        int task2 = inMemoryTaskManager.createTask(new Task(Status.NEW, "Футбол",
                "Посмотреть финал чемпионата мира по футболу",
                15L, LocalDateTime.of(2023,02,23,12,0)));
        int epic1 = inMemoryTaskManager.createEpic(new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту",0, null));
        int subtask1 = inMemoryTaskManager.createSubTask(new Subtask(Status.NEW, epic1, "Подзадача 1 эпика 1",
                "Победить всех злодеев в мире",
                15L, LocalDateTime.of(2023,02,23,13,0)));
        int subtask2 = inMemoryTaskManager.createSubTask(new Subtask(Status.NEW, epic1, "Лосьон для волос",
                "Выпустить собственную линейку лосьонов для волос",
                15L, LocalDateTime.of(2023,02,23,14,0)));
        inMemoryTaskManager.getById(task1);
        inMemoryTaskManager.getById(task2);
        inMemoryTaskManager.getById(epic1);
        inMemoryTaskManager.getById(subtask1);
        inMemoryTaskManager.getById(subtask2);
        assertFalse(historyManager.getHistory().isEmpty());
        assertEquals(5, historyManager.getHistory().size());
    }
}