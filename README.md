Replicon4j
==========
[![Build Status](https://travis-ci.org/tmoreira2020/replicon4j.svg?branch=master)](https://travis-ci.org/tmoreira2020/replicon4j)
[![Coverage Status](https://coveralls.io/repos/tmoreira2020/replicon4j/badge.png?branch=develop)](https://coveralls.io/r/tmoreira2020/replicon4j?branch=develop)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/br.com.thiagomoreira.replicon/replicon4j/badge.svg)](https://maven-badges.herokuapp.com/maven-central/br.com.thiagomoreira.replicon/replicon4j)

Replicon4j is a Java/REST client to access the Replicon API available at [Replicon's Developer Getting Started](http://www.replicon.com/getting-started). It was developed leveraging the `org.springframework.web.client.RestTemplate` class of [Spring Framework](http://projects.spring.io/spring-framework/).

### License

Replicon4j is licensed under [Apache 2](http://www.apache.org/licenses/LICENSE-2.0) license.

### Getting Started

It's really simple to use the client, first of all you need credentials. In Replicon system the credentials are composed by three fields:

1. company: your company. Since Replicon is a SaaS system you need to provide which company you work for. This is not something new for customers of Replicon.
2. username: your username
3. password: your password

The second step is create a object of `br.com.thiagomoreira.replicon.Replicon` class and  start to invoke its methods. That's it!


```java
import br.com.thiagomoreira.replicon.Replicon;
import br.com.thiagomoreira.replicon.model.User;

public static void main(String[] args) {
    String company = "...";
    String username = "...";
    String password = "...";

    Replicon replicon = new Replicon(company, username, password);

    User[] users = replicon.getUsers();
    
	for (User user : users) {
	    Resource resource = replicon.getResource(user.getUri());
        ...
    }
}
```

### Which methods are implemented?

Yep, it is a small list. Can you help me? Look here: [Contributing](#contributing).
* getProject(String projectUri)
* getProjectAllocations(Date startDate,	Date endDate, String resourceUri)
* getResource(String resourceUri)
* getTask(String taskUri)
* getTaskAllocations(String projectUri, String resourceUri)
* getTimeOff(String userUri, Date startDate, Date endDate)
* getUserByLoginName(String loginName)
* getUsers()
* getUsersBySupervisor(String userUri)

### Maven/Gradle

Replicon4j is available on Maven central, the artifact is as follows:

Maven:

```xml
<dependency>
    <groupId>br.com.thiagomoreira.replicon</groupId>
    <artifactId>replicon4j</artifactId>
    <version>1.0.0</version>
</dependency>
```
Gradle:

```groovy
dependencies {
    compile(group: "br.com.thiagomoreira.replicon", name: "replicon4j", version: "1.0.0");
}
```
### Support
Replicon4j tracks [bugs and feature requests](https://github.com/tmoreira2020/replicon4j/issues) with Github's issue system. Feel free to open your [new ticket](https://github.com/tmoreira2020/replicon4j/issues/new)!

### Contributing

Replicon4j is a project based on Maven to improve it you just need to fork the repository, clone it and from the command line invoke

```shell
mvn package
```
After complete your work you can send a pull request to incorporate the modifications.

Enjoy!