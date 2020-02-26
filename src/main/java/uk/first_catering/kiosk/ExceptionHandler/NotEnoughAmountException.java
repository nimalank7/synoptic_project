package uk.first_catering.kiosk.ExceptionHandler;

public class NotEnoughAmountException extends RuntimeException {
    public NotEnoughAmountException(String message) {
        super(message);
    }
}
