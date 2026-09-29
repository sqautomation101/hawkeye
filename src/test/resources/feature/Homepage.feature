Feature: Homepage

  @TC01-Homepage_Content_Verification
  Scenario Outline: User sees the correct Homepage content
    Given the user is logged into Hawkeye as an "<role>"
    Then the Homepage elements should be visible
    And the user should only see the following filter tabs:
    #data table belongs exclusively to a single step, runs once only, List or Map
      | All         |
      | Draft       |
      | Queued      |
      | In-Progress |
      | Compiling   |
      | Failed      |
      | For-Review  |
      | Completed   |

    #And testing only
    And the side panel links should be visible for "<role>"
    #loops entire scenario(once per data row), requires matching <placeholder> to your steps, must matched with your method parameter
    Examples:

      | role  |
      | Admin |
      | Editor |

  @TC02-Homepage_Navigation
  Scenario Outline: User can route cleanly to all modules
    Given the user is logged into Hawkeye as an "<role>"
    When the user navigates to the "<module>" module
    Then the URL should contain "<expected_path>"

    Examples:

      | role   | module                 | expected_path     |
      | Admin  | homepage               | /home             |
      | Admin  | user management        | /Maintenance?Id=1 |
      | Admin  | attributes             | /Maintenance?Id=2 |
      | Admin  | new request side panel | /Requests         |
      | Admin  | new request header     | /Requests         |
      | Editor | homepage               | /home             |
      | Editor | new request side panel | /Requests         |
      | Editor | new request header     | /Requests         |


  @TC03-Database_Data_Validation
  Scenario Outline: Verify live dashboard record data perfectly aligns with database state
    Given the user is logged into Hawkeye as an "Admin"
    When the user views the requests dashboard table
    Then the UI data for campaign "<campaign_title>" should match the database records

    Examples:

      | campaign_title      |
      | 00008_Test Campaign |



