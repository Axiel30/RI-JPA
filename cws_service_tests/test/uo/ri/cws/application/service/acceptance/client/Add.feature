Feature: [C.A] Add a client
  As a Foreman
  I want to register a client
  Because new clients must be known by the workshop

  Scenario: [C.A.1] Add a non existing client
    Given [C.A.1] a new non existing client
    When [C.A.1] I add that client
    Then [C.A.1] the client results added to the system
    And [C.A.1] the client has a cash payment mean

  Scenario: [C.A.2] Add a non existing client recommended by an existing client
    Given [C.A.2] a registered recommender client
    And [C.A.2] a new non existing client
    When [C.A.2] I add that client with the recommender nif
    Then [C.A.2] the client results added to the system
    And [C.A.2] the client has a cash payment mean
    And [C.A.2] the recommendation results registered

  Scenario: [C.A.3] Try to add a client with a repeated nif
    Given [C.A.3] a registered client
    When [C.A.3] I try to add a new client with same nif
    Then [C.A.3] a business error happens with an explaining message

  Scenario: [C.A.4] Try to add a client recommended by a non existing client
    Given [C.A.4] a new non existing client
    When [C.A.4] I try to add that client with a non existing recommender nif
    Then [C.A.4] a business error happens with an explaining message

  Scenario: [C.A.5] Try to add a client recommended by itself
    Given [C.A.5] a new non existing client
    When [C.A.5] I try to add that client with its own nif as recommender
    Then [C.A.5] a business error happens with an explaining message
