Feature: [V.GR] Generate vouchers for recommendations
  As a Manager
  I want to generate vouchers for recommendations
  Because clients that recommend the workshop must be rewarded

  Scenario: [V.GR.1] Generate one voucher for three paid recommendations
    Given [V.GR.1] a client with a paid invoice
    And [V.GR.1] three recommended clients with paid invoices
    When [V.GR.1] I generate vouchers
    Then [V.GR.1] one voucher of 25 euro is generated for the client
    And [V.GR.1] the voucher description is "Por recomendación"
    And [V.GR.1] the recommendations are marked as used for the voucher

  Scenario: [V.GR.2] Do not generate a recommendation voucher without three recommendations
    Given [V.GR.2] a client with a paid invoice
    And [V.GR.2] two recommended clients with paid invoices
    When [V.GR.2] I generate vouchers
    Then [V.GR.2] no voucher is generated

  Scenario: [V.GR.3] Do not generate a recommendation voucher if the sponsor has no paid invoice
    Given [V.GR.3] a client without paid invoices
    And [V.GR.3] three recommended clients with paid invoices
    When [V.GR.3] I generate vouchers
    Then [V.GR.3] no voucher is generated

  Scenario: [V.GR.4] Do not generate a recommendation voucher if one recommended client has no paid invoice
    Given [V.GR.4] a client with a paid invoice
    And [V.GR.4] three recommended clients and one of them has no paid invoice
    When [V.GR.4] I generate vouchers
    Then [V.GR.4] no voucher is generated

  Scenario: [V.GR.5] Do not reuse recommendations already used for a voucher
    Given [V.GR.5] a client with a voucher generated from three recommendations
    When [V.GR.5] I generate vouchers again
    Then [V.GR.5] no new recommendation voucher is generated for the client

  Scenario: [V.GR.6] Generate several recommendation vouchers for the same client
    Given [V.GR.6] a client with a paid invoice
    And [V.GR.6] six recommended clients with paid invoices
    When [V.GR.6] I generate vouchers
    Then [V.GR.6] two vouchers of 25 euro are generated for the client
    And [V.GR.6] the voucher description is "Por recomendación"
    And [V.GR.6] the recommendations are marked as used for the voucher

  Scenario: [V.GR.7] Generate a new recommendation voucher for a client that already has a previous one
    Given [V.GR.7] a client with a voucher generated from three recommendations
    And [V.GR.7] four new recommended clients with paid invoices
    And [V.GR.7] two another new recommended clients with no paid invoices
	When [V.GR.7] I generate vouchers
	Then [V.GR.7] one new voucher of 25 euro is generated for the client
	And [V.GR.7] the voucher description is "Por recomendación"
	And [V.GR.7] the recommendations are marked as used for the voucher

  Scenario: [V.GR.8] Generate a new voucher with mixed new recommendations
	Given [V.GR.8] a client with a voucher generated from three recommendations
	And [V.GR.8] four new recommended clients with paid invoices
	And [V.GR.8] two of the new recommended clients have no paid invoices
	When [V.GR.8] I generate vouchers
	Then [V.GR.8] one new voucher of 25 euro is generated for the client
    And [V.GR.8] the voucher description is "Por recomendación"
    And [V.GR.8] the recommendations are marked as used for the voucher
