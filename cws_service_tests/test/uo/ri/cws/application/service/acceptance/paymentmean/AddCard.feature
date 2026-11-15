Feature: [PM.AC] Add a credit card
  As a Cashier
  I want to register a credit card for a client
  Because clients can use their cards to pay invoices

  Scenario: [PM.AC.1] Add a credit card to an existing client
    Given [PM.AC.1] a registered client
    When [PM.AC.1] I add a new credit card for that client
    Then [PM.AC.1] the credit card results added to the system

  Scenario: [PM.AC.2] Try to add a credit card for a non existing client
    When [PM.AC.2] I try to add a credit card for a non existing client
    Then [PM.AC.2] a business error happens with an explaining message

  Scenario: [PM.AC.3] Try to add a credit card with a repeated number
    Given [PM.AC.3] a registered client with a credit card
    When [PM.AC.3] I try to add another credit card with same number
    Then [PM.AC.3] a business error happens with an explaining message

  Scenario: [PM.AC.4] Try to add an outdated credit card
    Given [PM.AC.4] a registered client
    When [PM.AC.4] I try to add an outdated credit card for that client
    Then [PM.AC.4] a business error happens with an explaining message
