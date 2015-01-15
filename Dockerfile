FROM dockerfile/java

RUN sudo apt-get --assume-yes update

RUN sudo apt-get --assume-yes install python-setuptools
RUN sudo easy_install supervisor
ADD ./prod-config/supervisord.conf /etc/supervisord.conf

ADD ./target/uberjar/cloujera-0.1.0-SNAPSHOT-standalone.jar /srv/cloujera.jar

EXPOSE 8080

CMD ["sudo", "supervisord", "-c", "/etc/supervisord.conf"]
