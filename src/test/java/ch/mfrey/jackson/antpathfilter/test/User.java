package ch.mfrey.jackson.antpathfilter.test;

import java.util.List;

public class User {

    private Address address;

    private String email;

    private String firstName;

    private String lastName;

    private User manager;
    
    private List<User> reports;

    public Address getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public User getManager() {
        return manager;
    }

    public List<User> getReports() {
        return reports;
    }

    public void setAddress(final Address address) {
        this.address = address;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public void setManager(final User manager) {
        this.manager = manager;
    }
    
    public void setReports(List<User> reports) {
        this.reports = reports;
    }
}