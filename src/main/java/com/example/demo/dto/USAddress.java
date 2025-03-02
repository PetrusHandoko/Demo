package com.example.demo.dto;
public record USAddress(String street, String city, String state, String zipCode) {
    // Optionally, you can add custom methods or constructors here
    // for validation or additional functionality.

    public String formattedAddress() {
        return String.format("%s, %s, %s %s", street, city, state, zipCode);
    }
}