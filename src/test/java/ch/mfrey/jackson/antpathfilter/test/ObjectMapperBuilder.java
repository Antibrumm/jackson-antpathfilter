package ch.mfrey.jackson.antpathfilter.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import ch.mfrey.jackson.antpathfilter.AntPathFilterMixin;
import ch.mfrey.jackson.antpathfilter.AntPathPropertyFilter;

public class ObjectMapperBuilder {
    public static ObjectMapper buildObjectMapper(final String[] filters) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.addMixIn(Object.class, AntPathFilterMixin.class);

        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("antPathFilter",
                new AntPathPropertyFilter(filters));
        objectMapper.setFilters(filterProvider);
        return objectMapper;
    }

}
