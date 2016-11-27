#!/bin/bash
mvn clean install
cd server-auth
mvn tomcat7:redeploy
cd ..
