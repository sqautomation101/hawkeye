Feature: Login

  Background:
    Given the user navigates to Hawkeye Login Page

    @TC01-Login_Content
    Scenario: User sees the correct Login page content
      Then the Login page elements should be visible

    @TC02-Login_Success
    Scenario Outline: User successfully logs in to the Hawkeye
      When the user enters a username "<username>" and password "<password>"
      Then the user should be redirected to the Hawkeye Homepage

      Examples:
        | username   | password        |
        | hlsy       | Sophie@Test@25! |
        | daespiritu | Tulip080723!!!  |
      
    #@login-success
    #Scenario: User successfully logs in to the Hawkeye
    # When the user logs in with credentials from "validCredentials.csv"
    # Then the user should be redirected to the Hawkeye Homepage

    @TC03-Login_Errors
    Scenario Outline: User sees correct error message with invalid credentials
      When the user enters a username "<username>" and password "<password>"
      Then the error "<errorMessage>" should appear at <fieldName>

      Examples:
        | username | password | fieldName | errorMessage            |
        |          |          | username  | This field is required. |
        |          |          | password  | This field is required. |
        | hlsy     |          | password  | This field is required. |
        |          | Test@123 | username  | This field is required. |
        | hlsy          | wrongpassword | password  | Invalid ADID            |
        | wrongusername | Test@123 | password  | Invalid ADID            |