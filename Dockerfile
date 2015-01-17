FROM dockerfile/java

RUN sudo apt-get --assume-yes update

ADD ./target/uberjar/cloujera-0.1.0-SNAPSHOT-standalone.jar /srv/cloujera.jar

EXPOSE 8080

CMD ["java", "-jar", "/srv/cloujera.jar", ">", "/var/log/cloujera.log", "2>&1"]
