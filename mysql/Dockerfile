FROM mysql

RUN bash -c 'echo Europe/Moscow >/etc/timezone && dpkg-reconfigure -f noninteractive tzdata'
RUN /entrypoint.sh