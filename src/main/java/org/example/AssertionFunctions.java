package org.example;

import org.junit.Assert;

public class AssertionFunctions {
    public static void assertEquals(String assertionName, String expectedValue, String actualValue) {
        if (actualValue.equals(expectedValue)) {
            System.out.println("\nASSERTION PASS: " + assertionName + "\n        Expected value = '" + expectedValue + "'\n        Actual value =   '" + actualValue + "'");
        } else {
            Assert.fail("\nASSERTION FAIL: " + assertionName + "\n        Expected value = '" + expectedValue + "'\n        Actual value =   '" + actualValue + "'");
        }
    }
}
