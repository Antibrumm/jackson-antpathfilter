package ch.mfrey.jackson.antpathfilter.test;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.mfrey.jackson.antpathfilter.Jackson2Helper;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.io.InputStream;

public class AntPathFilterTest {

    private final Jackson2Helper jackson2Helper = new Jackson2Helper();
    private final ObjectMapper defaultJackson = new ObjectMapper();

    @Test
    public void canFilterOutAKey() throws JsonProcessingException, IOException {
        final String[] filters = new String[]{"**", "!manager"};
        final InputStream in = this.getClass().getResourceAsStream("AntPathFilterTest_canFilterOutAKey.json");
        final JsonNode expected = defaultJackson.readTree(in);
        final ObjectMapper filteringMapper = jackson2Helper.buildObjectMapper(filters);
        final String gotString = filteringMapper.writeValueAsString(User.buildMySelf());
        final JsonNode got = defaultJackson.readTree(gotString);
        Assert.assertEquals(expected, got);
    }

    @Test
    public void canFilterOutAKeyWithOldPattern() throws JsonProcessingException, IOException {
        final String[] filters = new String[]{"**", "-manager"};
        final InputStream in = this.getClass().getResourceAsStream("AntPathFilterTest_canFilterOutAKey.json");
        final JsonNode expected = defaultJackson.readTree(in);
        final ObjectMapper filteringMapper = jackson2Helper.buildObjectMapper(filters);
        final String gotString = filteringMapper.writeValueAsString(User.buildMySelf());
        final JsonNode got = defaultJackson.readTree(gotString);
        Assert.assertEquals(expected, got);
    }

    @Test
    public void canExcludeTwoKeys() throws JsonProcessingException, IOException {
        final String[] filters = new String[]{"**", "!manager", "!**.streetNumber"};
        final InputStream in = this.getClass().getResourceAsStream("AntPathFilterTest_canExcludeTwoKeys.json");
        final JsonNode expected = defaultJackson.readTree(in);
        final ObjectMapper filteringMapper = jackson2Helper.buildObjectMapper(filters);
        final String gotString = filteringMapper.writeValueAsString(User.buildMySelf());
        final JsonNode got = defaultJackson.readTree(gotString);
        Assert.assertEquals(expected, got);
    }

    @Test
    public void canExtractAKey() throws JsonProcessingException, IOException {
        final String[] filters = new String[]{"firstName"};
        final InputStream in = this.getClass().getResourceAsStream("AntPathFilterTest_canExtractAKey.json");
        final JsonNode expected = defaultJackson.readTree(in);
        final ObjectMapper filteringMapper = jackson2Helper.buildObjectMapper(filters);
        final String gotString = filteringMapper.writeValueAsString(User.buildMySelf());
        final JsonNode got = defaultJackson.readTree(gotString);
        Assert.assertEquals(expected, got);
    }

    @Test
    public void noFilterReturnAllObject() throws JsonProcessingException, IOException {
        final String[] filters = new String[]{"**"};
        final InputStream in = this.getClass().getResourceAsStream("AntPathFilterTest_noFilterReturnAllObject.json");
        final JsonNode expected = defaultJackson.readTree(in);
        final ObjectMapper filteringMapper = jackson2Helper.buildObjectMapper(filters);
        final String gotString = filteringMapper.writeValueAsString(User.buildMySelf());
        final JsonNode got = defaultJackson.readTree(gotString);
        Assert.assertEquals(expected, got);
    }

    @Test
    public void canIncludeOnlySomePath() throws JsonProcessingException, IOException {
        final String[] filters = new String[]{"*", "address.*", "manager.firstName"};
        final InputStream in = this.getClass().getResourceAsStream("AntPathFilterTest_canIncludeOnlySomePath.json");
        final JsonNode expected = defaultJackson.readTree(in);
        final ObjectMapper filteringMapper = jackson2Helper.buildObjectMapper(filters);
        final String gotString = filteringMapper.writeValueAsString(User.buildMySelf());
        final JsonNode got = defaultJackson.readTree(gotString);
        Assert.assertEquals(expected, got);
    }

    @Test
    public void canExtractManagerName() throws JsonProcessingException, IOException {
        final String[] filters = new String[]{"manager", "manager.firstName", "manager.lastName"};
        final InputStream in = this.getClass().getResourceAsStream("AntPathFilterTest_canExtractManagerName.json");
        final JsonNode expected = defaultJackson.readTree(in);
        final ObjectMapper filteringMapper = jackson2Helper.buildObjectMapper(filters);
        final String gotString = filteringMapper.writeValueAsString(User.buildMySelf());
        final JsonNode got = defaultJackson.readTree(gotString);
        Assert.assertEquals(expected, got);
    }

    @Test
    public void testRecursive1Levels() throws JsonProcessingException, IOException {
        final String[] filters = new String[]{"**", "!manager"};
        final InputStream in = this.getClass().getResourceAsStream("AntPathFilterTest_testRecursive1Levels.json");
        final JsonNode expected = defaultJackson.readTree(in);
        final ObjectMapper filteringMapper = jackson2Helper.buildObjectMapper(filters);
        final String gotString = filteringMapper.writeValueAsString(User.buildRecursive());
        final JsonNode got = defaultJackson.readTree(gotString);
        Assert.assertEquals(expected, got);
    }

    @Test
    public void extractOnlyReportFirstName() throws JsonProcessingException, IOException {
        final String[] filters = new String[]{"reports", "reports.firstName"};
        final InputStream in = this.getClass().getResourceAsStream("AntPathFilterTest_extractOnlyReportFirstName.json");
        final JsonNode expected = defaultJackson.readTree(in);
        final ObjectMapper filteringMapper = jackson2Helper.buildObjectMapper(filters);
        final String gotString = filteringMapper.writeValueAsString(User.buildMySelf());
        final JsonNode got = defaultJackson.readTree(gotString);
        Assert.assertEquals(expected, got);
    }

    @Test
    public void extractOnlyReportFirstNameAlternativeFilter() throws JsonProcessingException, IOException {
        final String[] filters = new String[]{"reports.firstName"};
        final InputStream in = this.getClass().getResourceAsStream("AntPathFilterTest_extractOnlyReportFirstName.json");
        final JsonNode expected = defaultJackson.readTree(in);
        final ObjectMapper filteringMapper = jackson2Helper.buildObjectMapper(filters);
        final String gotString = filteringMapper.writeValueAsString(User.buildMySelf());
        final JsonNode got = defaultJackson.readTree(gotString);
        Assert.assertEquals(expected, got);
    }

    @Test
    public void extractAllFirstLevelAndAPropertyFromEachSubtree() throws JsonProcessingException, IOException {
        final String[] filters = new String[]{"*.firstName"};
        final InputStream in = this.getClass().getResourceAsStream("AntPathFilterTest_extractAllFirstLevelAndAPropertyFromEachSubtree.json");
        final JsonNode expected = defaultJackson.readTree(in);
        final ObjectMapper filteringMapper = jackson2Helper.buildObjectMapper(filters);
        final String gotString = filteringMapper.writeValueAsString(User.buildMySelf());
        final JsonNode got = defaultJackson.readTree(gotString);
        Assert.assertEquals(expected, got);
    }

}
