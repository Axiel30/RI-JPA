Feature: [M.U] Update a mechanic
  As a Manager
  I want to update a mechanic

  Scenario: [M.U.1] Update an existing mechanic
    Given [M.U.1] a mechanic
    When [M.U.1] I update the mechanic
    Then [M.U.1] the mechanic results updated
    And [M.U.1] mechanic version increases

  Scenario: [M.U.2] Try to update a non existing mechanic
    When [M.U.2] I try to update a non existing mechanic
    Then [M.U.2] a business error happens with an explaining message

  Scenario: [M.U.3] Try to update a mechanic updated in the while (wrong version)
    Given [M.U.3] a mechanic
    When [M.U.3] I try to update a mechanic updated in the while
    Then [M.U.3] a business error happens with an explaining message
