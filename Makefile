.PHONY: client server build

client:
	./gradlew runClient

server:
	./gradlew runServer

build:
	./gradlew clean build
