package project.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import project.model.task.Epic;
import project.model.task.Subtask;
import project.model.task.Task;
import project.service.Managers;
import project.service.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {

    private final TaskManager taskManager;
    private final HttpServer server;
    private final Gson gson;
    private static final int PORT = 8080;

    public HttpTaskServer() throws IOException, InterruptedException {
        server = HttpServer.create();
        server.bind(new InetSocketAddress("localhost", PORT), 0);
        System.out.println(server);
        taskManager = Managers.getDefault();
        gson = Managers.getGson();
        server.createContext("/tasks/task/", this::handleTasks);
        server.createContext("/tasks/epic/", this::handleEpics);
        server.createContext("/tasks/subtask/", this::handleSubtasks);
        server.createContext("/tasks/subtask/epic/", this::handleEpicSubtasks);
        server.createContext("/tasks/history/", this::handleHistory);
        server.createContext("/tasks/", this::handlePrioritizedTasks);
    }

    private void handleTasks(HttpExchange exchange) {
        try {
            String query = exchange.getRequestURI().getQuery();
            String method = exchange.getRequestMethod();
            String response = "";
            switch (method) {
                case "GET":
                    if (query == null) {
                        response = gson.toJson(taskManager.getAllTasks());
                        writeResponse(exchange, response, 200);
                        break;
                    }
                    if (Pattern.matches("^id=\\d+$", query)) {
                        String pathId = query.replaceFirst("id=", "");
                        int id = parseIntId(pathId);
                        if (id != -1) {
                            response = gson.toJson(taskManager.getById(id));
                            writeResponse(exchange, response, 200);
                        } else {
                            System.out.println("Получен некорректный id = " + pathId);
                            writeResponse(exchange, response, 405);
                        }
                    }
                    break;
                case "POST":
                    Task task = gson.fromJson(readText(exchange), Subtask.class);
                    if (taskManager.getAllTasks().contains(task)) {
                        taskManager.updateTask(task.getId(), task, task.getStatus());
                        System.out.println("Задача успешно обновлена.");
                        writeResponse(exchange, response, 200);
                    } else {
                        int id = taskManager.createTask(task);
                        response = String.valueOf(id);
                        System.out.println("Задача успешно добавлена.");
                        writeResponse(exchange, response, 200);
                    }
                    break;
                case "DELETE":
                    if (query == null) {
                        taskManager.deleteAllTasks();
                        System.out.println("Все задачи удалены.");
                        writeResponse(exchange, response, 200);
                        break;
                    }
                    if (Pattern.matches("^id=\\d+$", query)) {
                        String pathId = query.replaceFirst("id=", "");
                        int id = parseIntId(pathId);
                        if (id != -1) {
                            taskManager.deleteOneTask(id);
                            System.out.println("Задача с id = " + id + " успешно удалена.");
                            writeResponse(exchange, response, 200);
                        } else {
                            System.out.println("Получен некорректный id = " + pathId);
                            writeResponse(exchange, response, 405);
                        }
                    }
                    break;
                default:
                    System.out.println("Не подходящий метод.");
                    writeResponse(exchange, response, 405);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private void handleEpics(HttpExchange exchange) {
        try {
            String query = exchange.getRequestURI().getQuery();
            String method = exchange.getRequestMethod();
            String response = "";
            switch (method) {
                case "GET":
                    if (query == null) {
                        response = gson.toJson(taskManager.getAllSubtasks());
                        writeResponse(exchange, response, 200);
                        break;
                    }
                    if (Pattern.matches("^id=\\d+$", query)) {
                        String pathId = query.replaceFirst("id=", "");
                        int id = parseIntId(pathId);
                        if (id != -1) {
                            response = gson.toJson(taskManager.getById(id));
                            writeResponse(exchange, response, 200);
                        } else {
                            System.out.println("Получен некорректный id = " + pathId);
                            writeResponse(exchange, response, 405);
                        }
                    } else {
                        System.out.println("Получен некорректный query");
                        writeResponse(exchange, response, 405);
                    }
                    break;
                case "POST":
                    Epic epic = gson.fromJson(readText(exchange), Epic.class);
                    if (taskManager.getAllEpics().contains(epic)) {
                        System.out.println("Этот эпик уже существует и его статус зависит от его подзадач.");
                        response = String.valueOf(epic.getId());
                        writeResponse(exchange, response, 405);
                    } else {
                        int id = taskManager.createEpic(epic);
                        response = String.valueOf(id);
                        System.out.println("Эпик успешно добавлен.");
                        writeResponse(exchange, response, 200);
                    }
                    break;
                case "DELETE":
                    if (query == null) {
                        taskManager.deleteAllTasks();
                        System.out.println("Все задачи удалены.");
                        writeResponse(exchange, response, 200);
                        break;
                    }
                    if (Pattern.matches("^id=\\d+$", query)) {
                        String pathId = query.replaceFirst("id=", "");
                        int id = parseIntId(pathId);
                        if (id != -1) {
                            taskManager.deleteOneTask(id);
                            System.out.println("Эпик с id = " + id + " успешно удален.");
                            writeResponse(exchange, response, 200);
                        } else {
                            System.out.println("Получен некорректный id = " + pathId);
                            writeResponse(exchange, response, 405);
                        }
                    }
                    break;
                default:
                    System.out.println("Не подходящий метод.");
                    writeResponse(exchange, response, 405);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private void handleSubtasks(HttpExchange exchange) {
        try {
            String query = exchange.getRequestURI().getQuery();
            String method = exchange.getRequestMethod();
            String response = "";
            switch (method) {
                case "GET":
                    if (query == null) {
                        response = gson.toJson(taskManager.getAllSubtasks());
                        writeResponse(exchange, response, 200);
                        break;
                    }
                    if (Pattern.matches("^id=\\d+$", query)) {
                        String pathId = query.replaceFirst("id=", "");
                        int id = parseIntId(pathId);
                        System.out.println(id);
                        if (id != -1) {
                            response = gson.toJson(taskManager.getById(id));
                            writeResponse(exchange, response, 200);
                        } else {
                            System.out.println("Получен некорректный id = " + pathId);
                            writeResponse(exchange, response, 405);
                        }
                    }
                    break;
                case "POST":
                    Subtask subtask = gson.fromJson(readText(exchange), Subtask.class);
                    if (taskManager.getAllSubtasks().contains(subtask)) {
                        taskManager.updateTask(subtask.getId(), subtask, subtask.getStatus());
                        System.out.println("Подзадача успешно обновлена.");
                        writeResponse(exchange, response, 200);
                    } else {
                        int id = taskManager.createSubTask(subtask);
                        response = String.valueOf(id);
                        System.out.println("Подзадача успешно добавлена.");
                        writeResponse(exchange, response, 200);
                    }
                    break;
                case "DELETE":
                    if (query == null) {
                        taskManager.deleteAllTasks();
                        System.out.println("Все задачи удалены.");
                        writeResponse(exchange, response, 200);
                        break;
                    }
                    if (Pattern.matches("^id=\\d+$", query)) {
                        String pathId = query.replaceFirst("id=", "");
                        int id = parseIntId(pathId);
                        if (id != -1) {
                            taskManager.deleteOneTask(id);
                            System.out.println("Подзадача с id = " + id + " успешно удалена.");
                            writeResponse(exchange, response, 200);
                        } else {
                            System.out.println("Получен некорректный id = " + pathId);
                            writeResponse(exchange, response, 405);
                        }
                    }
                    break;
                default:
                    System.out.println("Не подходящий метод.");
                    writeResponse(exchange, response, 405);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private void handleEpicSubtasks(HttpExchange exchange) {
        try {
            String query = exchange.getRequestURI().getQuery();
            String method = exchange.getRequestMethod();
            String response = "";
            switch (method) {
                case "GET":
                    if (query == null) {
                        System.out.println("Нужно указать id эпика, чтобы получить его подзадачи");
                        writeResponse(exchange, response, 405);
                        break;
                    }
                    if (Pattern.matches("^id=\\d+$", query)) {
                        String pathId = query.replaceFirst("id=", "");
                        int id = parseIntId(pathId);
                        if (id != -1) {
                            if (taskManager.getById(id) != null) {
                                response = gson.toJson(taskManager.getAllSubtasksByEpic(id));
                                writeResponse(exchange, response, 200);
                            } else {
                                System.out.println("Эпика с id = " + pathId + " не существует.");
                                writeResponse(exchange, response, 405);
                            }
                        } else {
                            System.out.println("Получен некорректный id = " + pathId);
                            writeResponse(exchange, response, 405);
                        }
                    }
                    break;
                default:
                    System.out.println("Не подходящий метод. Можно только получить подзадачи эпика методом GET");
                    writeResponse(exchange, response, 405);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private void handleHistory(HttpExchange exchange) {
        try {
            String method = exchange.getRequestMethod();
            String response = "";
            if ("GET".equals(method)) {
                response = gson.toJson(taskManager.getHistory());
                writeResponse(exchange, response, 200);
            } else {
                System.out.println("/task/history работает только для GET запросов, метод "
                        + method + " не обрабатывается");
                writeResponse(exchange, response, 405);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private void handlePrioritizedTasks(HttpExchange exchange) {
        try {
            String method = exchange.getRequestMethod();
            String response = "";
            if ("GET".equals(method)) {
                response = gson.toJson(taskManager.getSortedTasks());
                writeResponse(exchange, response, 200);
            } else {
                System.out.println("/tasks работает только для GET запросов, метод " + method + " не обрабатывается");
                writeResponse(exchange, response, 405);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private void writeResponse(HttpExchange exchange, String response, int responseCode) throws IOException {
        if (response.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            exchange.sendResponseHeaders(responseCode, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
        exchange.close();
    }

    private int parseIntId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    private String readText(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), UTF_8);
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        server.start();
    }

    public void stop() {
        System.out.println("Сервер на порту " + PORT + " остановлен.");
        server.stop(0);
    }
}
