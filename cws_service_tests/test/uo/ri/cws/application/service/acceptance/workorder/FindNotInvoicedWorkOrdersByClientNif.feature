Feature: [W.FnI] Find not yet invoiced workorders for a client by client nif
  As a Cashier
  I need to find not yet invoiced workorders
  To generate the invoice

  Scenario: [W.FnI.1] Find not invoiced workorders by client nif
    Given [W.FnI.1] a client registered with a vehicle and a list of several approved workorders
    When [W.FnI.1] I search not invoiced workorders by client nif
    Then [W.FnI.1] I get only approved workorders

  Scenario: [W.FnI.2] Find not invoiced workorders by client nif with no workorders
    Given [W.FnI.2] a client registered
    When [W.FnI.2] I search not invoiced workorders by client nif
    Then [W.FnI.2] I get an empty list

  Scenario: [W.FnI.3] Find not invoiced workorders by client nif with some not approved workorders
    Given [W.FnI.3] a client registered with a vehicle and a list of several approved workorders
    And [W.FnI.3] one INVOICED workorder
    And [W.FnI.3] one OPEN workorder
    And [W.FnI.3] one ASSIGNED workorder
    And [W.FnI.3] one FINISHED workorder
    When [W.FnI.3] I search not invoiced workorders by client nif
    Then [W.FnI.3] I get only approved workorders


#  Scenario: [W.FnI.3] Find not invoiced workorders by client nif with some not finished workorders
#    Given [W.FnI.3] a client registered with a vehicle and a list of several finished workorders
#    And [W.FnI.3] one INVOICED workorder
#    And [W.FnI.3] one OPEN workorder
#    And [W.FnI.3] one ASSIGNED workorder
#    When [W.FnI.3] I search not invoiced workorders by client nif
#    Then [W.FnI.3] I get only finished workorders

  Scenario: [W.FnI.4] Try to find workorders for a non existent client
    When [W.FnI.4] I search workorders for a non existent nif
    Then [W.FnI.4] I get an empty list
