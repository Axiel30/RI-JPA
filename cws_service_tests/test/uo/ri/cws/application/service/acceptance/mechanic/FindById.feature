Feature: [M.FbI] Find a mechanic by id
  As a Manager
  I need to recover a mechanic by id
  To see its details

  Scenario: [M.FbI.1] Find an existing mechanic
    Given [M.FbI.1] a mechanic
    When [M.FbI.1] I look for mechanic
    Then [M.FbI.1] I get mechanic

  Scenario: [M.FbI.2] Try to find a mechanic with non existing id
    When [M.FbI.2] I try to find a non existent mechanic
    Then [M.FbI.2] mechanic is not found
