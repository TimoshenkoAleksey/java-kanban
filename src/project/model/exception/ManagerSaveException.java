package project.model.exception;

import java.io.IOException;

public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException(String message) {
        super();
        System.out.println(message);
    }
}
