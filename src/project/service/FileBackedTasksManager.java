package project.service;

import project.model.exception.ManagerSaveException;
import project.model.task.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    File file;

    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
    }

    public static void main(String[] args) {
        FileBackedTasksManager fileBackedTasksManager1 = new FileBackedTasksManager(
                new File("C:\\Users\\Алексей\\dev\\java-kanban\\src\\resources\\tasks.csv"));
        HistoryManager historyManager = fileBackedTasksManager1.getHistoryManager();

        int task1 = fileBackedTasksManager1.createTask(new Task(Status.NEW, "Задача",
                "Создать задачу для тестирования работоспособности приложения"));
        int task2 = fileBackedTasksManager1.createTask(new Task(Status.NEW, "Футбол",
                "Посмотреть финал чемпионата мира по футболу"));
        int epic1 = fileBackedTasksManager1.createEpic(new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту"));
        int subtask1 = fileBackedTasksManager1.createSubTask(new Subtask(Status.NEW, epic1,
                "Подзадача 1 эпика 1", "Победить всех злодеев в мире"));
        int subtask2 = fileBackedTasksManager1.createSubTask(new Subtask(Status.NEW, epic1, "Лосьон для волос",
                "Выпустить собствнную линейку лосьонов для волос"));

        System.out.println();
        System.out.println("Запрос задачи и вывод истории:");
        historyManager.add(fileBackedTasksManager1.getById(epic1));
        System.out.println("История запросов: \n" + historyManager.getHistory());
        historyManager.add(fileBackedTasksManager1.getById(subtask1));
        System.out.println("История запросов: \n" + historyManager.getHistory());
        historyManager.add(fileBackedTasksManager1.getById(epic1));
        System.out.println("История запросов: \n" + historyManager.getHistory());

        try {
            FileBackedTasksManager fileBackedTasksManager2 = loadFromFile(
                    new File("C:\\Users\\Алексей\\dev\\java-kanban\\src\\resources\\tasks.csv"));
            HistoryManager historyManager2 = fileBackedTasksManager2.getHistoryManager();
            System.out.println("Запрос задачи и вывод истории после загрузки из файла:");
            System.out.println("История запросов: \n" + historyManager2.getHistory());
        } catch (IOException e) {
            throw new ManagerSaveException("Привет, Сергей! :)");
        }

    }

    private void save() {
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(getAllTasks());
        allTasks.addAll(getAllEpics());
        allTasks.addAll(getAllSubtasks());

        try (BufferedWriter writter = new BufferedWriter(new FileWriter(file))) {
            writter.write("id,type,name,status,description,epic" + "\n");
            for (Task task : allTasks) {
                writter.write(task.toString());
            }
            writter.write("\n");
            writter.write(historyToString(getHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException("Привет, Сергей! :)");
        }
    }

    public static String historyToString(HistoryManager manager) {
        String historyString = "";
        for (Task task : manager.getHistory()) {
            historyString += task.getId() + ",";
        }
        return historyString;
    }

    public static List<Integer> historyFromString(String value) {
        String[] strings = value.split("\n");
        String[] ids = strings[strings.length - 1].split(",");
        List<Integer> list = new ArrayList<>();
        for (String number : ids) {
            list.add(Integer.parseInt(number));
        }
        return list;
    }

    public static FileBackedTasksManager loadFromFile(File file) throws IOException {
        FileBackedTasksManager fileBackedTasksManage = new FileBackedTasksManager(file);
        String[] stringFile = (Files.readString(Path.of(file.toURI()))).split("\n");
        for (int i = 1; i < stringFile.length; i++) {
            if (!stringFile[i].isBlank()) {
                String[] paths = stringFile[i].split(","); // id,type,name,status,description,epic
                switch (paths[1]) {
                    case "TASK":
                        Task task = new Task(Status.valueOf(paths[3]), paths[2], paths[4]);
                        task.setId(Integer.parseInt(paths[0]));
                        fileBackedTasksManage.addTasks(task.getId(), task);
                        break;
                    case "EPIC":
                        Epic epic = new Epic(Status.valueOf(paths[3]), paths[2], paths[4]);
                        epic.setId(Integer.parseInt(paths[0]));
                        fileBackedTasksManage.addEpics(epic.getId(), epic);
                        break;
                    case "SUBTASK":
                        Subtask subtask = new Subtask(Status.valueOf(paths[3]), Integer.parseInt(paths[5]), paths[2],
                                paths[4]);
                        subtask.setId(Integer.parseInt(paths[0]));
                        fileBackedTasksManage.addSubtasks(subtask.getId(), subtask);
                }
            } else {
                for (Integer id : historyFromString(stringFile[stringFile.length - 1])) {
                    if (fileBackedTasksManage.getTasks().containsKey(id)) {
                        fileBackedTasksManage.getHistoryManager().add(fileBackedTasksManage.getTasks().get(id));
                        // add - это метод HistoryManager, который добавляет task в CustomLinkedList.
                    } else if (fileBackedTasksManage.getEpics().containsKey(id)) {
                        fileBackedTasksManage.getHistoryManager().add(fileBackedTasksManage.getEpics().get(id));
                    } else {
                        fileBackedTasksManage.getHistoryManager().add(fileBackedTasksManage.getSubtasks().get(id));
                    }
                }
            }
        }
        return fileBackedTasksManage;
    }

    @Override
    public void addTasks(int id, Task task) {
        super.addTasks(id, task);
        save();
    }

    @Override
    public void addEpics(int id, Epic epic) {
        super.addEpics(id, epic);
        save();
    }

    @Override
    public void addSubtasks(int id, Subtask subtask) {
        super.addSubtasks(id, subtask);
        save();
    }

    @Override
    public Task getById(int id) {
        Task task = super.getById(id);
        save();
        return task;
    }

    @Override
    public void updateTask(Integer id, Task task, Status newStatus) {
        super.updateTask(id, task, newStatus);
        save();
    }

    @Override
    public void deleteOneTask(Integer id) {
        super.deleteOneTask(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }
}
