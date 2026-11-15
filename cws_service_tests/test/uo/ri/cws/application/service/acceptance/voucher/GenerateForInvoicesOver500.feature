Feature: [V.GI] Generate vouchers for invoices over 500 euro
  As a Manager
  I want to generate vouchers for invoices over 500 euro
  Because clients with high value invoices must be rewarded

  Scenario: [V.GI.1] Generate one voucher for an invoice over 500 euro
    Given [V.GI.1] a client with a paid invoice over 500 euro
    When [V.GI.1] I generate vouchers
    Then [V.GI.1] one voucher of 30 euro is generated for the client
    And [V.GI.1] the voucher description is "Por factura de más de 500"
    And [V.GI.1] the invoice is marked as used for the voucher

  Scenario: [V.GI.2] Do not generate an invoice voucher for an invoice of 500 euro
    Given [V.GI.2] a client with a paid invoice of 500 euro
    When [V.GI.2] I generate vouchers
    Then [V.GI.2] no voucher is generated

  Scenario: [V.GI.3] Do not generate an invoice voucher for an unpaid invoice over 500 euro
    Given [V.GI.3] a client with an unpaid invoice over 500 euro
    When [V.GI.3] I generate vouchers
    Then [V.GI.3] no voucher is generated

  Scenario: [V.GI.4] Do not reuse an invoice already used for a voucher
    Given [V.GI.4] a client with a voucher generated from a paid invoice over 500 euro
    When [V.GI.4] I generate vouchers again
    Then [V.GI.4] no new invoice voucher is generated for the client

  Scenario: [V.GI.5] Generate several invoice vouchers for the same client
    Given [V.GI.5] a client with two paid invoices over 500 euro
    When [V.GI.5] I generate vouchers
    Then [V.GI.5] two vouchers of 30 euro are generated for the client
    And [V.GI.5] the voucher description is "Por factura de más de 500"
    And [V.GI.5] the invoices are marked as used for the voucher

  Scenario: [V.GI.6] Generate a new invoice voucher for a client that already has a previous one
    Given [V.GI.6] a client with a voucher generated from a paid invoice over 500 euro
    And [V.GI.6] a new paid invoice over 500 euro
    And [V.GI.6] another non-paid invoice over 500 euro
    When [V.GI.6] I generate vouchers
    Then [V.GI.6] one new voucher of 30 euro is generated for the client
    And [V.GI.6] the voucher description is "Por factura de más de 500"
    And [V.GI.6] the invoice is marked as used for the voucher
