
Jackson AntPath Property Filter
===============================

A Jackson Filter matching the path of the current value to serialize against the AntPathMatcher. The inclusion / exclusion works similar to the `ant` file `include / exclude` functionality. Ant / Maven users should mostly be aware of how this works. I plan to add more examples and test cases later.


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
    String[] filter = new String[] {"*", "*.*", "-not.that.path"};

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
   "-property", "-**.someExpensiveMethod";
```

= Examples (from the Unit Tests)

    Filter: manager,manager.firstName,manager.lastName,
    Result: {"manager":{"firstName":"John","lastName":"Doe"}}

    Filter: **,-manager,
    Result: {"address":{"streetName":"At my place","streetNumber":"1"},"email":"somewhere@no.where","firstName":"Martin","lastName":"Frey"}

    Filter: *,address.*,manager.firstName,
    Result: {"address":{"streetName":"At my place","streetNumber":"1"},"email":"somewhere@no.where","firstName":"Martin","lastName":"Frey","manager":{"firstName":"John"}}

    Filter: **,-manager,-**.streetNumber,
    Result: {"address":{"streetName":"At my place"},"email":"somewhere@no.where","firstName":"Martin","lastName":"Frey"}

    Filter: firstName,
    Result: {"firstName":"Martin"}

    Filter: **,-manager,
    Result: {"address":{"streetName":"At my place","streetNumber":"1"},"email":"somewhere@no.where","firstName":"Martin","lastName":"Frey"}

