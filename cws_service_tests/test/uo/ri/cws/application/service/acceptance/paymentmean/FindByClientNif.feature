Feature: [PM.FbN] Find payment means by client nif
  As a Cashier
  I want to list the payment means of a client
  Because I need to know how the client can pay

  Scenario: [PM.FbN.1] Find payment means for a client with cash only
    Given [PM.FbN.1] a registered client with cash
    When [PM.FbN.1] I search payment means by client nif
    Then [PM.FbN.1] I get the cash payment mean

  Scenario: [PM.FbN.2] Find payment means for a client with several payment means
    Given [PM.FbN.2] a registered client with cash, credit cards and vouchers
    When [PM.FbN.2] I search payment means by client nif
    Then [PM.FbN.2] I get all client payment means

  Scenario: [PM.FbN.3] Find payment means for a non existing client
    When [PM.FbN.3] I search payment means for a non existing client nif
    Then [PM.FbN.3] I get an empty list
