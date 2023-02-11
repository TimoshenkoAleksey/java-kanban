import project.service.HistoryManager;
import project.service.InMemoryTaskManager;
import project.model.task.Epic;
import project.model.task.Status;
import project.model.task.Subtask;
import project.model.task.Task;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        HistoryManager historyManager = inMemoryTaskManager.getHistoryManager();
        int task1 = inMemoryTaskManager.createTask(new Task(Status.NEW, "Задача",
                "Создать задачу для тестирования работоспособности приложения"));
        int task2 = inMemoryTaskManager.createTask(new Task(Status.NEW,"Футбол",
                "Посмотреть финал чемпионата мира по футболу"));
        int epic1 = inMemoryTaskManager.createEpic(new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту"));
        int subtask1 = inMemoryTaskManager.createSubTask(new Subtask(Status.NEW, epic1,"Подзадача 1 эпика 1",
                "Победить всех злодеев в мире"));
        int subtask2 = inMemoryTaskManager.createSubTask(new Subtask(Status.NEW, epic1,"Лосьон для волос",
                "Выпустить собствнную линейку лосьонов для волос"));
        int subtask3 = inMemoryTaskManager.createSubTask(new Subtask(Status.NEW, epic1,"Полет на ракете",
                "Пока ракеты нет, выйти на улицу и взорвать петарду"));
        int epic2 = inMemoryTaskManager.createEpic(new Epic(Status.NEW, "Бегать по утрам",
                "Начать бегать по утрам каждый день"));
        int subtask4 = inMemoryTaskManager.createSubTask(new Subtask(Status.NEW, epic2,
                "Дождаться понедельника",
                "Дождаться понедельника и перенести на следующий понедельник"));


        for (int i = 1; i < 9; i++) {
            System.out.println("Получить по id: \n" + inMemoryTaskManager.getById(1));
            System.out.println("История запросов: \n" + inMemoryTaskManager.getHistoryManager().getHistory());
        }

        System.out.println(inMemoryTaskManager.getAllSubtasksByEpic(epic1));

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

        inMemoryTaskManager.updateTask(subtask1, new Subtask(Status.NEW, epic1,"Подзадача 1 эпика 1",
                "Победить всех злодеев в мире"), Status.DONE);
        inMemoryTaskManager.updateTask(subtask2, new Subtask(Status.NEW, epic1,"Лосьон для волос",
                        "Выпустить собствнную линейку лосьенов для волос"), Status.DONE);

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

        System.out.println(inMemoryTaskManager.getAllSubtasksByEpic(epic1));

        System.out.println("Получить по id: \n" + inMemoryTaskManager.getById(subtask4));
        System.out.println("История запросов: \n" + historyManager.getHistory());
        System.out.println("Получить по id: \n" + inMemoryTaskManager.getById(task2));
        System.out.println("История запросов: \n" + historyManager.getHistory());
        System.out.println("Получить по id: \n" + inMemoryTaskManager.getById(epic1));
        System.out.println("История запросов: \n" + historyManager.getHistory());

        System.out.println();
        System.out.println("Запрос задачи и вывод истории:");
        historyManager.add(inMemoryTaskManager.getById(epic1));
        System.out.println("История запросов: \n" + historyManager.getHistory());
        historyManager.add(inMemoryTaskManager.getById(subtask3));
        System.out.println("История запросов: \n" + historyManager.getHistory());
        historyManager.add(inMemoryTaskManager.getById(subtask1));
        System.out.println("История запросов: \n" + historyManager.getHistory());
        historyManager.add(inMemoryTaskManager.getById(epic1));
        System.out.println("История запросов: \n" + historyManager.getHistory());

        System.out.println();
        System.out.println("Удаляем несколько задач:");
        inMemoryTaskManager.deleteOneTask(task2);
        inMemoryTaskManager.deleteOneTask(epic2);
        inMemoryTaskManager.deleteOneTask(subtask4);
        System.out.println(historyManager.getHistory());
        System.out.println();
        System.out.println("Удаляем эпик:");
        inMemoryTaskManager.deleteOneTask(epic1);
        System.out.println(historyManager.getHistory());
    }
}
