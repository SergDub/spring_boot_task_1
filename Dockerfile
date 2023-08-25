FROM openjdk:11-jdk-slim
EXPOSE 8081
ADD build/libs/sbt_1-0.0.1-SNAPSHOT.jar myapp.jar
ENTRYPOINT ["java","-jar","/myapp.jar"]
