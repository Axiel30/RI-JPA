Feature: [I.C] Create one invoice for one or multiple jobs
  As a Cashier
  I want to create an invoice for several jobs done for a single client 
  Because I want to get paid

  Scenario: [I.C.1] Create one invoice for an existing workorder
    Given [I.C.1] a client registered with a vehicle and one approved workorder
    When [I.C.1] I create an invoice for the workorders
    Then [I.C.1] an invoice is created
	    #  And [I.C.1] the invoice is linked to the workorder
	    #  And [I.C.1] the invoice subtotal is the sum of the workorder total
	    #  And [I.C.1] the invoice total is the sum of the workorder total plus taxes
	    #  And [I.C.1] the invoice is marked as ISSUED
    And [I.C.1] the workorder is marked as INVOICED
   

  Scenario: [I.C.2] Create one invoice for multiple existing workorders
    Given [I.C.2] a client registered with a vehicle and a list of several approved workorders
    When [I.C.2] I create an invoice for the workorders
    Then [I.C.2] an invoice is created
	    #  And [I.C.1] the invoice is linked to the workorders
	    #  And [I.C.1] the invoice subtotal is the sum of the workorders total
	    #  And [I.C.1] the invoice total is the sum of the workorders total plus taxes
	    #  And [I.C.1] the invoice is marked as ISSUED
    And [I.C.2] the workorders are marked as INVOICED

  Scenario: [I.C.3] Trying to create one invoice there is one non existing workorder
    Given [I.C.3] a client registered with a vehicle and a list of several finished workorders
    When [I.C.3] I try to create an invoice including one non existent workorder id
    Then [I.C.3] a business error happens with an explaining message

  Scenario: [I.C.4] Trying to create one invoice there is one workorder ASSIGNED
    Given [I.C.4] a client registered with a vehicle and a list of several finished workorder
    And [I.C.4] one ASSIGNED workorder
    When [I.C.4] I try to create an invoice
    Then [I.C.4] a business error happens with an explaining message

  Scenario: [I.C.5] Trying to create one invoice there is one workorder OPEN
    Given [I.C.5] a client registered with a vehicle and a list of several finished workorder
    And [I.C.5] one OPEN workorder
    When [I.C.5] I try to create an invoice
    Then [I.C.5] a business error happens with an explaining message

  Scenario: [I.C.6] Trying to create one invoice but one workorder is INVOICED
    Given [I.C.6] a client registered with a vehicle and a list of several finished workorder
    And [I.C.6] one INVOICED workorder
    When [I.C.6] I try to create an invoice
    Then [I.C.6] a business error happens with an explaining message
