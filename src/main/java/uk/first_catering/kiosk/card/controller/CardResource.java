package uk.first_catering.kiosk.card.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.first_catering.kiosk.ExceptionHandler.CardTimedOutException;
import uk.first_catering.kiosk.ExceptionHandler.ErrorResponse;
import uk.first_catering.kiosk.ExceptionHandler.UnrecognizedCardException;
import uk.first_catering.kiosk.card.model.Card;
import uk.first_catering.kiosk.card.model.CardResponse;
import uk.first_catering.kiosk.card.model.UpdateCardTotalRequest;
import uk.first_catering.kiosk.card.service.CardService;
import uk.first_catering.kiosk.employee.model.Employee;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/card")
public class CardResource {

    @Autowired
    CardService cardService;

    @RequestMapping(path="{cardId}", method=RequestMethod.GET)
    public ResponseEntity<CardResponse> getCard(@CookieValue(name="card_id", required=false) String card_id, @PathVariable int cardId, HttpServletResponse response) {

        Card card = cardService.findById(String.valueOf(cardId));
        CardResponse cardResponse;
        Employee employee;

        if(card == null) {
            throw new UnrecognizedCardException("This card isn't recognized");
        }

        employee = card.getEmployee();

        if(employee == null) {
            Cookie cookie = new Cookie("card_id", card.getCardId());
            cookie.setMaxAge(240);
            cookie.setPath("/");
            response.addCookie(cookie);
            cardResponse = new CardResponse();
            cardResponse.setMessage("This card isn't registered");
            cardResponse.setCard(card);
            return ResponseEntity.status(400).body(cardResponse);
        }

        if(card_id != null) {
            Cookie cookie = new Cookie("card_id", card.getCardId());
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
            cardResponse = new CardResponse();
            cardResponse.setMessage("You have successfully logged out");
            return ResponseEntity.ok(cardResponse);
        }

        Cookie cookie = new Cookie("card_id", card.getCardId());
        cookie.setMaxAge(240);
        cookie.setPath("/");
        response.addCookie(cookie);
        cardResponse = new CardResponse();
        cardResponse.setMessage("Welcome: " + employee.getName());
        cardResponse.setCard(card);
        return ResponseEntity.ok(cardResponse);
    }

    @RequestMapping(path="", method=RequestMethod.POST)
    public ResponseEntity<CardResponse> createNewCard(@RequestBody Card card) {
        return ResponseEntity.ok().body(cardService.saveCard(card));
    }

    @RequestMapping(path="", method=RequestMethod.PATCH)
    public ResponseEntity<CardResponse> updateTotal(@CookieValue(name="card_id", required=false) String card_id, @RequestBody UpdateCardTotalRequest updateCardTotalRequest) {

        if(card_id == null) {
            throw new CardTimedOutException("You were timed out. Please tap in again.");
        }

        CardResponse cardResponse;
        Card card = cardService.findById(card_id);

        Integer topUp = updateCardTotalRequest.getTopUp();
        Integer pay = updateCardTotalRequest.getPay();

        if(topUp != 0) {
            cardResponse = cardService.topUpCard(card, topUp);
            return ResponseEntity.ok().body(cardResponse);
        }

        cardResponse = cardService.pay(card, pay);
        return ResponseEntity.ok().body(cardResponse);
    }
}
