
Feature: [M.D] Delete a mechanic
  As a Manager
  I want to delete a mechanic in case they have not been used
  Because we need to keep the system clean

  Scenario: [M.D.1] Delete an existing unused mechanic
    Given [M.D.1] a registered mechanic
    When [M.D.1] I remove the mechanic
    Then [M.D.1] the mechanic no longer exists

  Scenario: [M.D.2] Try to remove a non existing mechanic
    When [M.D.2] I try to remove a non existent mechanic
    Then [M.D.2] a business error happens with an explaining message

  Scenario: [M.D.3] Try to remove a used mechanic with work orders
    Given [M.D.3] a mechanic with work orders registered
    When [M.D.3] I try to remove the mechanic
    Then [M.D.3] a business error happens with an explaining message

  Scenario: [M.D.4] Try to remove a used mechanic with interventions in work orders
    Given [M.D.4] a mechanic with interventions registered
    When [M.D.4] I try to remove the mechanic
    Then [M.D.4] a business error happens with an explaining message
