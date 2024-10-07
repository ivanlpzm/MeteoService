Feature: Temperature Integration Test

  Background:
    * url 'http://localhost:8080'

  Scenario: Verify the temperature data retrieval from the API
    Given path 'temperature'
    And param latitude = 35.6895
    And param longitude = 139.6917
    When method GET
    Then status 200
    And match response contains {"temperature": "#notnull","longitude": "#notnull","latitude": "#notnull"}

  Scenario: Verify error handling for invalid coordinates
    Given path 'temperature'
    And param latitude = 999
    And param longitude = 999
    When method GET
    Then status 400
    And match response contains {"timestamp": "#notnull","message": "#string","errors": {"getTemperature.latitude": "#string","getTemperature.longitude": "#string"}}

