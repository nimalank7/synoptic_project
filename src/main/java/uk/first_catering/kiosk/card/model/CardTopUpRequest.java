package uk.first_catering.kiosk.card.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CardTopUpRequest {

    @JsonProperty("card_id")
    String cardId;

    int amount;

    public CardTopUpRequest() {
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


}
