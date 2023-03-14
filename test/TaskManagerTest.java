import org.junit.jupiter.api.Test;
import project.model.task.Epic;
import project.model.task.Status;
import project.model.task.Subtask;
import project.model.task.Task;
import project.service.HistoryManager;
import project.service.TaskManager;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    @Test
    void deleteAllTasksStandart() {
        int task1 = taskManager.createTask(new Task(Status.NEW, "Задача",
                "Создать задачу для тестирования работоспособности приложения",
                15L, LocalDateTime.of(2023, 02, 24, 12, 0)));
        int task2 = taskManager.createTask(new Task(Status.NEW, "Футбол",
                "Посмотреть финал чемпионата мира по футболу",
                15L, LocalDateTime.of(2023, 02, 23, 12, 0)));
        int epic1 = taskManager.createEpic(new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту", 0, null));
        int subtask1 = taskManager.createSubTask(new Subtask(Status.NEW, epic1, "Подзадача 1 эпика 1",
                "Победить всех злодеев в мире",
                15L, LocalDateTime.of(2023, 02, 23, 13, 0)));
        int subtask2 = taskManager.createSubTask(new Subtask(Status.NEW, epic1, "Лосьон для волос",
                "Выпустить собственную линейку лосьонов для волос",
                15L, LocalDateTime.of(2023, 02, 23, 14, 0)));
        int subtask3 = taskManager.createSubTask(new Subtask(Status.NEW, epic1, "Полет на ракете",
                "Пока ракеты нет, выйти на улицу и взорвать петарду",
                15L, LocalDateTime.of(2023, 02, 23, 15, 0)));
        int epic2 = taskManager.createEpic(new Epic(Status.NEW, "Бегать по утрам",
                "Начать бегать по утрам каждый день", 0, null));
        int subtask4 = taskManager.createSubTask(new Subtask(Status.NEW, epic2, "Дождаться понедельника",
                "Дождаться понедельника и перенести на следующий понедельник",
                15L, LocalDateTime.of(2023, 02, 23, 15, 0)));
        taskManager.deleteAllTasks();
        assertEquals(0, taskManager.getAllTasks().size());
        assertEquals(0, taskManager.getAllEpics().size());
        assertEquals(0, taskManager.getAllTasks().size());
    }

    @Test
    void deleteAllTasksForEmptyList() {
        taskManager.deleteAllTasks();
        assertEquals(0, taskManager.getAllTasks().size());
        assertEquals(0, taskManager.getAllEpics().size());
        assertEquals(0, taskManager.getAllSubtasks().size());
    }

    @Test
    void getByIdForTask() {
        int task1 = taskManager.createTask(new Task(Status.NEW, "Задача",
                "Создать задачу для тестирования работоспособности приложения",
                15L, LocalDateTime.of(2023, 2, 24, 12, 0)));
        Task task = taskManager.getById(task1);
        assertNotNull(task);
        assertTrue(taskManager.getHistory().contains(task));
    }

    @Test
    void getByIdForEpic() {
        int epic1 = taskManager.createEpic(new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту", 0, null));
        Task task = taskManager.getById(epic1);
        assertNotNull(task);
        assertTrue(taskManager.getHistory().contains(task));
    }

    @Test
    void getByIdForSubtask() {
        int epic1 = taskManager.createEpic(new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту", 0, null));
        int subtask1 = taskManager.createSubTask(new Subtask(Status.NEW, epic1, "Подзадача 1 эпика 1",
                "Победить всех злодеев в мире",
                15L, LocalDateTime.of(2023, 02, 23, 13, 0)));
        Task task = taskManager.getById(subtask1);
        assertNotNull(task);
        assertTrue(taskManager.getHistory().contains(task));
    }

    @Test
    void getByIdForEmptyList() {
        Task task = taskManager.getById(100500);
        assertNull(task);
        assertFalse(taskManager.getHistory().contains(task));
    }

    @Test
    void getByIdForWrongIndex() {
        int epic1 = taskManager.createEpic(new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту", 0, null));
        int subtask1 = taskManager.createSubTask(new Subtask(Status.NEW, epic1, "Подзадача 1 эпика 1",
                "Победить всех злодеев в мире",
                15L, LocalDateTime.of(2023, 02, 23, 13, 0)));
        Task task = taskManager.getById(100500);
        assertNull(task);
        assertFalse(taskManager.getHistory().contains(task));
    }

    @Test
    void createTaskStandard() {
        Task task = new Task(Status.NEW, "Задача",
                "Создать задачу для тестирования работоспособности приложения",
                15L, LocalDateTime.of(2023, 02, 24, 12, 0));
        taskManager.createTask(task);
        assertTrue(taskManager.getAllTasks().contains(task));
    }

    @Test
    void createTaskWithEmptyList() {
        Task task = new Task(Status.NEW, "Задача",
                "Создать задачу для тестирования работоспособности приложения",
                15L, LocalDateTime.of(2023, 02, 24, 12, 0));
        taskManager.createTask(task);
        assertTrue(taskManager.getAllTasks().contains(task));
    }

    @Test
    void createTaskNullObject() {
        int task1 = taskManager.createTask(null);
        assertFalse(taskManager.getAllTasks().contains(null));
    }

    //createEpic(Epic epic)
    @Test
    void createEpicStandard() {
        Epic epic = new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту", 0, null);
        taskManager.createEpic(epic);
        assertTrue(taskManager.getAllEpics().contains(epic));
    }

    @Test
    void createEpicWithEmptyList() {
        Epic epic = new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту", 0, null);
        taskManager.createEpic(epic);
        assertTrue(taskManager.getAllEpics().contains(epic));
    }

    @Test
    void createEpicNullObject() {
        int epic1 = taskManager.createEpic(null);
        assertEquals(0, taskManager.getAllEpics().size());
    }

    @Test
    void createSubTaskStandard() {
        int epic1 = taskManager.createEpic(new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту", 0, null));
        Subtask subtask = new Subtask(Status.NEW, epic1, "Подзадача 1 эпика 1",
                "Победить всех злодеев в мире",
                15L, LocalDateTime.of(2023, 02, 23, 13, 0));
        int subtask1 = taskManager.createSubTask(subtask);
        assertTrue(taskManager.getAllSubtasks().contains(subtask));
    }

    @Test
    void createSubTaskWithEmptyList() {
        int epic1 = taskManager.createEpic(new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту", 0, null));
        Subtask subtask = new Subtask(Status.NEW, epic1, "Подзадача 1 эпика 1",
                "Победить всех злодеев в мире",
                15L, LocalDateTime.of(2023, 02, 23, 13, 0));
        int subtask1 = taskManager.createSubTask(subtask);
        assertTrue(taskManager.getAllSubtasks().contains(subtask));
    }

    @Test
    void createSubTaskWithNullObject() {
        int epic1 = taskManager.createEpic(new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту", 0, null));
        int subtask1 = taskManager.createSubTask(null);
        assertFalse(taskManager.getAllSubtasks().contains(null));
    }

    @Test
    void createSubTaskWithoutEpic() {
        Subtask subtask = new Subtask(Status.NEW, 100500, "Подзадача 1 эпика 1",
                "Победить всех злодеев в мире",
                15L, LocalDateTime.of(2023, 02, 23, 13, 0));
        int subtask1 = taskManager.createSubTask(subtask);
        assertFalse(taskManager.getAllSubtasks().contains(subtask));
        assertEquals(-1, subtask1);
    }

    @Test
    void updateTaskForTask() {
        Task task = new Task(Status.NEW, "Задача",
                "Создать задачу для тестирования работоспособности приложения",
                15L, LocalDateTime.of(2023, 02, 24, 12, 0));
        int task1 = taskManager.createTask(task);
        taskManager.updateTask(task1, task, Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, task.getStatus());
    }

    @Test
    void updateTaskForSubtask() {
        Epic epic = new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту", 0, null);
        int epic1 = taskManager.createEpic(epic);
        Subtask subtask = new Subtask(Status.NEW, epic1, "Подзадача 1 эпика 1",
                "Победить всех злодеев в мире",
                15L, LocalDateTime.of(2023, 02, 23, 13, 0));
        int subtask1 = taskManager.createSubTask(subtask);
        taskManager.updateTask(subtask1, subtask, Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, subtask.getStatus());
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void updateTaskForEmptyList() {
        taskManager.updateTask(100500, null, Status.IN_PROGRESS);
        assertFalse(taskManager.getHistory().contains(null));
    }

    @Test
    void updateTaskForWrongIndex() {
        int epic1 = taskManager.createEpic(new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту", 0, null));
        Subtask subtask = new Subtask(Status.NEW, epic1, "Подзадача 1 эпика 1",
                "Победить всех злодеев в мире",
                15L, LocalDateTime.of(2023, 02, 23, 13, 0));
        int subtask1 = taskManager.createSubTask(subtask);
        taskManager.updateTask(100500, subtask, Status.IN_PROGRESS);
        assertEquals(Status.NEW, subtask.getStatus());
    }

    @Test
    void deleteOneTaskForTask() {
        int task1 = taskManager.createTask(new Task(Status.NEW, "Задача",
                "Создать задачу для тестирования работоспособности приложения",
                15L, LocalDateTime.of(2023, 02, 24, 12, 0)));
        taskManager.deleteOneTask(task1);
        assertEquals(0, taskManager.getAllTasks().size());
    }

    @Test
    void deleteOneTaskForEpic() {
        int epic1 = taskManager.createEpic(new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту", 0, null));
        taskManager.deleteOneTask(epic1);
        assertEquals(0, taskManager.getAllEpics().size());
    }

    @Test
    void deleteOneTaskForEpicAndSubtask() {
        int epic1 = taskManager.createEpic(new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту", 0, null));
        int subtask1 = taskManager.createSubTask(new Subtask(Status.NEW, epic1, "Подзадача 1 эпика 1",
                "Победить всех злодеев в мире",
                15L, LocalDateTime.of(2023, 02, 23, 13, 0)));
        taskManager.deleteOneTask(epic1);
        assertEquals(0, taskManager.getAllEpics().size());
        assertEquals(0, taskManager.getAllSubtasks().size());
    }

    @Test
    void deleteOneTaskForEmptyList() {
        taskManager.deleteOneTask(100500);
        assertEquals(0, taskManager.getAllTasks().size());
        assertEquals(0, taskManager.getAllEpics().size());
        assertEquals(0, taskManager.getAllSubtasks().size());
    }

    @Test
    void deleteOneTaskForWrongIndex() {
        int epic1 = taskManager.createEpic(new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту", 0, null));
        int subtask1 = taskManager.createSubTask(new Subtask(Status.NEW, epic1, "Подзадача 1 эпика 1",
                "Победить всех злодеев в мире",
                15L, LocalDateTime.of(2023, 02, 23, 13, 0)));
        taskManager.deleteOneTask(100500);
        assertEquals(0, taskManager.getAllTasks().size());
        assertEquals(1, taskManager.getAllEpics().size());
        assertEquals(1, taskManager.getAllSubtasks().size());
    }

    @Test
    void getAllSubtasksByEpicOneEpicTwoSubtask() {
        int epic1 = taskManager.createEpic(new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту", 0, null));
        int subtask1 = taskManager.createSubTask(new Subtask(Status.NEW, epic1, "Подзадача 1 эпика 1",
                "Победить всех злодеев в мире",
                15L, LocalDateTime.of(2023, 02, 23, 13, 0)));
        int subtask2 = taskManager.createSubTask(new Subtask(Status.NEW, epic1, "Лосьон для волос",
                "Выпустить собственную линейку лосьонов для волос",
                15L, LocalDateTime.of(2023, 02, 23, 14, 0)));
        List<String> list = taskManager.getAllSubtasksByEpic(epic1);
        assertEquals(2, list.size());
    }

    @Test
    void getAllSubtasksByEpicEmptyList() {
        int epic1 = taskManager.createEpic(new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту", 0, null));
        List<String> list = taskManager.getAllSubtasksByEpic(epic1);
        assertEquals(0, list.size());
    }

    @Test
    void getAllSubtasksByEpicOneEpicTwoSubtaskWrongId() {
        int epic1 = taskManager.createEpic(new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту", 0, null));
        int subtask1 = taskManager.createSubTask(new Subtask(Status.NEW, epic1, "Подзадача 1 эпика 1",
                "Победить всех злодеев в мире",
                15L, LocalDateTime.of(2023, 02, 23, 13, 0)));
        int subtask2 = taskManager.createSubTask(new Subtask(Status.NEW, epic1, "Лосьон для волос",
                "Выпустить собственную линейку лосьонов для волос",
                15L, LocalDateTime.of(2023, 02, 23, 14, 0)));
        List<String> list = taskManager.getAllSubtasksByEpic(100500);
        assertEquals(0, list.size());
    }

    @Test
    void epicStatusCalculationForEmptySubtasksList() {
        Epic epic = new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту", 0, null);
        int epic1 = taskManager.createEpic(epic);
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    void epicStatusCalculationForAllSubtasksStatusNew() {
        Epic epic = new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту", 0, null);
        int epic1 = taskManager.createEpic(epic);
        int subtask1 = taskManager.createSubTask(new Subtask(Status.NEW, epic1, "Подзадача 1 эпика 1",
                "Победить всех злодеев в мире",
                15L, LocalDateTime.of(2023, 02, 23, 13, 0)));
        int subtask2 = taskManager.createSubTask(new Subtask(Status.NEW, epic1, "Лосьон для волос",
                "Выпустить собственную линейку лосьонов для волос",
                15L, LocalDateTime.of(2023, 02, 23, 14, 0)));
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    void epicStatusCalculationForAllSubtasksStatusDone() {
        Epic epic = new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту", 0, null);
        int epic1 = taskManager.createEpic(epic);
        int subtask1 = taskManager.createSubTask(new Subtask(Status.NEW, epic1, "Подзадача 1 эпика 1",
                "Победить всех злодеев в мире",
                15L, LocalDateTime.of(2023, 02, 23, 13, 0)));
        int subtask2 = taskManager.createSubTask(new Subtask(Status.NEW, epic1, "Лосьон для волос",
                "Выпустить собственную линейку лосьонов для волос",
                15L, LocalDateTime.of(2023, 02, 23, 14, 0)));
        taskManager.updateTask(subtask1, new Subtask(Status.NEW, epic1, "Подзадача 1 эпика 1",
                "Победить всех злодеев в мире",
                15L, LocalDateTime.of(2023, 02, 23, 13, 0)), Status.DONE);
        taskManager.updateTask(subtask2, new Subtask(Status.NEW, epic1, "Лосьон для волос",
                "Выпустить собственную линейку лосьонов для волос",
                15L, LocalDateTime.of(2023, 02, 23, 14, 0)), Status.DONE);
        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    void epicStatusCalculationForAllSubtasksStatusNewAndDone() {
        Epic epic = new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту", 0, null);
        int epic1 = taskManager.createEpic(epic);
        int subtask1 = taskManager.createSubTask(new Subtask(Status.NEW, epic1, "Подзадача 1 эпика 1",
                "Победить всех злодеев в мире",
                15L, LocalDateTime.of(2023, 02, 23, 13, 0)));
        int subtask2 = taskManager.createSubTask(new Subtask(Status.NEW, epic1, "Лосьон для волос",
                "Выпустить собственную линейку лосьонов для волос",
                15L, LocalDateTime.of(2023, 02, 23, 14, 0)));
        taskManager.updateTask(subtask1, new Subtask(Status.NEW, epic1, "Подзадача 1 эпика 1",
                "Победить всех злодеев в мире",
                15L, LocalDateTime.of(2023, 02, 23, 13, 0)), Status.DONE);
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    void epicStatusCalculationForAllSubtasksStatusInProgress() {
        Epic epic = new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту", 0, null);
        int epic1 = taskManager.createEpic(epic);
        int subtask1 = taskManager.createSubTask(new Subtask(Status.NEW, epic1, "Подзадача 1 эпика 1",
                "Победить всех злодеев в мире",
                15L, LocalDateTime.of(2023, 02, 23, 13, 0)));
        int subtask2 = taskManager.createSubTask(new Subtask(Status.NEW, epic1, "Лосьон для волос",
                "Выпустить собственную линейку лосьонов для волос",
                15L, LocalDateTime.of(2023, 02, 23, 14, 0)));
        taskManager.updateTask(subtask1, new Subtask(Status.NEW, epic1, "Подзадача 1 эпика 1",
                "Победить всех злодеев в мире",
                15L, LocalDateTime.of(2023, 02, 23, 13, 0)), Status.IN_PROGRESS);
        taskManager.updateTask(subtask2, new Subtask(Status.NEW, epic1, "Лосьон для волос",
                "Выпустить собственную линейку лосьонов для волос",
                15L, LocalDateTime.of(2023, 02, 23, 14, 0)), Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void checkEpicStartAndEndTimeForOneEpicWithoutSubtasks() {
        Epic epic = new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту", 0, null);
        int epic1 = taskManager.createEpic(epic);
        epic.checkEpicStartAndEndTime();
        assertNull(epic.getStartTime());
        assertNull(epic.getEndTime());
        assertEquals(0, epic.getDuration());
    }

    @Test
    void checkEpicStartAndEndTimeForOneEpicOneSubtasks() {
        Epic epic = new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту", 0, null);
        int epic1 = taskManager.createEpic(epic);
        Subtask subtask = new Subtask(Status.NEW, epic1, "Подзадача 1 эпика 1",
                "Победить всех злодеев в мире",
                15L, LocalDateTime.of(2023, 02, 23, 13, 0));
        int subtask1 = taskManager.createSubTask(subtask);
        assertEquals(subtask.getStartTime(), epic.getStartTime());
        assertEquals(subtask.getEndTime(), epic.getEndTime());
        assertEquals(subtask.getDuration(), epic.getDuration());
    }

    @Test
    void checkEpicStartAndEndTimeForOneEpicTwoSubtasks() {
        Epic epic = new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту", 0, null);
        int epic1 = taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask(Status.NEW, epic1, "Подзадача 1 эпика 1",
                "Победить всех злодеев в мире",
                15L, LocalDateTime.of(2023, 02, 23, 13, 0));
        int subtaskId1 = taskManager.createSubTask(subtask1);
        Subtask subtask2 = new Subtask(Status.NEW, epic1, "Лосьон для волос",
                "Выпустить собственную линейку лосьонов для волос",
                15L, LocalDateTime.of(2023, 02, 23, 14, 0));
        int subtaskId2 = taskManager.createSubTask(subtask2);

        assertEquals(subtask1.getStartTime(), epic.getStartTime());
        assertEquals(subtask2.getEndTime(), epic.getEndTime());
        assertEquals(subtask1.getDuration() + subtask2.getDuration(), epic.getDuration());
    }

    @Test
    void getSortedTasksTest() {
        Task task1 = new Task(Status.NEW, "Задача",
                "Создать задачу для тестирования работоспособности приложения",
                15L, LocalDateTime.of(2023, 02, 24, 12, 0));
        taskManager.createTask(task1);
        Task task2 = new Task(Status.NEW, "Футбол", "Посмотреть финал чемпионата мира по футболу",
                15L, LocalDateTime.of(2023, 02, 23, 12, 0));
        taskManager.createTask(task2);
        Epic epic = new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту", 0, null);
        int epic1 = taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask(Status.NEW, epic1, "Подзадача 1 эпика 1",
                "Победить всех злодеев в мире",
                15L, LocalDateTime.of(2023, 02, 23, 13, 0));
        taskManager.createSubTask(subtask1);
        Subtask subtask2 = new Subtask(Status.NEW, epic1, "Лосьон для волос",
                "Выпустить собственную линейку лосьонов для волос",
                15L, LocalDateTime.of(2023, 02, 23, 14, 0));
        taskManager.createSubTask(subtask2);

        final List<Task> standartList = Arrays.asList(task2, subtask1, subtask2, task1);
        List<Task> list = taskManager.getSortedTasks();
        assertEquals(standartList, list);
    }
}