
Jackson AntPath Property Filter
===============================

A Jackson Filter matching the path of the 

Some parts of our webpage will not change often during the lifetime of the application or is dependent only on a usersession.
This dialect will cache the resulting output of the element it is declared on and will replace the element on a cache hit.


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
    objectMappercopy.addMixIn(Object.class, AntPathFilterMixin.class);
    FilterProvider filterProvider = new SimpleFilterProvider().addFilter("antPathFilter", new AntPathPropertyFilter(filter));
    objectMapper.setFilters(createCustomFilter(properties));
    objectMapper.writeValueAsString(someObject);
```

Inclusion:

```  
   "*", "**", "*.*", "someproperty.anotherone",
```

Exclusion:

```
   "-property";
```
