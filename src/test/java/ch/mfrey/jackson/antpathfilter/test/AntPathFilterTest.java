package ch.mfrey.jackson.antpathfilter.test;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.mfrey.jackson.antpathfilter.Jackson2Helper;

public class AntPathFilterTest {
    private final Jackson2Helper jackson2Helper = new Jackson2Helper();

    private void assertAntFilter(final Object testObj, final String[] filters, final String outcome)
            throws JsonProcessingException {
        ObjectMapper objectMapper = jackson2Helper.buildObjectMapper(filters);
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
        assertAntFilter(User.buildMySelf(), filters, outcome);
    }

    @Test
    public void testExclusion() throws JsonProcessingException {
        String[] filters = new String[] { "**", "-manager" };
        assertAntFilter(filters,
                "{\"address\":{\"streetName\":\"At my place\",\"streetNumber\":\"1\"},\"email\":\"somewhere@no.where\",\"firstName\":\"Martin\",\"lastName\":\"Frey\",\"reports\":[{\"address\":null,\"email\":\"report0@no.where\",\"firstName\":\"First 0\",\"lastName\":\"Doe 0\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report1@no.where\",\"firstName\":\"First 1\",\"lastName\":\"Doe 1\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report2@no.where\",\"firstName\":\"First 2\",\"lastName\":\"Doe 2\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report3@no.where\",\"firstName\":\"First 3\",\"lastName\":\"Doe 3\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report4@no.where\",\"firstName\":\"First 4\",\"lastName\":\"Doe 4\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report5@no.where\",\"firstName\":\"First 5\",\"lastName\":\"Doe 5\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report6@no.where\",\"firstName\":\"First 6\",\"lastName\":\"Doe 6\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report7@no.where\",\"firstName\":\"First 7\",\"lastName\":\"Doe 7\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report8@no.where\",\"firstName\":\"First 8\",\"lastName\":\"Doe 8\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report9@no.where\",\"firstName\":\"First 9\",\"lastName\":\"Doe 9\",\"manager\":null,\"reports\":null}]}");
    }

    @Test
    public void testExclusion2() throws JsonProcessingException {
        String[] filters = new String[] { "**", "-manager", "-**.streetNumber" };
        assertAntFilter(filters,
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
        assertAntFilter(filters,
                "{\"address\":{\"streetName\":\"At my place\",\"streetNumber\":\"1\"},\"email\":\"somewhere@no.where\",\"firstName\":\"Martin\",\"lastName\":\"Frey\",\"manager\":{\"address\":null,\"email\":\"john.doe@no.where\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"manager\":null,\"reports\":null},\"reports\":[{\"address\":null,\"email\":\"report0@no.where\",\"firstName\":\"First 0\",\"lastName\":\"Doe 0\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report1@no.where\",\"firstName\":\"First 1\",\"lastName\":\"Doe 1\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report2@no.where\",\"firstName\":\"First 2\",\"lastName\":\"Doe 2\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report3@no.where\",\"firstName\":\"First 3\",\"lastName\":\"Doe 3\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report4@no.where\",\"firstName\":\"First 4\",\"lastName\":\"Doe 4\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report5@no.where\",\"firstName\":\"First 5\",\"lastName\":\"Doe 5\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report6@no.where\",\"firstName\":\"First 6\",\"lastName\":\"Doe 6\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report7@no.where\",\"firstName\":\"First 7\",\"lastName\":\"Doe 7\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report8@no.where\",\"firstName\":\"First 8\",\"lastName\":\"Doe 8\",\"manager\":null,\"reports\":null},{\"address\":null,\"email\":\"report9@no.where\",\"firstName\":\"First 9\",\"lastName\":\"Doe 9\",\"manager\":null,\"reports\":null}]}");
    }

    @Test
    public void testInclusion() throws JsonProcessingException {
        String[] filters = new String[] { "*", "address.*", "manager.firstName" };
        assertAntFilter(filters,
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
        assertAntFilter(User.buildRecursive(), filters,
                "{\"address\":{\"streetName\":\"At my place\",\"streetNumber\":\"1\"},\"email\":\"somewhere@no.where\",\"firstName\":\"Martin\",\"lastName\":\"Frey\",\"reports\":null}");
    }

    @Test
    public void testReports() throws JsonProcessingException {
        String[] filters = new String[] { "reports", "reports.firstName" };
        assertAntFilter(User.buildMySelf(), filters,
                "{\"reports\":[{\"firstName\":\"First 0\"},{\"firstName\":\"First 1\"},{\"firstName\":\"First 2\"},{\"firstName\":\"First 3\"},{\"firstName\":\"First 4\"},{\"firstName\":\"First 5\"},{\"firstName\":\"First 6\"},{\"firstName\":\"First 7\"},{\"firstName\":\"First 8\"},{\"firstName\":\"First 9\"}]}");
    }

}
