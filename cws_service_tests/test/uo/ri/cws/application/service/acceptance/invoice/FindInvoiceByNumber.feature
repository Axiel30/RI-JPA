Feature: [I.FbN] Find an invoice by its number
  As a Cashier
  I want to find an invoice by its number
  Because I need to review its details

  Scenario: [I.FbN.1] Find an invoice among several invoices
    Given [I.FbN.1] two invoices with different numbers
    When [I.FbN.1] I search for one invoice by its number
    Then [I.FbN.1] I get the invoice with the requested id, number, date and version
    And [I.FbN.1] its subtotal, VAT percentage, VAT amount and total match the invoice

  Scenario: [I.FbN.2] Find an invoice with a non-existing number
    When [I.FbN.2] I search for an invoice with a non-existing number
    Then [I.FbN.2] I get an empty result
