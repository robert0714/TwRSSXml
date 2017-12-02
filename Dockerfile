FROM sdorra/oracle-java-8:8u151
MAINTAINER Robert Lee <robert0714@gmail.com>


# install oracle java 8
RUN apt-get update -y \
    && apt-get install -y curl
    
# set environment
ENV JAVA_HOME /usr/lib/jvm/java-8-oracle