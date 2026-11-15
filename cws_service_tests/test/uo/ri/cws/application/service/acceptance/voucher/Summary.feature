Feature: [V.S] Get voucher summary
  As a Manager
  I want to get aggregated voucher information by client
  Because I need to know the pending voucher liability

  Scenario: [V.S.1] Get voucher summary when there are no vouchers
    Given [V.S.1] there are no vouchers
    When [V.S.1] I ask for the voucher summary
    Then [V.S.1] I get an empty list

  Scenario: [V.S.2] Get voucher summary for several clients
    Given [V.S.2] several clients with consumed and available vouchers
    When [V.S.2] I ask for the voucher summary
    Then [V.S.2] I get one aggregated row per client with vouchers
    And [V.S.2] the rows are sorted by available amount descending
    And [V.S.2] each row includes issued, total, consumed and available amounts
