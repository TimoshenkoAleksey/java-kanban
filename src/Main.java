import Manager.Manager;
import Tasks.Epic;
import Tasks.Subtask;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        int task1 = manager.createTask(new Epic(manager.counterID, "Задача",
                "Создать задачу для тестирования работоспособности приложения"));
        int task2 = manager.createTask(new Epic(manager.counterID, "Футбол",
                "Посмотреть финал фемпионата мира по футболу"));
        int epic1 = manager.createEpic(new Epic(manager.counterID, "Мечта",
                "Осуществить мечту"));
        int subtask1 = manager.createSubTask(new Subtask(manager.counterID, manager.epics.get(epic1).getTaskID(),
                "Победить зло", "Победить всех злодеев в мире"));
        int subtask2 = manager.createSubTask(new Subtask(manager.counterID, manager.epics.get(epic1).getTaskID(),
                "Лосьон для волос", "Выпустить собствнную линейку лосьонов для волос"));
        int epic2 = manager.createEpic(new Epic(manager.counterID, "Бегать по утрам",
                "Начать бегать по утрам каждый день"));
        int subtask3 = manager.createSubTask(new Subtask(manager.counterID, manager.epics.get(epic2).getTaskID(),
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

        manager.updateTask(subtask1, new Subtask(manager.counterID, manager.epics.get(epic1).getTaskID(),
                "Победить зло", "Победить всех злодеев в мире"), "DONE");
        manager.updateTask(subtask2, new Subtask(manager.counterID, manager.epics.get(epic1).getTaskID(),
                "Лосьон для волос", "Выпустить собствнную линейку лосьенов для волос"), "DONE");

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

        manager.DeleteOneTasks(task1);
        manager.DeleteOneTasks(epic2);
    }
}
