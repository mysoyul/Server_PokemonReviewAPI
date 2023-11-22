FROM openjdk:17
VOLUME /tmp
COPY target/springboot-vuejs-backend.jar springboot-vuejs-backend.jar
ENTRYPOINT ["java","-jar","/springboot-vuejs-backend.jar","--spring.profiles.active=prod","--DB=mysql-svc"]