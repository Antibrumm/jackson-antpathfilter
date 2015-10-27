package ch.mfrey.jackson.antpathfilter.test;

import java.util.ArrayList;
import java.util.List;

public class User {
    public static User buildMySelf() {
        User myself = new User();
        myself.setFirstName("Martin");
        myself.setLastName("Frey");
        myself.setEmail("somewhere@no.where");
        Address address = new Address();
        address.setStreetName("At my place");
        address.setStreetNumber("1");
        myself.setAddress(address);

        User manager = new User();
        manager.setFirstName("John");
        manager.setLastName("Doe");
        manager.setEmail("john.doe@no.where");
        myself.setManager(manager);

        myself.setReports(new ArrayList<User>());
        for (int i = 0; i < 10; i++) {
            final User report = new User();
            report.setFirstName("First " + i);
            report.setLastName("Doe " + i);
            report.setEmail("report" + i + "@no.where");
            myself.getReports().add(report);
        }

        return myself;
    }

    public static User buildRecursive() {
        User myself = new User();
        myself.setFirstName("Martin");
        myself.setLastName("Frey");
        myself.setEmail("somewhere@no.where");
        Address address = new Address();
        address.setStreetName("At my place");
        address.setStreetNumber("1");
        myself.setAddress(address);

        myself.setManager(myself);
        return myself;
    }

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