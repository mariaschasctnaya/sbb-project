FROM jboss/wildfly
ADD ./target/timetable-0.0.1.war /opt/jboss/wildfly/standalone/deployments/

RUN /opt/jboss/wildfly/bin/add-user.sh admin admin --silent

USER root
ADD wrapper.sh wrapper.sh
RUN bash -c 'chmod +x wrapper.sh'
RUN bash -c 'date'
RUN bash -c 'rm -rf /etc/localtime'
RUN bash -c 'ln -s /usr/share/zoneinfo/Europe/Moscow /etc/localtime'
RUN bash -c 'date'
USER jboss
ENTRYPOINT ["/bin/bash", "wrapper.sh"]

EXPOSE 8080 9990