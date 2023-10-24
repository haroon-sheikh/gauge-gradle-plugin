package org.gauge.gradle.sample;

public class Main {
    public static void main(String[] args) {
        Test test = new Test("test_value");
        System.out.println("org.gauge.gradle.sample.Test Value is : " + test.getValue());
    }
}
