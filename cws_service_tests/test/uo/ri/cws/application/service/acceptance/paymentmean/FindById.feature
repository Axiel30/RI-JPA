Feature: [PM.FbI] Find payment mean by id
  As a Cashier
  I want to find a payment mean by id
  Because I need to inspect its registered data

  Scenario: [PM.FbI.1] Find an existing cash payment mean
    Given [PM.FbI.1] a registered client with cash
    When [PM.FbI.1] I search the payment mean by id
    Then [PM.FbI.1] I get the cash payment mean

  Scenario: [PM.FbI.2] Find an existing credit card
    Given [PM.FbI.2] a registered client with a credit card
    When [PM.FbI.2] I search the payment mean by id
    Then [PM.FbI.2] I get the credit card

  Scenario: [PM.FbI.3] Find an existing voucher
    Given [PM.FbI.3] a registered client with a voucher
    When [PM.FbI.3] I search the payment mean by id
    Then [PM.FbI.3] I get the voucher

  Scenario: [PM.FbI.4] Find a non existing payment mean
    When [PM.FbI.4] I search a non existing payment mean by id
    Then [PM.FbI.4] no payment mean is found
