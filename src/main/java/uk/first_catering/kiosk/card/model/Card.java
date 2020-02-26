package uk.first_catering.kiosk.card.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.first_catering.kiosk.employee.model.Employee;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="card")
public class Card {

    @Id
    @Column(name="card_id")
    @JsonProperty("card_id")
    private String cardId;

    @Column(name="total")
    private int total;

    @OneToOne(mappedBy = "card", cascade= CascadeType.ALL)
    @JsonIgnore
    private Employee employee;

    public Card() {
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardId='" + cardId + '\'' +
                ", total=" + total +
                '}';
    }
}
