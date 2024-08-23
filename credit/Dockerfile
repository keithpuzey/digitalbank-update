FROM tomcat:10.1.14-jdk17-temurin
EXPOSE 8080
EXPOSE 8443
COPY /target/credit.war /usr/local/tomcat/webapps/credit.war
COPY /target/classes/application.properties /usr/local/tomcat/conf/digitalcredit.properties
COPY /target/classes/keystore/digisic.p12 /usr/local/tomcat/conf/digisic.p12
COPY /target/classes/keystore/digisic.jks /usr/local/tomcat/conf/digisic.jks
COPY /target/classes/server.xml /usr/local/tomcat/conf/server.xml
HEALTHCHECK CMD curl -fail http://localhost:8080/credit/api/v1/health || exit 1