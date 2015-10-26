package ch.mfrey.jackson.antpathfilter.test;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AntPathFilterTest {

    private void assertAntFilter(final Object testObj, final String[] filters, final String outcome)
            throws JsonProcessingException {
        ObjectMapper objectMapper = ObjectMapperBuilder.buildObjectMapper(filters);
        String json = objectMapper.writeValueAsString(testObj);

        System.out.print("Filter: ");
        for (int i = 0; i < filters.length; i++) {
            System.out.print(filters[i] + ",");
        }
        System.out.println();

        System.out.println("Result: " + json);

        Assert.assertEquals(outcome, json);
    }

    private void assertAntFilter(final String[] filters, final String outcome) throws JsonProcessingException {
        User myself = buildMySelf();
        assertAntFilter(myself, filters, outcome);
    }

    private User buildMySelf() {
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

    private User buildRecursive() {
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

    @Test
    public void testExclusion() throws JsonProcessingException {
        String[] filters = new String[] { "**", "-manager" };
        assertAntFilter(
                filters,
                "{\"address\":{\"streetName\":\"At my place\",\"streetNumber\":\"1\"},\"email\":\"somewhere@no.where\",\"firstName\":\"Martin\",\"lastName\":\"Frey\",\"reports\":[{\"address\":null,\"email\":\"report0@no.where\",\"firstName\":\"First 0\",\"lastName\":\"Doe 0\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report1@no.where\",\"firstName\":\"First 1\",\"lastName\":\"Doe 1\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report2@no.where\",\"firstName\":\"First 2\",\"lastName\":\"Doe 2\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report3@no.where\",\"firstName\":\"First 3\",\"lastName\":\"Doe 3\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report4@no.where\",\"firstName\":\"First 4\",\"lastName\":\"Doe 4\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report5@no.where\",\"firstName\":\"First 5\",\"lastName\":\"Doe 5\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report6@no.where\",\"firstName\":\"First 6\",\"lastName\":\"Doe 6\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report7@no.where\",\"firstName\":\"First 7\",\"lastName\":\"Doe 7\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report8@no.where\",\"firstName\":\"First 8\",\"lastName\":\"Doe 8\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report9@no.where\",\"firstName\":\"First 9\",\"lastName\":\"Doe 9\",\"manager\":null,\"reports\":null}]}");
    }

    @Test
    public void testExclusion2() throws JsonProcessingException {
        String[] filters = new String[] { "**", "-manager", "-**.streetNumber" };
        assertAntFilter(
                filters,
                "{\"address\":{\"streetName\":\"At my place\"},\"email\":\"somewhere@no.where\",\"firstName\":\"Martin\",\"lastName\":\"Frey\",\"reports\":[{\"address\":null,\"email\":\"report0@no.where\",\"firstName\":\"First 0\",\"lastName\":\"Doe 0\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report1@no.where\",\"firstName\":\"First 1\",\"lastName\":\"Doe 1\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report2@no.where\",\"firstName\":\"First 2\",\"lastName\":\"Doe 2\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report3@no.where\",\"firstName\":\"First 3\",\"lastName\":\"Doe 3\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report4@no.where\",\"firstName\":\"First 4\",\"lastName\":\"Doe 4\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report5@no.where\",\"firstName\":\"First 5\",\"lastName\":\"Doe 5\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report6@no.where\",\"firstName\":\"First 6\",\"lastName\":\"Doe 6\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report7@no.where\",\"firstName\":\"First 7\",\"lastName\":\"Doe 7\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report8@no.where\",\"firstName\":\"First 8\",\"lastName\":\"Doe 8\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report9@no.where\",\"firstName\":\"First 9\",\"lastName\":\"Doe 9\",\"manager\":null,\"reports\":null}]}");
    }

    @Test
    public void testFirstName() throws JsonProcessingException {
        String[] filters = new String[] { "firstName" };
        assertAntFilter(filters, "{\"firstName\":\"Martin\"}");
    }

    @Test
    public void testFull() throws JsonProcessingException {
        String[] filters = new String[] { "**" };
        assertAntFilter(
                filters,
                "{\"address\":{\"streetName\":\"At my place\",\"streetNumber\":\"1\"},\"email\":\"somewhere@no.where\",\"firstName\":\"Martin\",\"lastName\":\"Frey\",\"manager\":{\"address\":null,\"email\":\"john.doe@no.where\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"manager\":null,\"reports\":null},\"reports\":[{\"address\":null,\"email\":\"report0@no.where\",\"firstName\":\"First 0\",\"lastName\":\"Doe 0\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report1@no.where\",\"firstName\":\"First 1\",\"lastName\":\"Doe 1\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report2@no.where\",\"firstName\":\"First 2\",\"lastName\":\"Doe 2\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report3@no.where\",\"firstName\":\"First 3\",\"lastName\":\"Doe 3\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report4@no.where\",\"firstName\":\"First 4\",\"lastName\":\"Doe 4\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report5@no.where\",\"firstName\":\"First 5\",\"lastName\":\"Doe 5\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report6@no.where\",\"firstName\":\"First 6\",\"lastName\":\"Doe 6\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report7@no.where\",\"firstName\":\"First 7\",\"lastName\":\"Doe 7\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report8@no.where\",\"firstName\":\"First 8\",\"lastName\":\"Doe 8\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report9@no.where\",\"firstName\":\"First 9\",\"lastName\":\"Doe 9\",\"manager\":null,\"reports\":null}]}");
    }

    @Test
    public void testInclusion() throws JsonProcessingException {
        String[] filters = new String[] { "*", "address.*", "manager.firstName" };
        assertAntFilter(
                filters,
                "{\"address\":{\"streetName\":\"At my place\",\"streetNumber\":\"1\"},\"email\":\"somewhere@no.where\",\"firstName\":\"Martin\",\"lastName\":\"Frey\",\"manager\":{\"firstName\":\"John\"},\"reports\":[{},{},{},{},{},{},{},{},{},{}]}");
    }

    @Test
    public void testManagerNames() throws JsonProcessingException {
        String[] filters = new String[] { "manager", "manager.firstName", "manager.lastName" };
        assertAntFilter(filters, "{\"manager\":{\"firstName\":\"John\",\"lastName\":\"Doe\"}}");
    }

    @Test
    public void testRecursive1Levels() throws JsonProcessingException {
        String[] filters = new String[] { "**", "-manager" };
        assertAntFilter(
                buildRecursive(),
                filters,
                "{\"address\":{\"streetName\":\"At my place\",\"streetNumber\":\"1\"},\"email\":\"somewhere@no.where\",\"firstName\":\"Martin\",\"lastName\":\"Frey\",\"reports\":null}");
    }

    @Test
    public void testReports() throws JsonProcessingException {
        String[] filters = new String[] { "reports", "reports.firstName" };
        assertAntFilter(
                buildMySelf(),
                filters,
                "{\"reports\":[{\"firstName\":\"First 0\"},{\"firstName\":\"First 1\"},{\"firstName\":\"First 2\"},{\"firstName\":\"First 3\"},{\"firstName\":\"First 4\"},{\"firstName\":\"First 5\"},{\"firstName\":\"First 6\"},{\"firstName\":\"First 7\"},{\"firstName\":\"First 8\"},{\"firstName\":\"First 9\"}]}");
    }

}
