FROM java:8
MAINTAINER Micro liuzh@56ctm.com
ENV ACTIVE=""
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
RUN bash -c 'touch ./app.jar'
EXPOSE 1551
ENTRYPOINT ["sh","-c","java -Djava.security.egd=file:/dev/./urandom -jar /app.jar $ACTIVE"]
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar $ACTIVE"]