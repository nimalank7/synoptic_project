package uk.first_catering.kiosk.card.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.first_catering.kiosk.card.model.Card;
import uk.first_catering.kiosk.card.model.CardTopUpRequest;
import uk.first_catering.kiosk.card.repository.CardRepository;

import java.util.List;
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

    public List<Card> findAll() {
        return cardRepository.findAll();
    }

    public Card saveCard(Card card) {
        return cardRepository.save(card);
    }

    public Card topUpCard(CardTopUpRequest cardTopUpRequest) {

        Card card = findById(cardTopUpRequest.getCardId());
        int nextTotal = card.getTotal() + cardTopUpRequest.getAmount();
        card.setTotal(nextTotal);
        return cardRepository.save(card);
    }

    public void deleteCard(Card card) {
        cardRepository.delete(card);
    }
}
