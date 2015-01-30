package ch.mfrey.jackson.antpathfilter.test;

import org.junit.Assert;
import org.junit.Test;

import ch.mfrey.jackson.antpathfilter.AntPathFilterMixin;
import ch.mfrey.jackson.antpathfilter.AntPathPropertyFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

public class AntPathFilterTest {

    private void assertAntFilter(final Object testObj, final String[] filters, final String outcome)
            throws JsonProcessingException {
        ObjectMapper objectMapper = buildObjectMapper(filters);
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
        return myself;
    }

    private ObjectMapper buildObjectMapper(final String[] filters) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.addMixIn(Object.class, AntPathFilterMixin.class);

        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("antPathFilter",
                new AntPathPropertyFilter(filters));
        objectMapper.setFilters(filterProvider);
        return objectMapper;
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
                "{\"address\":{\"streetName\":\"At my place\",\"streetNumber\":\"1\"},\"email\":\"somewhere@no.where\",\"firstName\":\"Martin\",\"lastName\":\"Frey\"}");
    }

    @Test
    public void testExclusion2() throws JsonProcessingException {
        String[] filters = new String[] { "**", "-manager", "-**.streetNumber" };
        assertAntFilter(
                filters,
                "{\"address\":{\"streetName\":\"At my place\"},\"email\":\"somewhere@no.where\",\"firstName\":\"Martin\",\"lastName\":\"Frey\"}");
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
                "{\"address\":{\"streetName\":\"At my place\",\"streetNumber\":\"1\"},\"email\":\"somewhere@no.where\",\"firstName\":\"Martin\",\"lastName\":\"Frey\",\"manager\":{\"address\":null,\"email\":\"john.doe@no.where\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"manager\":null}}");
    }

    @Test
    public void testInclusion() throws JsonProcessingException {
        String[] filters = new String[] { "*", "address.*", "manager.firstName" };
        assertAntFilter(
                filters,
                "{\"address\":{\"streetName\":\"At my place\",\"streetNumber\":\"1\"},\"email\":\"somewhere@no.where\",\"firstName\":\"Martin\",\"lastName\":\"Frey\",\"manager\":{\"firstName\":\"John\"}}");
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
                "{\"address\":{\"streetName\":\"At my place\",\"streetNumber\":\"1\"},\"email\":\"somewhere@no.where\",\"firstName\":\"Martin\",\"lastName\":\"Frey\"}");
    }
}
