package uk.first_catering.kiosk.card.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.first_catering.kiosk.ExceptionHandler.NotEnoughAmountException;
import uk.first_catering.kiosk.card.model.Card;
import uk.first_catering.kiosk.card.model.CardResponse;
import uk.first_catering.kiosk.card.repository.CardRepository;

import java.util.Optional;

@Service
public class CardService {

    @Autowired
    CardRepository cardRepository;

    public Card findById(String cardId) {
        Card card = null;

        Optional<Card> result = cardRepository.findById(String.valueOf(cardId));

        if (result.isPresent()) {
            card = result.get();
        }

        return card;
    }

    public CardResponse saveCard(Card card) {
        String message = "Your card has been created!";
        cardRepository.save(card);
        return createCardResponse(card, message);
    }

    public CardResponse topUpCard(Card card, int amount) {
        int nextTotal = card.getTotal() + amount;
        card.setTotal(nextTotal);
        card = cardRepository.save(card);
        String message = "You have topped up by " + amount;
        return createCardResponse(card, message);
    }

    public CardResponse pay(Card card, int amount) {

        if (card.getTotal() < amount) {
            throw new NotEnoughAmountException("Insufficent funds for this payment.");
        }
        int nextTotal = card.getTotal() - amount;
        card.setTotal(nextTotal);
        card = cardRepository.save(card);
        String message = "You have paid: " + amount + ". Your remaining total is: " + card.getTotal();
        return createCardResponse(card, message);
    }

    public CardResponse createCardResponse(Card card, String message) {
        CardResponse cardResponse = new CardResponse();
        cardResponse.setMessage(message);
        cardResponse.setCard(card);
        return cardResponse;
    }
}
