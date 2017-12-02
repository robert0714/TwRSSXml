# Introduction
windows要先安裝Docker-toolbox <br/>
mvn docker:help <br/>
docker-machine rm default <br/>
docker-machine create  --virtualbox-memory 4096 default <br/>
eval $( docker-machine env  default ) <br/>
mvn clean package docker:build <br/>
mvn docker:start <br/>
# Docker building
[weblogic@1204003pc01 TwRSSXml]$ docker build -t robert0714/oracle-java-8 .