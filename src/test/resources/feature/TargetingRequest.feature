Feature: TargetingRequest

  @TC01-TargetingRequest-Attribute-Display-Verification
  Scenario Outline: User sees the attribute selected in the main content area
    Given the user is logged into Hawkeye as an "<role>"
    When the user navigates to the "<module>" module
    And the user navigates to the "<group_tab>" Group tab
    And the user selects the "<attribute>" attribute from "<sub_group>" subgroup
    Then the attribute "<attribute>" should be visible in the "<group_tab>" main content area

    Examples:

      | role  | module                 | group_tab    | sub_group            | attribute |
      | Admin | new request side panel | Demographics | General Demographics | Gender    |
      | Admin | new request side panel | Segments     | Transaction Dtag     | Company - Dtag|


  @TC02-TargetingRequest-EndtoEnd
  Scenario Outline: User create a new campaign request successfully
    Given the user is logged into Hawkeye as an "<role>"
    When the user navigates to the "<module>" module
    And the user navigates to the "<group_tab>" Group tab
    And the user selects the "<attribute>" attribute from "<sub_group>" subgroup
    And the user populates the attribute data under "<group_tab>" with name "<attribute>", type "<dataType>", value "<valueToSet>", and operator "<operator>"
    And the user saves the "<attribute>" attribute
    And the user populates the campaign title with "<campaignTitle>"
    And the user clicks the "Select Reference Campaign" button
    And the user clicks the "With No Campaign Reference"
    And the user clicks the "Proceed" button
    And the user clicks the "Back to Home" button
    Then the campaign "<campaignTitle>" should appear in the request table
    And the UI data for campaign "<campaignTitle>" should match the database records

    Examples:

      | role  | module                 | group_tab    | sub_group             | attribute | dataType | valueToSet | operator | campaignTitle       |
      | Admin | new request side panel | Transactions | Transactions - Header | ASV       | function | 5          | Equal to | 00010_Test Campaign |
#     | Admin | new request side panel | Transactions | Transactions OLS      | Co Brand  | action   | SMAC       |          |