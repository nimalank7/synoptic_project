package uk.first_catering.kiosk.card.model;

public class UpdateCardTotalRequest {

    int topUp;

    int pay;

    public UpdateCardTotalRequest() {
    }

    public int getTopUp() {
        return topUp;
    }

    public void setTopUp(int topUp) {
        this.topUp = topUp;
    }

    public int getPay() {
        return pay;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }
}
