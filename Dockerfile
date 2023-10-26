FROM amazoncorretto:11-alpine3.18-jdk

EXPOSE 80

COPY target/*.jar /app/common.jar

CMD java -Dserver.port=80 -Xms512m -Xmx512m -server -jar /app/common.jar --spring.profiles.active=dev
