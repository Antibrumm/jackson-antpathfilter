package ch.mfrey.jackson.antpathfilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

/**
 * A takeout of Springs StringUtils functionality just to match the need of AntPathPropertyFilter.
 * 
 * @author Martin Frey
 * 
 *         Miscellaneous {@link String} utility methods.
 * 
 *         <p>
 *         Mainly for internal use within the framework; consider <a
 *         href="http://jakarta.apache.org/commons/lang/">Jakarta's Commons Lang</a> for a more comprehensive suite of
 *         String utilities.
 * 
 *         <p>
 *         This class delivers some simple functionality that should really be provided by the core Java {@code String}
 *         and {@link StringBuilder} classes, such as the ability to replace all occurrences of a given
 *         substring in a target string. It also provides easy-to-use methods to convert between delimited strings, such
 *         as CSV strings, and collections and arrays.
 * 
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Keith Donald
 * @author Rob Harrop
 * @author Rick Evans
 * @author Arjen Poutsma
 * @since 16 April 2001
 */
public class StringUtils {

    public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens,
        boolean ignoreEmptyTokens) {

        if (str == null) {
            return null;
        }
        StringTokenizer st = new StringTokenizer(str, delimiters);
        List<String> tokens = new ArrayList<String>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if (!ignoreEmptyTokens || token.length() > 0) {
                tokens.add(token);
            }
        }
        return toStringArray(tokens);
    }

    public static boolean hasLength(CharSequence str) {
        return (str != null && str.length() > 0);
    }

    public static boolean hasText(CharSequence str) {
        if (!hasLength(str)) {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static String[] toStringArray(Collection<String> collection) {
        if (collection == null) {
            return null;
        }
        return collection.toArray(new String[collection.size()]);
    }

}
