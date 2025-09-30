#!/usr/bin/env bash
# Install Maven
curl -sSL https://apache.osuosl.org/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.tar.gz | tar -xz -C /tmp
export PATH=/tmp/apache-maven-3.9.9/bin:$PATH

# Go into backend folder
cd roadmap-Backend

# Build JAR
mvn clean package -DskipTests
