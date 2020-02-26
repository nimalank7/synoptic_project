package uk.first_catering.kiosk.ExceptionHandler;

public class CardTimedOutException extends RuntimeException {

    public CardTimedOutException(String message) {
        super(message);
    }
}
