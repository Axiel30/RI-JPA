Feature: [C.D] Delete a client
  As a Foreman
  I want to delete unused clients
  Because we need to keep the system clean

  Scenario: [C.D.1] Delete an existing unused client
    Given [C.D.1] a registered client with cash
    When [C.D.1] I remove the client by nif
    Then [C.D.1] the client no longer exists
    And [C.D.1] the client payment means are deleted

  Scenario: [C.D.2] Delete a client with recommendations
    Given [C.D.2] a registered client with made and received recommendations
    When [C.D.2] I remove the client by nif
    Then [C.D.2] the client no longer exists
    And [C.D.2] the client recommendations are deleted

  Scenario: [C.D.3] Try to remove a non existing client
    When [C.D.3] I try to remove a non existing client by nif
    Then [C.D.3] a business error happens with an explaining message

  Scenario: [C.D.4] Try to remove a client with vehicles
    Given [C.D.4] a registered client with vehicles
    When [C.D.4] I try to remove the client by nif
    Then [C.D.4] a business error happens with an explaining message

  Scenario: [C.D.5] Try to remove a client with paid invoices
    Given [C.D.5] a registered client with paid invoices
    When [C.D.5] I try to remove the client by nif
    Then [C.D.5] a business error happens with an explaining message
