package ch.mfrey.jackson.antpathfilter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

/**
 * Implementation that allows to set nested properties. The filter will use the
 * parents from the context to identify if a property has to be filtered.
 * 
 * Example: user -&gt; manager (user)
 * 
 * "id", "firstName", "lastName", "manager.id", "manager.fullName"
 * 
 * { "id" : "2", "firstName" : "Martin", "lastName" : "Frey", manager : { "id" :
 * "1", "fullName" : "System Administrator"}}
 * 
 * @author Martin Frey
 */
public class AntPathPropertyFilter extends SimpleBeanPropertyFilter {
    
    private static final Logger log = Logger.getLogger(AntPathPropertyFilter.class.toString());
    
    /** The matcher. */
    private static final AntPathMatcher MATCHER = new AntPathMatcher(".");

    /** The _properties to exclude. */
    protected final Set<String> _propertiesToExclude;

    /**
     * Set of property names to include.
     */
    protected final Set<String> _propertiesToInclude;

    /**
     * Cache of patterns to test, and match results
     */
    private final Map<String, Boolean> matchCache = new HashMap<String, Boolean>();

    /**
     * Instantiates a new ant path property filter.
     * 
     * @param properties
     *            the properties
     */
    public AntPathPropertyFilter(final String... properties) {
        super();
        _propertiesToInclude = new HashSet<String>(properties.length);
        _propertiesToExclude = new HashSet<String>(properties.length);
        for (int i = 0; i < properties.length; i++) {
            if (properties[i].startsWith("-")) {
                _propertiesToExclude.add(properties[i].substring(1));
                log.warning("Using '-' for exclusion is now deprecated. Please use '!'");
            } else if (properties[i].startsWith("!")) {
                _propertiesToExclude.add(properties[i].substring(1));
            } else {
                _propertiesToInclude.add(properties[i]);
            }
        }
    }

    /**
     * Gets the path to test.
     * 
     * @param writer
     *            the writer
     * @param jgen
     *            the jgen
     * @return the path to test
     */
    private String getPathToTest(final PropertyWriter writer, final JsonGenerator jgen) {
        StringBuilder nestedPath = new StringBuilder();
        nestedPath.append(writer.getName());
        JsonStreamContext sc = jgen.getOutputContext();
        if (sc != null) {
            sc = sc.getParent();
        }
        while (sc != null) {
            if (sc.getCurrentName() != null) {
                if (nestedPath.length() > 0) {
                    nestedPath.insert(0, ".");
                }
                nestedPath.insert(0, sc.getCurrentName());
            }
            sc = sc.getParent();
        }
        return nestedPath.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter#include(
     * com.fasterxml.jackson.databind.ser. BeanPropertyWriter)
     */
    @Override
    protected boolean include(final BeanPropertyWriter writer) {
        throw new UnsupportedOperationException("Cannot call include without JsonGenerator");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter#include(
     * com.fasterxml.jackson.databind.ser. PropertyWriter)
     */
    @Override
    protected boolean include(final PropertyWriter writer) {
        throw new UnsupportedOperationException("Cannot call include without JsonGenerator");
    }

    /**
     * Include.
     * 
     * @param writer
     *            the writer
     * @param jgen
     *            the jgen
     * @return true, if successful
     */
    protected boolean include(final PropertyWriter writer, final JsonGenerator jgen) {
        String pathToTest = getPathToTest(writer, jgen);
        // Check cache first
        if (matchCache.containsKey(pathToTest)) {
            return matchCache.get(pathToTest);
        }

        // Only Excludes.
        if (_propertiesToInclude.isEmpty()) {
            for (String pattern : _propertiesToExclude) {
                if (matchPath(pathToTest, pattern)) {
                    matchCache.put(pathToTest, false);
                    return false;
                }
            }
            matchCache.put(pathToTest, true);
            return true;
        }

        // Else do full check
        boolean include = false;
        // Check Includes first
        for (String pattern : _propertiesToInclude) {
            if (matchPath(pathToTest, pattern)) {
                include = true;
                break;
            }
        }

        // Might still be excluded
        if (include && !_propertiesToExclude.isEmpty()) {
            for (String pattern : _propertiesToExclude) {
                if (matchPath(pathToTest, pattern)) {
                    include = false;
                    break;
                }
            }
        }

        matchCache.put(pathToTest, include);
        return include;
    }

    /**
     * Only uses AntPathMatcher if the pattern contains wildcards, else use
     * simple equals
     * 
     * @param pathToTest
     * @param pattern
     * @return
     */
    private boolean matchPath(String pathToTest, String pattern) {
        if (pattern.contains("*")) {
            return MATCHER.match(pattern, pathToTest);
        } else {
            return pattern.equals(pathToTest);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter#
     * serializeAsField(java.lang.Object,
     * com.fasterxml.jackson.core.JsonGenerator,
     * com.fasterxml.jackson.databind.SerializerProvider,
     * com.fasterxml.jackson.databind.ser.PropertyWriter)
     */
    @Override
    public void serializeAsField(final Object pojo, final JsonGenerator jgen, final SerializerProvider provider,
            final PropertyWriter writer) throws Exception {

        if (include(writer, jgen)) {
            writer.serializeAsField(pojo, jgen, provider);
        } else if (!jgen.canOmitFields()) { // since 2.3
            writer.serializeAsOmittedField(pojo, jgen, provider);
        }
    }
}