FROM eclipse-temurin:17-jdk-alpine as jdk-image

RUN ${JAVA_HOME}/bin/jlink --module-path jmods --add-modules jdk.jcmd --output /jcmd

FROM eclipse-temurin:17-jdk-alpine as build

RUN apk add --no-cache curl tar bash procps rsync


ARG MAVEN_VERSION=3.9.4

ARG USER_HOME_DIR="/root"

ARG BASE_URL=https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries

RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
  && echo "Downlaoding maven" \
  && curl -fsSL -o /tmp/apache-maven.tar.gz ${BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
  \
  && echo "Unziping maven" \
  && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
  \
  && echo "Cleaning and setting links" \
  && rm -f /tmp/apache-maven.tar.gz \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

RUN mkdir -p server

COPY . /server

RUN cd server && sh build.sh

FROM eclipse-temurin:17-jre-alpine as production

COPY --from=jdk-image /jcmd /jcmd

COPY --from=build /server/dist /dist
COPY --from=build /server/entrypoint.sh entrypoint.sh

LABEL maintainer="admin@localhost"

VOLUME /tmp

EXPOSE 8080

ARG JAR_FILE=/dist/server-*.jar
ARG PLUGIN_JARS=/dist/plugins/*.jar
ARG APPSMITH_SEGMENT_CE_KEY
ENV APPSMITH_SEGMENT_CE_KEY=${APPSMITH_SEGMENT_CE_KEY}

#Create the plugins directory
RUN mkdir -p /plugins

# Add entrypoint script and make it executable.

RUN chmod +x /entrypoint.sh

#Add the jar to the container. Always keep this at the end. This is to ensure that all the things that can be taken
#care of via the cache happens. The following statement would lead to copy because of change in hash value
RUN cp ${JAR_FILE} server.jar
RUN cp -r ${PLUGIN_JARS} /plugins/
HEALTHCHECK --interval=15s --timeout=15s --start-period=15s --retries=3 CMD wget --no-verbose --spider http://localhost:8080/api/v1/users/me/ || exit 1
ENTRYPOINT ["/entrypoint.sh"]
