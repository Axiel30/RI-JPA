Feature: [M.FbN] Find a mechanic by nif
  As a Manager
  I need to recover a mechanic by nif
  To see its details

  Scenario: [M.FbN.1] Find an existing mechanic
    Given [M.FbN.1] a mechanic
    When [M.FbN.1] I look for mechanic by nif
    Then [M.FbN.1] I get mechanic

  Scenario: [M.FbN.2] Try to find a mechanic with non existing nif
    When [M.FbN.2] I try to find a mechanic by a non existent nif
    Then [M.FbN.2] mechanic is not found
