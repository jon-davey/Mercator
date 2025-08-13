Feature: Add Item To Basket

  Scenario: Add Highest Price Item To Basket

    Given The user has navigated to the following URL: https://www.saucedemo.com/
    And The user has logged in using the following details: (username: standard_user, password: secret_sauce)

    When The user selects the highest price item
    Then The highest price item is shown

    When The user adds the selected highest price item to the cart
    Then The highest price item appears in the cart
