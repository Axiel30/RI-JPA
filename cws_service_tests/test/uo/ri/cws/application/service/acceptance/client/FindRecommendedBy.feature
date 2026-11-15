Feature: [C.FRbN] Find clients recommended by client nif
  As a Foreman
  I need to know clients recommended by a client

  Scenario: [C.FRbN.1] Find clients recommended by an existing client
    Given [C.FRbN.1] a registered client with several recommended clients
    When [C.FRbN.1] I search recommended clients by recommender nif
    Then [C.FRbN.1] I get all recommended clients

  Scenario: [C.FRbN.2] Find clients recommended by a client without recommendations
    Given [C.FRbN.2] a registered client
    When [C.FRbN.2] I search recommended clients by recommender nif
    Then [C.FRbN.2] I get an empty list

  Scenario: [C.FRbN.3] Find clients recommended by a non existing client
    When [C.FRbN.3] I search recommended clients for a non existing recommender nif
    Then [C.FRbN.3] I get an empty list
