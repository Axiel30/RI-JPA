
Feature: [V.GW] Generate vouchers for workorders
  As a Manager
  I want to generate vouchers for paid workorders
  Because loyal clients with repeated repairs must be rewarded

  Scenario: [V.GW.1] Generate one voucher for three paid workorders
    Given [V.GW.1] a client with three invoiced and paid workorders
    When [V.GW.1] I generate vouchers
    Then [V.GW.1] one voucher of 20 euro is generated for the client
    And [V.GW.1] the voucher description is "Por tres órdenes de trabajo"
    And [V.GW.1] the workorders are marked as used for the voucher

  Scenario: [V.GW.2] Do not generate a workorder voucher without three paid workorders
    Given [V.GW.2] a client with two invoiced and paid workorders
    When [V.GW.2] I generate vouchers
    Then [V.GW.2] no voucher is generated

  Scenario: [V.GW.3] Do not generate a workorder voucher if one workorder invoice is not paid
    Given [V.GW.3] a client with three invoiced workorders and one invoice is not paid
    When [V.GW.3] I generate vouchers
    Then [V.GW.3] no voucher is generated

  Scenario: [V.GW.4] Do not reuse workorders already used for a voucher
    Given [V.GW.4] a client with a voucher generated from three paid workorders
    When [V.GW.4] I generate vouchers again
    Then [V.GW.4] no new workorder voucher is generated for the client

  Scenario: [V.GW.5] Generate several workorder vouchers for the same client
    Given [V.GW.5] a client with six invoiced and paid workorders
    When [V.GW.5] I generate vouchers
    Then [V.GW.5] two vouchers of 20 euro are generated for the client
    And [V.GW.5] the voucher description is "Por tres órdenes de trabajo"
    And [V.GW.5] the workorders are marked as used for the voucher

  Scenario: [V.GW.6] Generate only one new voucher when not enough new workorders are paid
    Given [V.GW.6] a client with a voucher generated from three paid workorders
    And [V.GW.6] six new invoiced workorders and only five of them are paid
    When [V.GW.6] I generate vouchers
    Then [V.GW.6] one new voucher of 20 euro is generated for the client
    And [V.GW.6] the voucher description is "Por tres órdenes de trabajo"
    And [V.GW.6] the workorders are marked as used for the voucher
    
