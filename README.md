SpringBoot HelloWorld for App Engine Standard (Java 8)
============================

This sample demonstrates how to deploy a Spring Boot application on Google App Engine.

See the [Google App Engine standard environment documentation][ae-docs] for more
detailed instructions.

[ae-docs]: https://cloud.google.com/appengine/docs/java/

* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Maven](https://maven.apache.org/download.cgi) (at least 3.5)
* [Google Cloud SDK](https://cloud.google.com/sdk/) (aka gcloud command line tool)

## Setup

* Download and initialize the [Cloud SDK](https://cloud.google.com/sdk/)

    `gcloud init`

* Create an App Engine app within the current Google Cloud Project

    `gcloud app create`

## Maven
### Running locally

`mvn appengine:run`

To use vist: http://localhost:8080/

### Deploying

`mvn appengine:deploy`

To use vist:  https://YOUR-PROJECT-ID.appspot.com

## Testing

`mvn verify`

As you add / modify the source code (`src/main/java/...`) it's very useful to add [unit testing](https://cloud.google.com/appengine/docs/java/tools/localunittesting)
to (`src/main/test/...`).  The following resources are quite useful:

* [Junit4](http://junit.org/junit4/)
* [Mockito](http://mockito.org/)
* [Truth](http://google.github.io/truth/)


For further information, consult the
[Java App Engine](https://developers.google.com/appengine/docs/java/overview) documentation.

## Steps to convert a Spring Boot application for App Engine Standard
### Use the WAR packaging
You must use WAR packaging to deploy into Google App Engine Standard.

If you generate a Spring Boot project from [start.spring.io](http://start.spring.io/),
make sure you *switch to the full version* view of the initializer site, and select *WAR*
packaging.

If you have an existing `JAR` packaging project, you can convert it into a `WAR` project by:
1. In `pom.xml`, change `<packaging>jar</packaging>` to `<packaging>war</packaging>`
1. Create a new `SpringBootServletInitializer` implementation:

```java
public class ServletInitializer extends SpringBootServletInitializer {
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
  return application.sources(YourApplication.class);
  }
}
```

### Remove Tomcat Starter
Google App Engine Standard deploys your `WAR` into a Jetty server. Spring Boot's starter
includes Tomcat by default. This will introduce conflicts. Exclude Tomcat dependencies:
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
  <exclusions>
    <exclusion>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-tomcat</artifactId>
    </exclusion>
  </exclusions>
</dependency>
```

Do not include the Jetty dependencies. But you must include Servlet API dependency:
```xml
<dependency>
  <groupId>javax.servlet</groupId>
  <artifactId>javax.servlet-api</artifactId>
  <version>3.1.0</version>
  <scope>provided</scope>
</dependency>
```

### Add App Engine Standard Plugin
In the `pom.xml`, add the App Engine Standard plugin:
```xml
<plugin>
  <groupId>com.google.cloud.tools</groupId>
  <artifactId>appengine-maven-plugin</artifactId>
  <version>1.3.1</version>
</plugin>
```

This plugin is used to run local development server as well as deploying the application
into Google App Engine.

### Add App Engine Configuration
Add a `src/main/webapp/WEB-INF/appengine-web.xml`:
```xml
<appengine-web-app xmlns="http://appengine.google.com/ns/1.0">
  <version>1</version>
  <threadsafe>true</threadsafe>
  <runtime>java8</runtime>
</appengine-web-app>
```

This configure is required for applications running in Google App Engine.

### Exclude JUL to SLF4J Bridge
Spring Boot's default logging bridge conflicts with Jetty's logging system.
To be able to capture the Spring Boot startup logs, you need to exclude
`org.slf4j:jul-to-slf4j` dependency.  The easiest way to do this is to
set the dependency scope to `provided`, so that it won't be included in
the `WAR` file:

```xml
<!-- Exclude any jul-to-slf4j -->
<dependency>
  <groupId>org.slf4j</groupId>
  <artifactId>jul-to-slf4j</artifactId>
  <scope>provided</scope>
</dependency>
```

### Note from gcloud_gae.org
<pre>
* ========== gcloud commandline installation and initialization
* Google Cloud SDK installed location
/Applications/google-cloud-sdk

* extra components
┌─────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                                  Components                                                 │
├───────────────┬──────────────────────────────────────────────────────┬──────────────────────────┬───────────┤
│     Status    │                         Name                         │            ID            │    Size   │
├───────────────┼──────────────────────────────────────────────────────┼──────────────────────────┼───────────┤
│ Not Installed │ App Engine Go Extensions                             │ app-engine-go            │  97.7 MiB │
│ Not Installed │ Cloud Bigtable Command Line Tool                     │ cbt                      │   4.0 MiB │
│ Not Installed │ Cloud Bigtable Emulator                              │ bigtable                 │   3.5 MiB │
│ Not Installed │ Cloud Datalab Command Line Tool                      │ datalab                  │   < 1 MiB │
│ Not Installed │ Cloud Datastore Emulator                             │ cloud-datastore-emulator │  15.4 MiB │
│ Not Installed │ Cloud Datastore Emulator (Legacy)                    │ gcd-emulator             │  38.1 MiB │
│ Not Installed │ Cloud Pub/Sub Emulator                               │ pubsub-emulator          │  33.2 MiB │
│ Not Installed │ Emulator Reverse Proxy                               │ emulator-reverse-proxy   │  14.5 MiB │
│ Not Installed │ Google Container Local Builder                       │ container-builder-local  │   3.7 MiB │
│ Not Installed │ Google Container Registry's Docker credential helper │ docker-credential-gcr    │   2.2 MiB │
│ Not Installed │ gcloud Alpha Commands                                │ alpha                    │   < 1 MiB │
│ Not Installed │ gcloud Beta Commands                                 │ beta                     │   < 1 MiB │
│ Not Installed │ gcloud app Java Extensions                           │ app-engine-java          │ 116.9 MiB │
│ Not Installed │ gcloud app PHP Extensions                            │ app-engine-php           │  21.9 MiB │
│ Not Installed │ gcloud app Python Extensions                         │ app-engine-python        │   6.2 MiB │
│ Not Installed │ kubectl                                              │ kubectl                  │  15.9 MiB │
│ Installed     │ BigQuery Command Line Tool                           │ bq                       │   < 1 MiB │
│ Installed     │ Cloud SDK Core Libraries                             │ core                     │   7.2 MiB │
│ Installed     │ Cloud Storage Command Line Tool                      │ gsutil                   │   3.0 MiB │
└───────────────┴──────────────────────────────────────────────────────┴──────────────────────────┴───────────┘
To install or remove components at your current SDK version [175.0.0], run:
  $ gcloud components install COMPONENT_ID
  $ gcloud components remove COMPONENT_ID

To update your SDK installation to the latest version [175.0.0], run:
  $ gcloud components update

* gcloud init
... select or create a project

Not setting default zone/region (this feature makes it easier to use
[gcloud compute] by setting an appropriate default value for the
--zone and --region flag).
See https://cloud.google.com/compute/docs/gcloud-compute section on how to set
default compute region and zone manually. If you would like [gcloud init] to be
able to do this for you the next time you run it, make sure the
Compute Engine API is enabled for your project on the
https://console.developers.google.com/apis page.

Created a default .boto configuration file at [/Users/jirapat/.boto]. See this file and
[https://cloud.google.com/storage/docs/gsutil/commands/config] for more
information about configuring Google Cloud Storage.
Your Google Cloud SDK is configured and ready to use!

** Commands that require authentication will use jirapat.tanghongs@gmail.com by default
** Commands will reference project `backend-tour-of-heroes` by default
Run `gcloud help config` to learn how to change individual settings

This gcloud configuration is called [default]. You can create additional configurations if you work with multiple accounts and/or projects.
Run `gcloud topic configurations` to learn more.

Some things to try next:

** Run `gcloud --help` to see the Cloud Platform services you can interact with. And run `gcloud help COMMAND` to get help on any gcloud command.
** Run `gcloud topic -h` to learn about advanced features of the SDK like arg files and output formatting



* ========== Google App Engine Sample
* github for sample projects
** base
https://github.com/GoogleCloudPlatform
** java sample
https://github.com/GoogleCloudPlatform/getting-started-java
** gae standard java 8
https://github.com/GoogleCloudPlatform/getting-started-java/tree/master/appengine-standard-java8
** springboot-appengine-standard
https://github.com/GoogleCloudPlatform/getting-started-java/tree/master/appengine-standard-java8/springboot-appengine-standard

* Using Apache Maven and the App Engine Plugin (Cloud SDK-based)
https://cloud.google.com/appengine/docs/standard/java/tools/using-maven
https://cloud.google.com/appengine/docs/standard/java/tools/maven-reference

* clone whole getting-started-java
# on home directory
mkdir getting-started-java
git clone https://github.com/GoogleCloudPlatform/getting-started-java.git getting-started-java/

* test springboot-appengine-standard on server
cd appengine-standard-java8/springboot-appengine-standard

# optional describe appengine goals, build project
mvn help:describe -Dplugin=appengine
mvn clean package

# run locally
mvn appengine:run

# test locally
# if on cloud console, use Web Preview button to launch preview page
# Ctrl-c to stop test server

# list and set default project to deploy to
gcloud projects list
gcloud config set project backend-tour-of-heroes

# deploy
mvn appengine:deploy

</pre>


