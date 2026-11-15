Feature: [PM.AV] Add a voucher
  As a Cashier
  I want to register a voucher for a client
  Because clients can use vouchers to pay invoices

  Scenario: [PM.AV.1] Add a voucher to an existing client
    Given [PM.AV.1] a registered client
    When [PM.AV.1] I add a new voucher for that client
    Then [PM.AV.1] the voucher results added to the system

  Scenario: [PM.AV.2] Try to add a voucher for a non existing client
    When [PM.AV.2] I try to add a voucher for a non existing client
    Then [PM.AV.2] a business error happens with an explaining message

  Scenario: [PM.AV.3] Try to add a voucher with a repeated code
    Given [PM.AV.3] a registered client with a voucher
    When [PM.AV.3] I try to add another voucher with same code
    Then [PM.AV.3] a business error happens with an explaining message
