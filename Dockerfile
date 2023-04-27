FROM amazoncorretto:17.0.6-alpine as builder

WORKDIR /WhatsATTManager

COPY ./mvnw .
COPY ./pom.xml .
COPY ./.mvn ./.mvn

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

COPY ./src ./src

RUN ./mvnw clean package -DskipTests

FROM amazoncorretto:17.0.6-alpine

WORKDIR /WhatsATTManager

COPY --from=builder /WhatsATTManager/target/whatsapp-messages.jar .

EXPOSE 7051

ENTRYPOINT [ "java","-jar","whatsapp-messages.jar" ]
