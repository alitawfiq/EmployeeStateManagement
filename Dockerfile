FROM adoptopenjdk/openjdk11:jre-11.0.6_10-alpine
EXPOSE 8080
ADD target/WorkMotionTask.jar WorkMotionTask.jar
ENTRYPOINT ["java","-jar","WorkMotionTask.jar"]