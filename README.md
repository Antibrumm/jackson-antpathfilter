
Thymeleaf Cache Dialect
========================

A dialect for Thymeleaf that allows you to do partial page caching.

Some parts of our webpage will not change often during the lifetime of the application or is dependent only on a usersession.
This dialect will cache the resulting output of the element it is declared on and will replace the element on a cache hit.


Requirements
------------

 - Java 5
 - Thymeleaf 2.1.0+ (2.1.0.RELEASE and its dependencies included)


Installation
------------

### For Maven and Maven-compatible dependency managers
Add a dependency to your project with the following co-ordinates:

 - GroupId: `ch.mfrey.thymeleaf.extras.cache`
 - ArtifactId: `thymeleaf-cache-dialect`
 - Version: `1.0.1`

```xml
<dependency>
	<groupId>ch.mfrey.thymeleaf.extras.cache</groupId>
	<artifactId>thymeleaf-cache-dialect</artifactId>
	<version>1.0.1</version>
</dependency>
```

Usage
-----

The Cache dialect supports three attributes:

 - cache:name="name" -> Cache by name
 - cache:ttl="60" -> Make cache live 60 sec
 - cache:evict="name" -> Evict cache by name
 
All attribute values can be an expression.



Add the Cache dialect to your existing Thymeleaf template engine, eg:

```java
ServletContextTemplateResolver templateresolver = new ServletContextTemplateResolver();
templateresolver.setTemplateMode("HTML5");

templateengine = new TemplateEngine();
templateengine.setTemplateResolver(templateresolver);
templateengine.addDialect(new CacheDialect());		// This line adds the dialect to Thymeleaf
```

Or, for those using Spring configuration files:

```xml
<bean id="templateResolver" class="org.thymeleaf.templateresolver.ServletContextTemplateResolver">
  <property name="templateMode" value="HTML5"/>
</bean>

<bean id="templateEngine" class="org.thymeleaf.spring3.SpringTemplateEngine">
  <property name="templateResolver" ref="templateResolver"/>

  <!-- These lines add the dialect to Thymeleaf -->
  <property name="additionalDialects">
    <set>
      <beans:bean class="ch.mfrey.thymeleaf.extras.cache.CacheDialect" />
    </set>
  </property>

</bean>
```

Use it
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
	xmlns:cache="http://www.thymeleaf.org/extras/cache">
<head></head>
<body>
	<!-- Simplest use by name. Cached indefinitely -->
	<ul cache:name="menu+${user_role}">
		<li th:text="${a.title}"></li>
		<li th:each="b : ${a.bs}" th:object="${b}">
			<span th:text="*{text}"></span>
			<ul>
				<li th:each="c : *{cs}" th:object="${c}">
					<span th:text="*{text}"></span>
				</li>
			</ul>
		</li>
	</ul>
	
	<!-- Cached for 60 sec -->
	<div cache:name="longCalcValue" cache:ttl="60">
		<span th:text="${a.title}"></span>
	</div>
	
	<!-- Evict a cache by name if not resolved to '' -->
	<div cache:evict="${mayResolveIntoACachedName}" cache:name="longCalcValue">
		<span th:text="${a.title}"></span>
	</div>
</body>
</html>
```