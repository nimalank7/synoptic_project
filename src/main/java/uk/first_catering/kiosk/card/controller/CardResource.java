package uk.first_catering.kiosk.card.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.first_catering.kiosk.card.model.Card;
import uk.first_catering.kiosk.card.model.CardTopUpRequest;
import uk.first_catering.kiosk.card.service.CardService;

import java.util.List;

@RestController
@RequestMapping("/card")
public class CardResource {

    @Autowired
    CardService cardService;

    @RequestMapping(path="{cardId}", method=RequestMethod.GET)
    public Card getCard(@PathVariable int cardId) {
        return cardService.findById(String.valueOf(cardId));
    }

    @RequestMapping(path="/cards", method=RequestMethod.GET)
    public List<Card> getAllCards() {
        return cardService.findAll();
    }

    @RequestMapping(path="", method=RequestMethod.POST)
    public Card createNewCard(@RequestBody Card card) {
        return cardService.saveCard(card);
    }

    @RequestMapping(path="", method=RequestMethod.PUT)
    public Card updateCard(@RequestBody  Card card) {
        return cardService.saveCard(card);
    }

    @RequestMapping(path="", method=RequestMethod.DELETE)
    public String deleteCard(@RequestBody Card card) {
        cardService.deleteCard(card);

        return "Deleted card: " + card.getCardId();
    }

    @RequestMapping(path="topUpCard", method=RequestMethod.PATCH)
    public Card topUpCard(@RequestBody CardTopUpRequest cardTopUpRequest) {
        return cardService.topUpCard(cardTopUpRequest);
    }
}
