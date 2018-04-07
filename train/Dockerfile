FROM tomcat:8.0.20-jre8
ADD ./target/train-0.0.1.war /usr/local/tomcat/webapps/train.war

RUN bash -c 'echo Europe/Moscow >/etc/timezone && dpkg-reconfigure -f noninteractive tzdata'

EXPOSE 8080