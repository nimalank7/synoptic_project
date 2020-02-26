package uk.first_catering.kiosk.ExceptionHandler;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Component
public class CardExceptionHandler {

    @ExceptionHandler(value = CardTimedOutException.class)
    public ResponseEntity<ErrorResponse> handleException(CardTimedOutException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        return ResponseEntity.status(408).body(errorResponse);
    }

    @ExceptionHandler(value = UnrecognizedCardException.class)
    public ResponseEntity<ErrorResponse> handleException(UnrecognizedCardException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        return ResponseEntity.status(400).body(errorResponse);
    }

    @ExceptionHandler(value = NotEnoughAmountException.class)
    public ResponseEntity<ErrorResponse> handleException(NotEnoughAmountException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        return ResponseEntity.status(400).body(errorResponse);
    }
}
