package ch.mfrey.jackson.antpathfilter;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

/**
 * This is just a help for a start.
 */
public class Jackson2Helper {

    private final ObjectMapper objectMapper;

    public Jackson2Helper() {
        super();
        this.objectMapper = new ObjectMapper();
        this.getObjectMapper().addMixIn(Object.class, AntPathFilterMixin.class);
    }

    /**
     * Allows to explicitly override the default ObjectMapper with an own
     * instance to be able to add more functionality. It is important to know
     * that the given objectMapper needs to contain the
     * {@link AntPathFilterMixin}!
     * 
     * @param objectMapper
     */
    public Jackson2Helper(ObjectMapper objectMapper) {
        super();
        this.objectMapper = objectMapper;
    }

    /**
     * Returns a prepared copy of the ObjectMapper with the filters set to the
     * current arguments.
     * 
     * @param filters
     * @return The prepared {@link ObjectMapper} ready for filtering
     */
    public ObjectMapper buildObjectMapper(final String... filters) {
        ObjectMapper copyForFilter = getObjectMapper().copy();
        copyForFilter.setFilters(buildFilterProvider(filters));
        return copyForFilter;
    }

    public SimpleFilterProvider buildFilterProvider(final String... filters) {
        return new SimpleFilterProvider().addFilter("antPathFilter", new AntPathPropertyFilter(filters));
    }

    /**
     * Convenience method to simply write an object to a json representation
     * using the given filters.
     * 
     * @param value
     *            Any object that can be serialized to json
     * @param filters
     *            The desired filters to be used
     * @return The json representation
     */
    public String writeFiltered(final Object value, final String... filters) {
        try {
            return buildObjectMapper(filters).writeValueAsString(value);
        } catch (IOException ioe) {
            throw new RuntimeException("Could not write object filtered.", ioe);
        }
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}