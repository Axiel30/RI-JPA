Feature: [V.FbN] Find vouchers by client nif
  As a Manager
  I want to list vouchers of a client
  Because I need to know the available voucher amount

  Scenario: [V.FbN.1] Find vouchers for a client with vouchers
    Given [V.FbN.1] a registered client with several vouchers
    When [V.FbN.1] I search vouchers by client nif
    Then [V.FbN.1] I get all client vouchers

  Scenario: [V.FbN.2] Find vouchers for a client without vouchers
    Given [V.FbN.2] a registered client
    When [V.FbN.2] I search vouchers by client nif
    Then [V.FbN.2] I get an empty list

  Scenario: [V.FbN.3] Find vouchers for a non existing client
    When [V.FbN.3] I search vouchers for a non existing client nif
    Then [V.FbN.3] I get an empty list
