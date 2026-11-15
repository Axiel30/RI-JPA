Feature: [I.S] Settle an invoice with one or more payment means
  As a Cashier
  I want to charge the payment means chosen for an invoice
  Because the invoice must be recorded as paid

  Scenario: [I.S.1] Settle an issued invoice with cash
    Given [I.S.1] an issued invoice for 121.00 and a cash payment mean
    When [I.S.1] I settle the invoice by charging 121.00 to the cash payment mean
    Then [I.S.1] the invoice is marked as PAID
    And [I.S.1] one charge for 121.00 links the invoice and the cash payment mean
    And [I.S.1] the cash payment mean accumulated amount increases by 121.00

  Scenario: [I.S.2] Settle an invoice with a card valid through the current month
    Given [I.S.2] an issued invoice for 121.00 and a card valid through the current month
    When [I.S.2] I settle the invoice by charging 121.00 to the card
    Then [I.S.2] the invoice is marked as PAID
    And [I.S.2] one charge for 121.00 links the invoice and the card
    And [I.S.2] the card accumulated amount increases by 121.00

  Scenario: [I.S.3] Split an invoice between a voucher and cash
    Given [I.S.3] an issued invoice for 121.00
    And [I.S.3] a voucher with 50.00 available and a cash payment mean
    When [I.S.3] I settle the invoice with 50.00 from the voucher and 71.00 in cash
    Then [I.S.3] the invoice is marked as PAID with two charges for the requested amounts
    And [I.S.3] the voucher available amount becomes 0.00
    And [I.S.3] each payment mean accumulated amount increases by its charge

  Scenario: [I.S.4] Settle an invoice with a payment mean owned by another client
    Given [I.S.4] an issued invoice for 121.00 and a cash payment mean owned by another client
    When [I.S.4] I settle the invoice by charging 121.00 to that payment mean
    Then [I.S.4] the invoice is marked as PAID
    And [I.S.4] the charge is linked to that payment mean

  Scenario: [I.S.5] Reject settlement of a non-existing invoice
    Given [I.S.5] a cash payment mean
    When [I.S.5] I try to settle a non-existing invoice with that payment mean
    Then [I.S.5] a business error happens with an explaining message
    And [I.S.5] no charge is created or accumulated amount changed

  Scenario: [I.S.6] Reject settlement of an invoice already paid
    Given [I.S.6] a paid invoice and a cash payment mean
    When [I.S.6] I try to settle the invoice again with that payment mean
    Then [I.S.6] a business error happens with an explaining message
    And [I.S.6] the existing charges and accumulated amounts remain unchanged

  Scenario: [I.S.7] Reject a non-existing payment mean
    Given [I.S.7] an issued invoice for 121.00
    When [I.S.7] I try to settle it by charging 121.00 to a non-existing payment mean
    Then [I.S.7] a business error happens with an explaining message
    And [I.S.7] the invoice remains ISSUED with no charges

  Scenario: [I.S.8] Reject charges whose sum differs from the invoice total
    Given [I.S.8] an issued invoice for 121.00 and a cash payment mean
    When [I.S.8] I try to settle the invoice by charging 120.99
    Then [I.S.8] a business error happens with an explaining message
    And [I.S.8] the invoice remains ISSUED with no charges
    And [I.S.8] the cash payment mean accumulated amount remains unchanged

  Scenario: [I.S.9] Reject an expired card
    Given [I.S.9] an issued invoice for 121.00 and a card expired before the current month
    When [I.S.9] I try to settle the invoice by charging 121.00 to the card
    Then [I.S.9] a business error happens with an explaining message
    And [I.S.9] the invoice remains ISSUED with no charges
    And [I.S.9] the card accumulated amount remains unchanged

  Scenario: [I.S.10] Reject a voucher without enough available balance
    Given [I.S.10] an issued invoice for 121.00 and a voucher with 120.00 available
    When [I.S.10] I try to settle the invoice by charging 121.00 to the voucher
    Then [I.S.10] a business error happens with an explaining message
    And [I.S.10] the invoice remains ISSUED with no charges
    And [I.S.10] the voucher available and accumulated amounts remain unchanged

  Scenario: [I.S.11] Reject all charges if one payment mean cannot be charged
    Given [I.S.11] an issued invoice for 121.00
    And [I.S.11] a cash payment mean and a card expired before the current month
    When [I.S.11] I try to settle it with 71.00 in cash and 50.00 on the card
    Then [I.S.11] a business error happens with an explaining message
    And [I.S.11] the invoice remains ISSUED with no charges
    And [I.S.11] neither payment mean accumulated amount changes

  Scenario: [I.S.12] Compare and record charge amounts at cent precision
    Given [I.S.12] an issued invoice for 121.00 and two cash payment means
    When [I.S.12] I settle it by charging 60.005 to one and 60.995 to the other
    Then [I.S.12] the invoice is marked as PAID
    And [I.S.12] the charges are recorded as 60.00 and 61.00 respectively
    And [I.S.12] the payment means accumulated amounts increase by those recorded amounts
