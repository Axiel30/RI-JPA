Feature: [M.FA] Find all mechanics
  As a Manager
  I need to know data about all mechanics

  Scenario: [M.FA.1] Find all existing mechanic
    Given [M.FA.1] the following relation of mechanics
      | nif | name   | surname   |
      | 12345678Z | Name-1 | Surname-1 |
      | 98765432M | Name-2 | Surname-2 |
      | 45678912S | Name-3 | Surname-3 |
    When [M.FA.1] we read all mechanics
    Then [M.FA.1] we get all of them
