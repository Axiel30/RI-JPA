Feature: [PM.FbC] Find voucher by code
  As a Cashier
  I want to find a voucher by code
  Because I need to inspect its registered data

  Scenario: [PM.FbC.1] Find an existing voucher by code
    Given [PM.FbC.1] a registered client with a voucher
    When [PM.FbC.1] I search the voucher by code
    Then [PM.FbC.1] I get the voucher

  Scenario: [PM.FbC.2] Find a non existing voucher by code
    When [PM.FbC.2] I search a non existing voucher by code
    Then [PM.FbC.2] no voucher is found
