package com.leon3k0;



public class EmailGenerator{   
    public String generateEmail(String firstName, String lastName, String company) {
        String sanitizedFirstName = firstName.toLowerCase().replaceAll("\\s+", "");
        String sanitizedLastName = lastName.toLowerCase().replaceAll("\\s+", "");
        String sanitizedCompany = company.toLowerCase().replaceAll("\\s+", "");

        return sanitizedFirstName + "." + sanitizedLastName + "@" + sanitizedCompany + ".com";
    }
}