import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        int task1 = inMemoryTaskManager.createTask(new Task(inMemoryTaskManager.getNewId(), "Задача",
                "Создать задачу для тестирования работоспособности приложения"));
        int task2 = inMemoryTaskManager.createTask(new Task(inMemoryTaskManager.getNewId(), "Футбол",
                "Посмотреть финал чемпионата мира по футболу"));
        int epic1 = inMemoryTaskManager.createEpic(new Epic(inMemoryTaskManager.getNewId(), "Мечта",
                "Осуществить мечту"));
        int subtask1 = inMemoryTaskManager.createSubTask(new Subtask(inMemoryTaskManager.getNewId(),
                inMemoryTaskManager.getEpics().get(epic1).getId(),
                "Победить зло", "Победить всех злодеев в мире"));
        int subtask2 = inMemoryTaskManager.createSubTask(new Subtask(inMemoryTaskManager.getNewId(),
                inMemoryTaskManager.getEpics().get(epic1).getId(),
                "Лосьон для волос", "Выпустить собствнную линейку лосьонов для волос"));
        int epic2 = inMemoryTaskManager.createEpic(new Epic(inMemoryTaskManager.getNewId(), "Бегать по утрам",
                "Начать бегать по утрам каждый день"));
        int subtask3 = inMemoryTaskManager.createSubTask(new Subtask(inMemoryTaskManager.getNewId(),
                inMemoryTaskManager.getEpics().get(epic2).getId(),
                "Дождаться понедельника",
                "Дождаться понедельника и перенести на следующий понедельник"));

        System.out.println("Получить по id: \n" + inMemoryTaskManager.getById(1));
        System.out.println("История запросов: \n" + inMemoryTaskManager.getHistoryManager().getHistory());
        System.out.println("Получить по id: \n" + inMemoryTaskManager.getById(2));
        System.out.println("История запросов: \n" + inMemoryTaskManager.getHistoryManager().getHistory());
        System.out.println("Получить по id: \n" + inMemoryTaskManager.getById(3));
        System.out.println("История запросов: \n" + inMemoryTaskManager.getHistoryManager().getHistory());

        for (int i = 1; i < 9; i++) {
            System.out.println("Получить по id: \n" + inMemoryTaskManager.getById(1));
            System.out.println("История запросов: \n" + inMemoryTaskManager.getHistoryManager().getHistory());
        }

        System.out.println("Задачи:");
        for (Task myTask : inMemoryTaskManager.getAllTasks()) {
            System.out.println(myTask);
        }
        System.out.println("Эпики:");
        for (Epic myTask : inMemoryTaskManager.getAllEpics()) {
            System.out.println(myTask);
        }
        System.out.println("Подзадачи:");
        for (Subtask myTask : inMemoryTaskManager.getAllSubtasks()) {
            System.out.println(myTask);
        }

        inMemoryTaskManager.updateTask(subtask1, new Subtask(inMemoryTaskManager.getNewId(),
                inMemoryTaskManager.getEpics().get(epic1).getId(),
                "Победить зло", "Победить всех злодеев в мире"), Status.DONE);
        inMemoryTaskManager.updateTask(subtask2, new Subtask(inMemoryTaskManager.getNewId(),
                        inMemoryTaskManager.getEpics().get(epic1).getId(),
                        "Лосьон для волос", "Выпустить собствнную линейку лосьенов для волос"),
                Status.DONE);

        System.out.println("Задачи:");
        for (Task myTask : inMemoryTaskManager.getAllTasks()) {
            System.out.println(myTask);
        }
        System.out.println("Эпики:");
        for (Epic myTask : inMemoryTaskManager.getAllEpics()) {
            System.out.println(myTask);
        }
        System.out.println("Подзадачи:");
        for (Subtask myTask : inMemoryTaskManager.getAllSubtasks()) {
            System.out.println(myTask);
        }

        inMemoryTaskManager.deleteOneTask(task1);
        inMemoryTaskManager.deleteOneTask(epic2);
    }
}
