
Feature: [M.A] Add a mechanic
  As a Manager
  I want to register a mechani
  Because we need a new worker

  Scenario: [M.A.1] Add a non existing mechanic
    Given [M.A.1] a new non existing mechanic
    When [M.A.1] I add a that mechanic
    Then [M.A.1] the mechanic results added to the system

  Scenario: [M.A.2] Try to add a mechanic with a repeated nif
    Given [M.A.2] a registered mechanic
    When [M.A.2] I try to add a new mechanic with same nif
    Then [M.A.2] a business error happens with an explaining message
