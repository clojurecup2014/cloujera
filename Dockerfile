FROM java:7

RUN sudo apt-get --assume-yes update

ADD ./target/uberjar/cloujera-0.1.0-SNAPSHOT-standalone.jar /srv/cloujera.jar

EXPOSE 8080

CMD ["java", "-jar", "/srv/cloujera.jar"]
