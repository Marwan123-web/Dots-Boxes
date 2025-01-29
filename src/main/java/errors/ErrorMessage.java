package errors;

import static java.lang.System.err;

public class ErrorMessage {

    public void genericErrorMessage(String message) {
        err.println(message);
    }
}
