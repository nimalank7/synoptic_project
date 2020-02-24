package uk.first_catering.kiosk.card.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.first_catering.kiosk.card.model.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, String> {

}
