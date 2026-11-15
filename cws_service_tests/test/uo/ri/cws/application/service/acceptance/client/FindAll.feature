Feature: [C.FA] Find all clients
  As a Foreman
  I need to know data about all clients

  Scenario: [C.FA.1] Find all existing clients
    Given [C.FA.1] a relation of clients
    When [C.FA.1] we read all clients
    Then [C.FA.1] we get all of them

  Scenario: [C.FA.2] Find all clients when there are no clients
    Given [C.FA.2] there are no clients
    When [C.FA.2] we read all clients
    Then [C.FA.2] we get an empty list
