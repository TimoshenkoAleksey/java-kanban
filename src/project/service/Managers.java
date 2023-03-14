package project.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import project.model.adapter.LocalDateTimeAdapter;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class Managers {

    public static TaskManager getDefault() {
        return new HttpTaskManager("http://localhost:");
    }

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getDefaultBacked() {
        return new FileBackedTasksManager(new File("src/resources/tasks.csv"));
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.setPrettyPrinting();
        return gsonBuilder.create();
    }
}
