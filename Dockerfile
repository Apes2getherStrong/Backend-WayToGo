FROM eclipse-temurin:17.0.9_9-jre

ENV VERSION="0.0.1-SNAPSHOT"
ENV SERVER_PORT=8090


ENV spring_datasource_url=jdbc:postgresql://localhost:5432/waytogo
ENV spring_datasource_username=waytogo
ENV spring_datasource_password=waytogo
ENV spring_jpa_hibernate.ddl-auto=validate
ENV spring_jpa_database=postgresql
ENV spring_datasource_driverClassName=org.postgresql.Driver
ENV spring_jpa_properties_hibernate_dialect=org.hibernate.dialect.PostgreSQLDialect
ENV spring_flyway_enabled=true
ENV app_cors_allowed_origins=http://localhost:4200,http://waytogo-frontend:80,http://localhost,https://way2go-api.popi.pl/,http://way2go-api.popi.pl/
EXPOSE 8090

COPY target/WayToGo-${VERSION}.jar /opt/waytogo/waytogo.jar

CMD ["java", "-jar", "/opt/waytogo/waytogo.jar"]