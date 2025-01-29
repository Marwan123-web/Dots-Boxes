package errors;

import static java.lang.System.err;

public class ErrorMessage {

    public String genericErrorMessage(String message) {
        err.println(message);
        return message;
    }
}
