package project.service;

import project.model.exception.ManagerSaveException;
import project.model.task.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private File file;
    private static final int ID_INDEX = 0;
    private static final int TYPE_INDEX = 1;
    private static final int NAME_INDEX = 2;
    private static final int STATUS_INDEX = 3;
    private static final int DESCRIPTION_INDEX = 4;
    private static final int DURATION_INDEX = 5;
    private static final int START_TIME_INDEX = 6;
    private static final int EPIC_INDEX = 7;

    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
    }

    public static void main(String[] args) {
        FileBackedTasksManager fileBackedTasksManager1 = new FileBackedTasksManager(
                new File("src/resources/tasks.csv"));
        HistoryManager historyManager = fileBackedTasksManager1.getHistoryManager();

        int task1 = fileBackedTasksManager1.createTask(new Task(Status.NEW, "Задача",
                "Создать задачу для тестирования работоспособности приложения",
                15L, LocalDateTime.of(2023,02,24,12,0)));
        int task2 = fileBackedTasksManager1.createTask(new Task(Status.NEW, "Футбол",
                "Посмотреть финал чемпионата мира по футболу",
                15L, LocalDateTime.of(2023,02,23,12,0)));
        int epic1 = fileBackedTasksManager1.createEpic(new Epic(Status.NEW, "Эпик 1",
                "Осуществить мечту",0, null));
        int subtask1 = fileBackedTasksManager1.createSubTask(new Subtask(Status.NEW, epic1, "Подзадача 1 эпика 1",
                "Победить всех злодеев в мире",
                15L, LocalDateTime.of(2023,02,23,13,0)));
        int subtask2 = fileBackedTasksManager1.createSubTask(new Subtask(Status.NEW, epic1, "Лосьон для волос",
                "Выпустить собственную линейку лосьонов для волос",
                15L, LocalDateTime.of(2023,02,23,14,0)));

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
                    new File("src/resources/tasks.csv"));
            fileBackedTasksManager2.save();
            HistoryManager historyManager2 = fileBackedTasksManager2.getHistoryManager();
            System.out.println("Запрос задачи и вывод истории после загрузки из файла:");
            System.out.println("История запросов: \n" + historyManager2.getHistory());
        } catch (IOException e) {
            throw new ManagerSaveException("Привет, Сергей! :)");
        }

    }

    protected void save() {
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(getAllTasks());
        allTasks.addAll(getAllEpics());
        allTasks.addAll(getAllSubtasks());

        try (BufferedWriter writter = new BufferedWriter(new FileWriter(file))) {
            writter.write("id,type,name,status,description,duration,startTime,epic" + "\n");
            for (Task task : allTasks) {
                writter.write(task.toString());
            }
            writter.write("\n");
            writter.write(historyToString(getHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл.");
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
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        String[] stringFile = (Files.readString(Path.of(file.toURI()))).split("\n");
        for (int i = 1; i < stringFile.length; i++) {
            if (!stringFile[i].isBlank()) {
                String[] paths = stringFile[i].split(","); // id,type,name,status,description,epic
                switch (Type.valueOf(paths[TYPE_INDEX])) {
                    case TASK:
                        Task task = new Task(Status.valueOf(paths[STATUS_INDEX]), paths[NAME_INDEX],
                                paths[DESCRIPTION_INDEX], Long.parseLong(paths[DURATION_INDEX]),
                                (!(paths[START_TIME_INDEX].equals("null")) ? LocalDateTime.parse(paths[START_TIME_INDEX]) : null));
                        task.setId(Integer.parseInt(paths[ID_INDEX]));
                        fileBackedTasksManager.addTasks(task.getId(), task);
                        break;
                    case EPIC:
                        Epic epic = new Epic(Status.valueOf(paths[STATUS_INDEX]), paths[NAME_INDEX],
                                paths[DESCRIPTION_INDEX], Long.parseLong(paths[DURATION_INDEX]),
                                (!(paths[START_TIME_INDEX].equals("null")) ? LocalDateTime.parse(paths[START_TIME_INDEX]) : null));
                        epic.setId(Integer.parseInt(paths[ID_INDEX]));
                        fileBackedTasksManager.addEpics(epic.getId(), epic);
                        break;
                    case SUBTASK:
                        Subtask subtask = new Subtask(Status.valueOf(paths[STATUS_INDEX]),
                                Integer.parseInt(paths[EPIC_INDEX]), paths[NAME_INDEX], paths[DESCRIPTION_INDEX],
                                Long.parseLong(paths[DURATION_INDEX]),
                                (!(paths[START_TIME_INDEX].equals("null")) ? LocalDateTime.parse(paths[START_TIME_INDEX]) : null));
                        subtask.setId(Integer.parseInt(paths[ID_INDEX]));
                        fileBackedTasksManager.addSubtasks(subtask.getId(), subtask);
                }
            } else {
                for (Integer id : historyFromString(stringFile[stringFile.length - 1])) {
                    if (fileBackedTasksManager.getTasks().containsKey(id)) {
                        fileBackedTasksManager.getHistoryManager().add(fileBackedTasksManager.getTasks().get(id));
                        // add - это метод HistoryManager, который добавляет task в CustomLinkedList.
                    } else if (fileBackedTasksManager.getEpics().containsKey(id)) {
                        fileBackedTasksManager.getHistoryManager().add(fileBackedTasksManager.getEpics().get(id));
                    } else {
                        fileBackedTasksManager.getHistoryManager().add(fileBackedTasksManager.getSubtasks().get(id));
                    }
                }
                return fileBackedTasksManager;
            }
        }
        return fileBackedTasksManager;
    }

    /*@Override
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
    }*/

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

    @Override
    public int createTask(Task task) {
        int id = super.createTask(task);
        save();
        return id;
    }

    @Override
    public int createEpic(Epic epic) {
        int id = super.createEpic(epic);
        save();
        return id;
    }

    @Override
    public int createSubTask(Subtask subtask) {
        int id = super.createSubTask(subtask);
        save();
        return id;
    }
}
