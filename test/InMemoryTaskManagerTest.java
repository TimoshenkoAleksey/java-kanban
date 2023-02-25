import org.junit.jupiter.api.BeforeEach;
import project.service.InMemoryTaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void BeforeEach() {
        taskManager = new InMemoryTaskManager();
        historyManager = taskManager.getHistoryManager();
    }
}