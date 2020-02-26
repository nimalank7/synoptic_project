# Catering API

This is a REST API provided by Catering Ltd which serves kiosk terminals at Bows Formula One High Performance Cars. Endpoints allow users to top up, pay and update their cards and details. 

# User guide

A front-end app is needed to call the endpoints and display the responses in an accessible format to the user.

* GET to /card/{cardId} is the entry point into the system. The other endpoints (except POST to /card) require the presence of a card_id
    * A card is tapped onto the system which takes the card_id and calls the following endpoint
    * If the card isn’t recognized it will return a message indicating so
    * If the card isn’t registered it will return a message indicating so, as well as the card details and store a session for valid for 4 minutes. These should be displayed to the user.
    * Tapping in with a registered card will return the card details as well as welcome message. These should be displayed to the user.
* POST to /card
    * Calling this endpoint does not require a card_id.
    * passing in the card details will result in the creation of a new card
    * This endpoint should be used by administrators to issue new cards to employees
* PATCH to /card
    * Calling this endpoint with a JSON field of either topUp or pay with an integer will result in either topping up or deducting the card's total pence and the card details will be returned. These should be displayed to the user.
    * If in the case of insufficient funds to pay for an amount a message will be returned to the user indicating so as with their card details. These should be displayed to the user.
* POST to /employee
    * Calling this endpoint with the JSON field for an Employee object will result in registering the card with that employee
    * The response will return the employee's details as well as a message. These should be displayed to the user.
    * If the user's session has timed out then it will return a message indicating this. This should be displayed to the user.
* PATCH to /employee
    * Calling this endpoint with the JSON field for an Employee object will result in the associated user details for the card to be updated. This should be displayed to the user.
    * If the user's session has timed out then it will return a message indicating this. This should be displayed to the user. 
