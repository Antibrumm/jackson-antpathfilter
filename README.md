
Jackson AntPath Property Filter
===============================

A Jackson Filter matching the path of the current value to serialize against the AntPathMatcher. The inclusion / exclusion works similar to the `ant` file `include / exclude` functionality. Ant / Maven users should mostly be aware of how this works.

[![Build Status](https://travis-ci.org/Antibrumm/jackson-antpathfilter.png)](https://travis-ci.org/Antibrumm/jackson-antpathfilter)

Requirements
------------

 - Java 5
 - Jackson 2.5.0+ (2.5.0.RELEASE and its dependencies included)


Installation
------------

### For Maven and Maven-compatible dependency managers
Add a dependency to your project with the following co-ordinates:

 - GroupId: `ch.mfrey.jackson`
 - ArtifactId: `jackson-antpathfilter`
 - Version: `${jackson-antpathfilter.version}`

```xml
<dependency>
	<groupId>ch.mfrey.jackson</groupId>
	<artifactId>jackson-antpathfilter</artifactId>
	<version>${jackson-antpathfilter.version}</version>
</dependency>
```

Usage
-----

```java
String[] filter = new String[] {"*", "*.*", "!not.that.path"};

ObjectMapper objectMapper = new ObjectMapper();
objectMapper.addMixIn(Object.class, AntPathFilterMixin.class);

FilterProvider filterProvider = new SimpleFilterProvider().addFilter("antPathFilter", new AntPathPropertyFilter(filter));
objectMapper.setFilters(filterProvider);

objectMapper.writeValueAsString(someObject);
```

= Inclusion:

```  
"*", "**", "*.*", "someproperty.someNesterProperty.*",
```

= Exclusion:

```
"-property", "!**.someExpensiveMethod";
```

Spring Integration
------------------

= Before Spring 4.2.2

For those using this together with Spring it is easy to use the Jackson2Helper class as a Component to configure the objectMapper only once and avoid boilerplate code in each `Controller`.
```xml
<!-- Needed so that Controllers can return a JSON ResponseBody directly as a String result (produced by Jackson2Helper) -->
<bean class = "org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter">
    <property name="messageConverters">
        <array>
            <bean class="org.springframework.http.converter.StringHttpMessageConverter">
                <property name="supportedMediaTypes" value="application/json; charset=UTF-8" />
            </bean>
        </array>
    </property>
</bean>
```

```java
@Configuration
public class Jackson2HelperConfig {

    @Bean
    public Jackson2Helper jackson2Helper() {
    	return new Jackson2Helper();
    }
}
```

```java
@Controller
@RequestMapping(value = "/someObject")
public class SomeController {
    @Autowired
    private Jackson2Helper jackson2Helper;

    @RequestMapping
    @ResponseBody
    public String getSomeObject() {
        return jackson2Helper.writeFiltered(someObject, "*", "*.*", "!not.that.path");
    }
}
```

= With Spring 4.2.2+

With the latest addon in Spring 4.2.2, the class `MappingJacksonValue` contains now a function `setFilters`, it is now possible to integrate this filter in a cleaner way. No specific StringConverter is needed anymore, but we need some more classes for now:
```java
@Configuration
public class WebConfig extends DelegatingWebMvcConfiguration {
    @Override
    public void configureMessageConverters(final List<HttpMessageConverter<?>> messageConverters) {
        // Add a MappingJackson2HttpMessageConverter so that
        // objectMapper.writeFiltered
        // is using the objectMapper configured with the needed Mixin
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder
            .json()
            .mixIn(Object.class, AntPathFilterMixin.class)
            .build();
        messageConverters.add(new MappingJackson2HttpMessageConverter(objectMapper));

        addDefaultHttpMessageConverters(messageConverters);
    }
}
```

```java
/**
 * Just a helper class to simplify usage
 */
public class AntPathFilterMappingJacksonValue extends MappingJacksonValue {

    public AntPathFilterMappingJacksonValue(final Object value) {
        super(value);
        setFilters(new SimpleFilterProvider().addFilter("antPathFilter", new AntPathPropertyFilter("**")));
    }

    public AntPathFilterMappingJacksonValue(final Object value, final String... filters) {
        super(value);
        setFilters(new SimpleFilterProvider().addFilter("antPathFilter", new AntPathPropertyFilter(filters)));
    }

}
```

```java
@Controller
@RequestMapping(value = "/someObject")
public class SomeController {
    
    @RequestMapping
    @ResponseBody
    public MappingJacksonValue getSomeObject() {
        return new AntPathFilterMappingJacksonValue(someObject, "*", "*.*", "!not.that.path");
    }
}
```

Examples (from the Unit Tests)
------------------------------

Data
```
{
  "address": {
    "streetName":"At my place",
    "streetNumber":"1"
  },
  "email":"somewhere@no.where",
  "firstName":"Martin",
  "lastName":"Frey",
  "manager":{
    "address":null,
    "email":"john.doe@no.where",
    "firstName":"John",
    "lastName":"Doe",
    "manager":null
  }
}
    
```

```
Filter: **,
Result: {"address":{"streetName":"At my place","streetNumber":"1"},"email":"somewhere@no.where","firstName":"Martin","lastName":"Frey","manager":{"address":null,"email":"john.doe@no.where","firstName":"John","lastName":"Doe","manager":null}}
```
    
```
Filter: firstName,
Result: {"firstName":"Martin"}
```

```
Filter: **,!manager,
Result: {"address":{"streetName":"At my place","streetNumber":"1"},"email":"somewhere@no.where","firstName":"Martin","lastName":"Frey"}
```

```
Filter: manager,manager.firstName,manager.lastName,
Result: {"manager":{"firstName":"John","lastName":"Doe"}}
```

```
Filter: *,address.*,manager.firstName,
Result: {"address":{"streetName":"At my place","streetNumber":"1"},"email":"somewhere@no.where","firstName":"Martin","lastName":"Frey","manager":{"firstName":"John"}}
```

```
Filter: **,-manager,!**.streetNumber,
Result: {"address":{"streetName":"At my place"},"email":"somewhere@no.where","firstName":"Martin","lastName":"Frey"}
```

```
Filter: "reports", "reports.firstName"
Result: {"reports":[{"firstName":"First 0"},{"firstName":"First 1"},{"firstName":"First 2"},{"firstName":"First 3"},{"firstName":"First 4"},{"firstName":"First 5"},{"firstName":"First 6"},{"firstName":"First 7"},{"firstName":"First 8"},{"firstName":"First 9"}]}
```
