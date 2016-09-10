#!/bin/bash
echo "Building application"
mvn clean install
mv server-auth/target/server-auth-*.war server-auth/docker/server-auth.war
mvn clean
echo "Building Docker image"
cd server-auth/docker
docker build -t server-auth .
echo "To run the image: $ docker run -it  -p 8080:8080 server-auth" 