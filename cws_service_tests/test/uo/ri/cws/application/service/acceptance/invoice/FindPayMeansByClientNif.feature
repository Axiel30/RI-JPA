Feature: [I.FPM] Find a client's payment means for invoicing
  As a Cashier
  I want to find the payment means registered for a client
  Because I need their details when settling an invoice

  Scenario: [I.FPM.1] Find a client's cash payment mean
    Given [I.FPM.1] a registered client with a cash payment mean
    When [I.FPM.1] I search for payment means by the client's nif
    Then [I.FPM.1] I get one CashDto with its id, version, client id and accumulated amount

  Scenario: [I.FPM.2] Find cash, card and voucher payment means
    Given [I.FPM.2] a registered client with cash, a credit card and a voucher
    When [I.FPM.2] I search for payment means by the client's nif
    Then [I.FPM.2] I get exactly those three payment means with their common data
    And [I.FPM.2] the CardDto includes its number, type and expiration month
    And [I.FPM.2] the VoucherDto includes its code, description and available amount

  Scenario: [I.FPM.3] Exclude payment means owned by another client
    Given [I.FPM.3] two registered clients with payment means
    When [I.FPM.3] I search for the first client's payment means by nif
    Then [I.FPM.3] I get only payment means owned by the first client

  Scenario: [I.FPM.4] Find a client without payment means
    Given [I.FPM.4] a registered client without payment means
    When [I.FPM.4] I search for payment means by the client's nif
    Then [I.FPM.4] I get an empty list

  Scenario: [I.FPM.5] Find payment means for a non-existing client
    When [I.FPM.5] I search for payment means by a non-existing client nif
    Then [I.FPM.5] I get an empty list
