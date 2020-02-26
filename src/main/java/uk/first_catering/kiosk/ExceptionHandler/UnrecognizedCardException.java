package uk.first_catering.kiosk.ExceptionHandler;

public class UnrecognizedCardException extends RuntimeException {
    public UnrecognizedCardException(String message) {
        super(message);
    }
}
