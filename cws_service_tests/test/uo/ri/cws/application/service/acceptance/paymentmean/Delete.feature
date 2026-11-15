Feature: [PM.D] Delete a payment mean
  As a Cashier
  I want to delete unused payment means
  Because wrongly registered payment means must be removable

  Scenario: [PM.D.1] Delete an existing unused credit card
    Given [PM.D.1] a registered client with an unused credit card
    When [PM.D.1] I remove the payment mean
    Then [PM.D.1] the payment mean no longer exists

  Scenario: [PM.D.2] Delete an existing unused voucher
    Given [PM.D.2] a registered client with an unused voucher
    When [PM.D.2] I remove the payment mean
    Then [PM.D.2] the payment mean no longer exists

  Scenario: [PM.D.3] Try to remove a non existing payment mean
    When [PM.D.3] I try to remove a non existing payment mean
    Then [PM.D.3] a business error happens with an explaining message

  Scenario: [PM.D.4] Try to remove cash
    Given [PM.D.4] a registered client with cash
    When [PM.D.4] I try to remove the cash payment mean
    Then [PM.D.4] a business error happens with an explaining message

  Scenario: [PM.D.5] Try to remove a charged payment mean
    Given [PM.D.5] a registered client with a payment mean used in charges
    When [PM.D.5] I try to remove the payment mean
    Then [PM.D.5] a business error happens with an explaining message
