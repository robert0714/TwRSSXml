FROM sdorra/oracle-java-8:8u151
MAINTAINER Robert Lee <robert0714@gmail.com>

    
# set locale
RUN apt-get update && apt-get install -y locales && rm -rf /var/lib/apt/lists/* \
    && localedef -i zh_TW -c -f UTF-8 -A /usr/share/locale/locale.alias zh_TW.UTF-8 \
    && apt-get install -y curl
    
ENV LANG zh_TW.utf8
    
# set environment
ENV JAVA_HOME /usr/lib/jvm/java-8-oracle