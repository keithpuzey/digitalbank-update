FROM tomcat:10.1.14-jdk17-temurin
# Environment variables
ENV CATALINA_HOME /usr/local/tomcat
EXPOSE 8080
EXPOSE 8443
# Install OpenJDK 17 or any necessary packages
RUN apt-get update && apt-get install -y openjdk-17-jdk
# Update the PATH environment variable
ENV PATH="/usr/lib/jvm/java-17-openjdk-arm64/bin:${PATH}"
COPY /target/bank.war /usr/local/tomcat/webapps/bank.war
COPY /target/classes/application.properties /usr/local/tomcat/conf/digitalbank.properties
COPY /target/classes/keystore/digisic.jks /usr/local/tomcat/conf/digisic.jks
COPY /target/classes/keystore/digisic.p12 /usr/local/tomcat/conf/digisic.p12
COPY /target/classes/server.xml /usr/local/tomcat/conf/server.xml
RUN mkdir /usr/local/tomcat/webapps/ROOT
HEALTHCHECK CMD curl -fail http://localhost:8080/bank/api/v1/health || exit 1
# Remove Manager and Host Manager applications
RUN rm -rf $CATALINA_HOME/webapps/manager \
    && rm -rf $CATALINA_HOME/webapps/host-manager

# Secure Tomcat file permissions
RUN chown -R root:root $CATALINA_HOME \
    && chmod -R 700 $CATALINA_HOME
COPY /target/classes/templates/index.html /usr/local/tomcat/webapps/ROOT/index.html
COPY /target/classes/templates/query.html /usr/local/tomcat/webapps/ROOT/query.html