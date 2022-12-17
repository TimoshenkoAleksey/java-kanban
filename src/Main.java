import manager.Manager;
import tasks.Epic;
import tasks.Subtask;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        int task1 = manager.createTask(new Epic(manager.getCounterId(), "Задача",
                "Создать задачу для тестирования работоспособности приложения"));
        int task2 = manager.createTask(new Epic(manager.getCounterId(), "Футбол",
                "Посмотреть финал чемпионата мира по футболу"));
        int epic1 = manager.createEpic(new Epic(manager.getCounterId(), "Мечта",
                "Осуществить мечту"));
        int subtask1 = manager.createSubTask(new Subtask(manager.getCounterId(),
                manager.getEpics().get(epic1).getTaskId(),
                "Победить зло", "Победить всех злодеев в мире"));
        int subtask2 = manager.createSubTask(new Subtask(manager.getCounterId(),
                manager.getEpics().get(epic1).getTaskId(),
                "Лосьон для волос", "Выпустить собствнную линейку лосьонов для волос"));
        int epic2 = manager.createEpic(new Epic(manager.getCounterId(), "Бегать по утрам",
                "Начать бегать по утрам каждый день"));
        int subtask3 = manager.createSubTask(new Subtask(manager.getCounterId(),
                manager.getEpics().get(epic2).getTaskId(),
                "Дождаться понедельника",
                "Дождаться понедельника и перенести на следующий понедельник"));

        System.out.println("Задачи:");
        for (String myTask : manager.getAllTasks()) {
            System.out.println(myTask);
        }
        System.out.println("Эпики:");
        for (String myTask : manager.getAllEpics()) {
            System.out.println(myTask);
        }
        System.out.println("Подзадачи:");
        for (String myTask : manager.getAllSubtasks()) {
            System.out.println(myTask);
        }

        manager.updateTask(subtask1, new Subtask(manager.getCounterId(), manager.getEpics().get(epic1).getTaskId(),
                "Победить зло", "Победить всех злодеев в мире"), "DONE");
        manager.updateTask(subtask2, new Subtask(manager.getCounterId(), manager.getEpics().get(epic1).getTaskId(),
                        "Лосьон для волос", "Выпустить собствнную линейку лосьенов для волос"),
                "DONE");

        System.out.println("Задачи:");
        for (String myTask : manager.getAllTasks()) {
            System.out.println(myTask);
        }
        System.out.println("Эпики:");
        for (String myTask : manager.getAllEpics()) {
            System.out.println(myTask);
        }
        System.out.println("Подзадачи:");
        for (String myTask : manager.getAllSubtasks()) {
            System.out.println(myTask);
        }

        manager.deleteOneTask(task1);
        manager.deleteOneTask(epic2);
    }
}
