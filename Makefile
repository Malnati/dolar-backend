JAR_NAME = backend-0.0.1-SNAPSHOT.jar
APP_BACKEND_PATH = backend
APP_IMAGE_NAME = dolar-backend_backend
APP_CONTAINER_NAME = dolar-backend
DOCKER_COMPOSE_FILE = docker-compose.yml

.DEFAULT: help
.PHONY : help

help : Makefile
	@sed -n 's/^##//p' $<
 
## make prune             : docker system prune -a --volumes
##                          TAKE CARE WITH PRUNE!
prune: 
	docker system prune -a --volumes

## make ps                : docker ps --all

ps: 
	docker ps --all

## make stats             : docker container stats $(docker container ps --format={{.Names}})
##                          To list all running containers with CPU, Memory, Networking I/O and Block I/O stats.

stats: 
	docker container stats $(docker container ps --format={{.Names}})

## make network           : docker network ls

network:
	docker network ls

## make volumes           : mkdir -p ~/volumes/$(APP_CONTAINER_NAME)/www/html/

volumes: 
	mkdir -p ~/volumes/$(APP_CONTAINER_NAME)/

## make stash             : git stash save "Changes saved by Makefile."

stash: 
	git stash save "Changes saved by Makefile."

## make install           : mvn -P local -f $(APP_BACKEND_PATH)/pom.xml clean install

install: 
	mvn -P local -f $(APP_BACKEND_PATH)/pom.xml clean install

## make sonar             : mvn -f $(APP_BACKEND_PATH)/pom.xml initialize sonar:sonar 

sonar: 
	mvn -f $(APP_BACKEND_PATH)/pom.xml initialize sonar:sonar 

## make local             : mvn spring-boot:run -P local -f $(APP_BACKEND_PATH)/pom.xml

local: 
	make clean && make install && mvn spring-boot:run -P local -f $(APP_BACKEND_PATH)/pom.xml

## make clean             : rm -f $(APP_BACKEND_PATH)/target/$(JAR_NAME)
##                          mvn -P local -f $(APP_BACKEND_PATH)/pom.xml clean 

clean: 
		rm -f $(APP_BACKEND_PATH)/target/$(JAR_NAME)
		mvn -P local -f $(APP_BACKEND_PATH)/pom.xml clean 
	    

## make bash              : docker run -it $(APP_CONTAINER_NAME) /bin/bash

bash: 
	docker run -it $(APP_CONTAINER_NAME) /bin/bash
	
## make up                : docker-compose -f $(DOCKER_COMPOSE_FILE) up -d

up: 
	docker-compose -f $(DOCKER_COMPOSE_FILE) up -d && make logs

## make down              : docker-compose -f $(DOCKER_COMPOSE_FILE) down

down: 
	docker-compose -f $(DOCKER_COMPOSE_FILE) down

## make start             : docker-compose -f $(DOCKER_COMPOSE_FILE) start

start: 
	docker-compose -f $(DOCKER_COMPOSE_FILE) start

## make remove            : docker-compose -f $(DOCKER_COMPOSE_FILE) start

remove: 
	docker start $(APP_CONTAINER_NAME) && docker rm $(APP_CONTAINER_NAME)

## make stop              : docker-compose -f $(DOCKER_COMPOSE_FILE) stop

stop: 
	docker-compose -f $(DOCKER_COMPOSE_FILE) stop

## make build             : docker build -t $(APP_IMAGE_NAME) --no-cache --force-rm \
##                                  --build-arg JAR_FILE=$(APP_BACKEND_PATH)/target/$(JAR_NAME) \
##                                  -f $(APP_BACKEND_PATH)/src/main/docker/Dockerfile ./

build: 
	docker build -t $(APP_IMAGE_NAME) --no-cache --force-rm --build-arg JAR_FILE=$(APP_BACKEND_PATH)/target/$(JAR_NAME) -f $(APP_BACKEND_PATH)/Dockerfile ./
	
## make logs              : docker logs $(APP_CONTAINER_NAME)_1 -f --tail=200
##                          MacOS: docker ps -aqf "name=$(APP_CONTAINER_NAME)"
##                          Linux: sudo docker ps -aqf "$(APP_CONTAINER_NAME)"
##                          docker inspect --format="{{.Id}}" $(APP_CONTAINER_NAME) for getting the Contatiner_ID
logs: 
	docker logs $(APP_CONTAINER_NAME) -f --tail=200

## make recreate          : make down && make prune && make up && make logs

recreate: 
	make down && make prune && make up 

## make deploy            : docker cp $(APP_BACKEND_PATH)/target/$(JAR_NAME) $(APP_CONTAINER_NAME):/$(JAR_NAME)

deploy: 
	mvn -P docker -f $(APP_BACKEND_PATH)/pom.xml clean install && docker cp $(APP_BACKEND_PATH)/target/$(JAR_NAME) $(APP_CONTAINER_NAME):/$(JAR_NAME)

## make image            : docker cp $(APP_BACKEND_PATH)/target/$(JAR_NAME) $(APP_CONTAINER_NAME):/$(JAR_NAME)

image: 
	docker image ls

## End Makefile 