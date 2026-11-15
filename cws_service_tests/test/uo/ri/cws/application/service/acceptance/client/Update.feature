Feature: [C.U] Update a client
  As a Foreman
  I want to update client data

  Scenario: [C.U.1] Update an existing client
    Given [C.U.1] a registered client
    When [C.U.1] I update the client
    Then [C.U.1] the client results updated
    And [C.U.1] client version increases

  Scenario: [C.U.2] Update an existing client changing the nif
    Given [C.U.2] a registered client
    When [C.U.2] I update the client with a new nif
    Then [C.U.2] the client results updated
    And [C.U.2] client version increases

  Scenario: [C.U.3] Try to update a non existing client
    When [C.U.3] I try to update a non existing client
    Then [C.U.3] a business error happens with an explaining message

  Scenario: [C.U.4] Try to update a client with a repeated nif
    Given [C.U.4] two registered clients
    When [C.U.4] I try to update one client with the other client nif
    Then [C.U.4] a business error happens with an explaining message

  Scenario: [C.U.5] Try to update a client updated in the while (wrong version)
    Given [C.U.5] a registered client
    When [C.U.5] I try to update a client updated in the while
    Then [C.U.5] a business error happens with an explaining message
