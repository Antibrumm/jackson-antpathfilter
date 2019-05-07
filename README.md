
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
"!property", "!**.someExpensiveMethod";
```

Spring Integration
------------------

= With Spring 4.2.2+

```java
public class AntPathMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {

    public AntPathMappingJackson2HttpMessageConverter(ObjectMapper originalObjectMapper) {
        super(originalObjectMapper.copy().addMixIn(Object.class, HibernateAwareAntPathFilterMixin.class));
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return AntPathFilterMappingJacksonValue.class.isAssignableFrom(clazz);
    }

    @JsonFilter("antPathFilter")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public static class HibernateAwareAntPathFilterMixin {
    }
}

public class AntPathFilterMappingJacksonValue<T> extends MappingJacksonValue {

    public AntPathFilterMappingJacksonValue(final T value, final String... filters) {
        super(value);
        setFilters(new SimpleFilterProvider().addFilter("antPathFilter", new AntPathPropertyFilter(filters)));
    }
}
```

```java
@Configuration
@EnableWebMvc
public class DispatcherConfiguration extends WebMvcConfigurerAdapter {

    @Bean
    public FactoryBean<ObjectMapper> jacksonObjectMapperFactory() {
    	// Normal Jackson configuration
    }
    
    @Bean
    public HttpMessageConverter antpathJacksonConverter() {
        return new AntPathMappingJackson2HttpMessageConverter(jacksonObjectMapperFactory().getObject());
    }

    @Bean
    public HttpMessageConverter jacksonConverter() {
        return new MappingJackson2HttpMessageConverter(jacksonObjectMapperFactory().getObject());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(...);
        // Register the Antpath Converter before the default Jackson Converter
        converters.add(antpathJacksonConverter());
        converters.add(jacksonConverter());
    }

}
```

```java
@Controller
@RequestMapping(value = "/someObject")
public class SomeController {
    
    @RequestMapping
    @ResponseBody
    public AntPathFilterMappingJacksonValue<SomeClass> getSomeObject() {
        return new AntPathFilterMappingJacksonValue<>(someObject, "*", "*.*", "!not.that.path");
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
