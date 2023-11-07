FROM openjdk:11
##okay
COPY target/DepartmentalStore-0.1.0.jar DepartmentalStore-0.1.0.jar
##fine
ENTRYPOINT ["java", "-jar", "/DepartmentalStore-0.1.0.jar"]
