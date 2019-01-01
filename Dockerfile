FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/homester-0.0.1-SNAPSHOT-standalone.jar /homester/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/homester/app.jar"]
