Feature: [PM.FbCC] Find credit card by number
  As a Cashier
  I want to find a credit card by number
  Because I need to inspect its registered data

  Scenario: [PM.FbCC.1] Find an existing credit card by number
    Given [PM.FbCC.1] a registered client with a credit card
    When [PM.FbCC.1] I search the credit card by number
    Then [PM.FbCC.1] I get the credit card

  Scenario: [PM.FbCC.2] Find a non existing credit card by number
    When [PM.FbCC.2] I search a non existing credit card by number
    Then [PM.FbCC.2] no credit card is found
