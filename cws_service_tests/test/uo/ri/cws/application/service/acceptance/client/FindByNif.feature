Feature: [C.FbN] Find a client by nif
  As a Foreman
  I need to know data about a client

  Scenario: [C.FbN.1] Find an existing client by nif
    Given [C.FbN.1] a registered client
    When [C.FbN.1] I search the client by nif
    Then [C.FbN.1] I get the client

  Scenario: [C.FbN.2] Find a non existing client by nif
    When [C.FbN.2] I search a non existing client by nif
    Then [C.FbN.2] no client is found
