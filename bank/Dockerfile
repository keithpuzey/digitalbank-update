FROM tomcat:11.0.22-jdk17-temurin-jammy
# Environment variables
ENV CATALINA_HOME=/usr/local/tomcat
EXPOSE 8080
EXPOSE 8443
# Install OpenJDK 17 or any necessary packages
#  RUN apt-get update 
# Update the PATH environment variable
# ENV PATH="/usr/lib/jvm/java-17-openjdk-arm64/bin:${PATH}"
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
COPY /target/classes/templates/index.jsp /usr/local/tomcat/webapps/ROOT/index.jsp
COPY /target/classes/templates/query.html /usr/local/tomcat/webapps/ROOT/query.html
COPY /target/classes/templates/nestedtableexample1-landing.html /usr/local/tomcat/webapps/ROOT/nestedtableexample1.html
COPY /target/classes/templates/nestedtableexample-landing.html /usr/local/tomcat/webapps/ROOT/nestedtableexample.html
COPY /target/classes/templates/guicardexample-landing.html /usr/local/tomcat/webapps/ROOT/guicardexample.html
COPY /target/classes/templates/financedashboard.html /usr/local/tomcat/webapps/ROOT/financedashboard.html
COPY /target/classes/templates/health-dashboard.html /usr/local/tomcat/webapps/ROOT/health-dashboard.html
COPY /target/classes/templates/sv-user-profile.html /usr/local/tomcat/webapps/ROOT/sv-user-profile.html
COPY /target/classes/templates/sv-user-profile.js /usr/local/tomcat/webapps/ROOT/sv-user-profile.js
COPY /target/classes/templates/flow-demo.html /usr/local/tomcat/webapps/ROOT/flow-demo.html
COPY /target/classes/templates/sv-profile.html /usr/local/tomcat/webapps/ROOT/sv-profile.html
COPY /target/classes/templates/button-progressbar.html /usr/local/tomcat/webapps/ROOT/button-progeressbar.html
COPY /target/classes/templates/sv-profile.js /usr/local/tomcat/webapps/ROOT/sv-profile.js

COPY /target/classes/templates/about-submit-v2.html /usr/local/tomcat/webapps/ROOT/about-submit-v2.html
COPY /target/classes/templates/about-submit.html /usr/local/tomcat/webapps/ROOT/about-submit.html
COPY /target/classes/templates/activity-v2.html /usr/local/tomcat/webapps/ROOT/activity-v2.html
COPY /target/classes/templates/activity.html /usr/local/tomcat/webapps/ROOT/activity.html
COPY /target/classes/templates/business-v2.html /usr/local/tomcat/webapps/ROOT/business-v2.html
COPY /target/classes/templates/business.html /usr/local/tomcat/webapps/ROOT/business.html
COPY /target/classes/templates/employees-v2.html /usr/local/tomcat/webapps/ROOT/employees-v2.html
COPY /target/classes/templates/employees.html /usr/local/tomcat/webapps/ROOT/employees.html
COPY /target/classes/templates/faceid-v2.html /usr/local/tomcat/webapps/ROOT/faceid-v2.html
COPY /target/classes/templates/faceid.html /usr/local/tomcat/webapps/ROOT/faceid.html
COPY /target/classes/templates/index-v1.html /usr/local/tomcat/webapps/ROOT/index-v1.html
COPY /target/classes/templates/index-v2.html /usr/local/tomcat/webapps/ROOT/index-v2.html
COPY /target/classes/templates/loan-success-v2.html /usr/local/tomcat/webapps/ROOT/loan-success-v2.html
COPY /target/classes/templates/loan-success.html /usr/local/tomcat/webapps/ROOT/loan-success.html
COPY /target/classes/templates/loan-v2.html /usr/local/tomcat/webapps/ROOT/loan-v2.html
COPY /target/classes/templates/loan.html /usr/local/tomcat/webapps/ROOT/loan.html
COPY /target/classes/templates/payments-v2.html /usr/local/tomcat/webapps/ROOT/payments-v2.html
COPY /target/classes/templates/payments.html /usr/local/tomcat/webapps/ROOT/payments.html
COPY /target/classes/templates/pin-v2.html /usr/local/tomcat/webapps/ROOT/pin-v2.html
COPY /target/classes/templates/pin.html /usr/local/tomcat/webapps/ROOT/pin.html
COPY /target/classes/templates/revenue-v2.html /usr/local/tomcat/webapps/ROOT/revenue-v2.html
COPY /target/classes/templates/revenue.html /usr/local/tomcat/webapps/ROOT/revenue.html
COPY /target/classes/templates/styles-v2.css /usr/local/tomcat/webapps/ROOT/styles-v2.css
COPY /target/classes/templates/styles.css /usr/local/tomcat/webapps/ROOT/styles.css
COPY /target/classes/templates/success-v2.html /usr/local/tomcat/webapps/ROOT/success-v2.html
COPY /target/classes/templates/success.html /usr/local/tomcat/webapps/ROOT/success.html
COPY /target/classes/templates/website-v2.html /usr/local/tomcat/webapps/ROOT/website-v2.html
COPY /target/classes/templates/website.html /usr/local/tomcat/webapps/ROOT/website.html
COPY /target/classes/templates/login-demo.html /usr/local/tomcat/webapps/ROOT/login-demo.html

COPY /target/classes/templates/web.xml /usr/local/tomcat/conf/web.xml



# Copy the entire images folder into the webapps ROOT
COPY /target/classes/static/images/ /usr/local/tomcat/webapps/ROOT/images/
ADD https://repo1.maven.org/maven2/org/apache/tomcat/tomcat-jasper/11.0.18/tomcat-jasper-11.0.18.jar /usr/local/tomcat/lib/
ADD https://repo1.maven.org/maven2/jakarta/servlet/jsp/jakarta.servlet.jsp-api/3.1.1/jakarta.servlet.jsp-api-3.1.1.jar /usr/local/tomcat/lib/
