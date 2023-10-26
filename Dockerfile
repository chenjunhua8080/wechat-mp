FROM amazoncorretto:11-alpine3.18-jdk

EXPOSE 80

COPY target/*.jar ./wechat-mp.jar

CMD java -Dserver.port=80 -Xms512m -Xmx512m -server -jar ./wechat-mp.jar --spring.profiles.active=dev
