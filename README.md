# dockerops
docker compose app including the archicture of java, mongo, express and volumes -It serves as a practice of the core concepts (moving from version changes until persistance is acquired)


---
### container
- a way to package an application with all the necessary dependencies and configurations
- portable artifact, easily shared and moved around
- containers/images live in repositories, e.g. docker hub, or ecr for aws (private)
- a container has its own isolated environment, i.e. including alpine lightweight linux distro
- a container has packaged all needed configuration, e. g. start script
- one command to install the app regardless the os
- its also possible to run same app with 2 or more different versions
- a deployment before containers had artifacts like jar files or db with its own set of configurations to follow depending the underlying os. the monolthic deployment had to be set up again under the uplevel environment servers normally by the operations team (configuration on hosted servers needed, e. g. stage/prod) and then because of the the model, dependency version conflicts occurred caused by a misunderstanding of the deployment guide
- a deployment after containers involves developers and operations in early stages to work together for packaging the application inside a container, i.e. devops inside teams or teams where every team member work on packaging and developing. No environmental configuration is needed on server for running, except docker runtime, e.g. simply pull java app container/image and then run


---
### what is a container?
- layers of images, i.e. layers of stack images among each other
- mostly linux based images because of small size like alpine distro
- application image on top layer, e.g. layer0=alpine:3.10, layer1=intermediate-layer-app:x.x, layer2=application-image:x.x


---
### terminal usage
- pull container/image:version and start it using "run" command, e.g. public postgres container/image from dockerhub repository   
- latest <pre><code>docker run --name my-postgres -e POSTGRES_PASSWORD=mysecretpass -d postgres</code></pre>
- 9.6 <pre><code>docker run --name my-postgres-legacy -e POSTGRES_PASSWORD=mysecretpass -d postgres:9.6</code></pre>
- list containers/images running <pre><code>docker ps</code></pre>
- check container/image log by container name<pre><code>docker logs -f my-postgres</code></pre>

**NOTE**
> no version means latest, d=detached i.e. background process, e=environment variable, f=follow log output
- hashes listed during pull process represent layers to download/downloaded as containers/images share common layers, i.e. alpine, or intermediate ones. The advantange of using common layers is that pulling a different package versions of the same application doesn't require to fetch the entire layers of stack images and instead it reuses most of them


---
### docker image vs docker container
- image is the actual package including layers of stack images, i.e. configuration + application layer e.g. postgres:9.6 + start script. Image is the artifact that can be moved around
- the container is the application started after the image is pulled. In other words, the container environment is created after application starts
- e.g. <pre><code>docker ps
CONTAINER ID   IMAGE          COMMAND                  CREATED         STATUS         PORTS      NAMES
4e4be23f47d7   postgres:9.6   "docker-entrypoint.s…"   2 minutes ago   Up 2 minutes   5432/tcp   my-postgres-legacy
47290f4da968   postgres       "docker-entrypoint.s…"   7 minutes ago   Up 7 minutes   5432/tcp   my-postgres</code></pre>


---
### docker vs virtual machine  
OOSS have two layers1=os kernel, layer2=applications (top layer), e.g. linux distros use the "same" linux kernel but have implemented different applications on top

<img src="https://github.com/paguerre3/dockerops/blob/master/os-layers.PNG" width="48%" height="30%">

**NOTE**
> layer0=hardware (bottom layer)
- docker virtualizes the application layer of the system but it uses the kernel of the host
- virtual box or virtual machine has the application layer and the os kernel layers virtualized therefore it virtualizes the complete operative system
- size of docker images are smaller, i.e. because docker virtualizes the aplication layer only e.g. image size measured in mb instead of gb for vm
- docker containers run faster because they run on top of the existent os kernel
- a virtual machine is more portable and in terms of compatibily is considered better as it can be ran above different OOSS like windows
- docker runs on top of the os kernel therefore is less portable and in terms of compatibility it can't be run above certain windows versions (lower than 10) because most docker images are based on lightweight linux application layers e.g. alpine distro not compatible with lower versions of windows. As workaround docker desktop supports hyper-v, linux as subsystem of windows and virtual machine platform
<img src="https://github.com/paguerre3/dockerops/blob/master/docker-vs-vm.PNG" width="48%" height="30%">

**NOTE**
> virtualization must be enabled in windows bios, if not, enter bios and enable it
> after login, go to admin tasks -> performance and check that virtualizations is enabled


---
### differences between image and container
- container is the running environment of the image
- application image e.g. postgres:9.6, mongo:x, ... and a container includes application image + environment configurations + file system
- a container has a "port binded" to talk to the application running inside the container e.g. port:5000
- the file system of the container is virtual i.e. not equals to the file system of the host machine
- a container has its own abstraction of the os
- images to download have versions or tags (version-tag), dockerhub has only images


---
### main docker commands
- pull image:version/-tag <pre><code>docker pull redis</code></pre>
- check images downloaded  <pre><code>docker images
REPOSITORY    TAG       IMAGE ID       CREATED         SIZE
postgres      9.6       7a313171f464   11 days ago     200MB
postgres      latest    4ea2949e4cb8   11 days ago     314MB
redis         latest    621ceef7494a   2 weeks ago     104MB</code></pre>
- for creating the container of the image in detached mode<pre><code>docker run --name my-redis -d redis</code></pre>
- check container/image log file by container name<pre><code>docker logs -f my-redis</code></pre>
- list running containers <pre><code>docker ps
CONTAINER ID   IMAGE          COMMAND                  CREATED         STATUS         PORTS      NAMES
cc97044ccf55   redis          "docker-entrypoint.s…"   2 minutes ago   Up 2 minutes   6379/tcp   my-redis
4e4be23f47d7   postgres:9.6   "docker-entrypoint.s…"   15 hours ago    Up 15 hours    5432/tcp   my-postgres-legacy
47290f4da968   postgres       "docker-entrypoint.s…"   15 hours ago    Up 15 hours    5432/tcp   my-postgres</code></pre>
- stop container ID<pre><code>docker stop cc97044ccf55</code></pre>
- start container ID<pre><code>docker start cc97044ccf55</code></pre>
- list running and stopped containers <pre><code>docker ps -a
CONTAINER ID   IMAGE          COMMAND                  CREATED          STATUS                    PORTS      NAMES
cc97044ccf55   redis          "docker-entrypoint.s…"   15 minutes ago   Up 4 minutes              6379/tcp   my-redis
5f49dac6a060   hello-world    "/hello"                 13 hours ago     Exited (0) 13 hours ago              epic_satoshi
bcda7a78d2a3   hello-world    "/hello"                 13 hours ago     Exited (0) 13 hours ago              my-hello-w
4e4be23f47d7   postgres:9.6   "docker-entrypoint.s…"   15 hours ago     Up 15 hours               5432/tcp   my-postgres-legacy
47290f4da968   postgres       "docker-entrypoint.s…"   15 hours ago     Up 15 hours               5432/tcp   my-postgres</code></pre>

**NOTE**
> docker run command pulls image and starts container


---
### container port vs host port
- multipe containers can run on the host machine without port conflicts e.g.: including same appication w/different versions
- host laptop/pc has certain ports availabe
- a container port can be "binded" to host port
- e.g. <pre><code>
host:5000 binded to container#1:5000, i.e. host port 5000 redirects traffic to port 5000 of container#1
host:3000 binded to container#2:3000, i.e. host port 3000 redirects traffic to port 3000 of container#2
host:3001 binded to container#3:3000, i.e. host port 3001 redirects traffic to port 3000 of container#3
</code></pre>

**NOTE**
> the laptop/pc with docker runtime is considered the host of the containers. In other words, containers run on the host laptop/pc
- conflict exists when using the same host port
- containers ports can be the same as long as they are "binded" to different host ports, e.g. container#1:3000 and container#2:3000 are accessed from different host ports, i.e. host:3000 and host:3001
- once the container port is "binded" to a host port it can be accessed from the host machine using the host port, e.g. some-app://localhost:3001 for accessing container#3:3000
- bind command is: run -p{host port}:{continer port} image-version/-tag. If a container exists with the same name it must be stopped in first place before executing run command 
- e.g.<pre><code>docker run -p6000:6379 --name my-redis-legacy -d redis:6.0-alpine</code></pre>
- e.g. 2<pre><code>docker run -p6001:6379 --name my-redis -d redis</code></pre>
- check port bindings<pre>docker ps
CONTAINER ID   IMAGE              COMMAND                  CREATED         STATUS         PORTS                    NAMES
03075a242df3   redis              "docker-entrypoint.s…"   9 seconds ago   Up 7 seconds   0.0.0.0:6001->6379/tcp   my-redis
8428a3012bda   redis:6.0-alpine   "docker-entrypoint.s…"   3 minutes ago   Up 3 minutes   0.0.0.0:6000->6379/tcp   my-redis-legacy<code></code></pre>


---
### debugging a container
- check container logs by container ID<pre><code>docker logs 03075a242df3</code></pre>
- check container logs by name<pre><code>docker logs my-redis-legacy</code></pre>
- get inside the terminal of the running container by container ID<pre><code>docker exec -it 03075a242df3 /bin/bash
root@03075a242df3:/data#</code></pre>

**NOTE**
> it=interative terminal, and at the end of the exec command use /bin/bash. Finally cursor changes to root user into the data directory of the container. Once inside the container, terminal command <code>pwd</code> returns the current directory, <code>env</code> lists environment variables, etc as its the application layer of the os virtualized, but please notice that it's a lightweight linux distro so curl isn't installed


---
### workflow with docker simplified
- 0 pull mongodb image from dockerhub and start container to communicate with custom java service <pre><code>docker run -p6003:27017 --name mongodb -e MONGO_INITDB_ROOT_USERNAME=root -e MONGO_INITDB_ROOT_PASSWORD=pass -d mongo</code></pre>
- 1 commit code to git
- 2 CI builds java service and creates docker image of custom java application
- 3 CI pushes the docker image created to a private repository
- 4 development server pulls both images, mongodb from a public repostory and custom java application from the private repository
- 5 development server starts both containers for testing them

**NOTE**
> custom java service is built using spring-boot/gradle and located under dockeropsvc
- for testing step0 in host go to dockeropsvc and build artifact<pre><code>./gradlew bootJar</code></pre>
- then, go to dockeropsvc/build/libs and run artifact:<pre><code>java -Dspring.data.mongodb.uri=mongodb://root:pass@localhost:6003/employeedb?authSource=admin -jar dockeropsvc-0.0.1-SNAPSHOT.jar</code></pre>
- test step0 in host with curl <pre><code>curl -X POST "localhost:8080/dockeropsvc/v1/employees" -H  "accept: application/json" -H  "Content-Type: application/json" -d '{"email": "pablo@gmail.com","name": "pablo"}'
curl -X POST "localhost:8080/dockeropsvc/v1/employees" -H  "accept: application/json" -H  "Content-Type: application/json" -d '{"email": "camila@gmail.com","name": "camila"}'
curl -X GET "localhost:8080/dockeropsvc/v1/employees" -H  "accept: application/json"
curl -X GET "localhost:8080/dockeropsvc/v1/employees/1" -H  "accept: application/json"
curl -X DELETE "localhost:8080/dockeropsvc/v1/employees/1" -H  "accept: application/json"</code></pre>


---
### docker network
- docker creates its own isolated docker network where the containers are running in
- when two containers are deployed under the same docker network then both containers can communicate with each other using their "container names" i.e. without localhost:port, e.g. mongodb with mongoexpress if the two containers are running using these names
- once the custom java service is packaged as an image and then run it as a container it can communicate with mongodb using its container name instead localhost:port of the host because of the common docker network
- finally, the browser will access java custom service container with a host port binded to the container of the custom appication
- docker already provides some networks that can be listed<pre><code>docker network ls
NETWORK ID     NAME      DRIVER    SCOPE
03c58a712462   bridge    bridge    local
3cbc36b487dc   host      host      local
fd94f8491208   none      null      local</code></pre>
- for creating a network that allows communication by containers names, e.g. between mongodb and mongoexpress<pre><code>docker network create mongo-network</code></pre>
- during run command we need to provide network names to the containers<pre><code>docker run -p 6003:27017 --name mongodb -e MONGO_INITDB_ROOT_USERNAME=root -e MONGO_INITDB_ROOT_PASSWORD=pass --net mongo-network -d mongo
docker run -p 8081:8081 --name mongoexpress -e ME_CONFIG_MONGODB_ADMINUSERNAME=root -e ME_CONFIG_MONGODB_ADMINPASSWORD=pass -e ME_CONFIG_MONGODB_SERVER=mongodb --net mongo-network -d mongo-express</code></pre>

**NOTE**
> MONGODB_SERVER=mongodb i.e. container name of mongo as its using the same docker network

- to see latest log entries by container ID<pre><code>docker logs 447a624a3952 | tail</code></pre>
- to stream log entries by container ID<pre><code>docker logs 447a624a3952 -f</code></pre>


---
### docker compose
- tool for running multiple containers simpler than using commands. In other words, its a structured way for running multiple containers instead of using docker commands, easier to mantain/edit
- e.g. [mongo docker compose yml](https://github.com/paguerre3/dockerops/blob/master/mongo.yml)

**NOTE**
> Docker Compose takes care of creating a docker network automatically! i.e. there is no need of creating a common network manually. Docker compose file example doesn't contain one. Docker Compose is already installed with Docker
- run command is: docker-compose -f {file-name} up, e.g.<pre><code>docker-compose -f mongo.yml up
Creating network "dockerops_default" with the default driver
Creating dockerops_mongodb_1      ... done
Creating dockerops_mongoexpress_1 ... done
... logs of all containers are mixed in the output</code></pre>

**NOTE**
> for docker-compose f=file instead of follow, up=for starting all containers inside yml. The action is specified at the end of the docker-compose command, e.g. up/down of all containers inside yml. Down stops and removes containers and network
- e.g. background mode<pre><code></code>docker-compose -f mongo.yml up -d</pre>
- docker-compose removal<pre><code>docker-compose -f mongo.yml down
Stopping dockerops_mongoexpress_1 ... done
Stopping dockerops_mongodb_1      ... done
Removing dockerops_mongoexpress_1 ... done
Removing dockerops_mongodb_1      ... done
Removing network dockerops_default</code></pre>


---
### dockerfile
- to deploy the custom application is required to package it into a docker container. Built artifact is copied into the container including configurations, e.g. .jar, .war, bundle.js, etc
- dockerfile is used to build an image of the custom application/service
- this is actually step3 of the workflow where the CI builds java service and creates docker image of custom java application
- dockerfile is the "blueprint" for building images
- <code>FROM</code>normally a new image is based on another, its always recommended to start from a base image in order to avoid starting from scratch or an alpine distro where only basic stuff is pre-installed, e.g. usually the dockerfile of a js application starts with <code>FROM node:version/-tag</code> so there is no need to install nodejs as its already installed in the base layer/image
- <code>ENV</code>environment variables can be added in docker file but its recommended to not set them inside the blueprint as changes on configurations will require to rebuild the image
- <code>RUN</code>commands added into dockerfile apply to the container and not on the host, e.g. <code>RUN mkdir -p /home/app</code> 
- <code>COPY</code>command can take things from the host and copy them to the container, e.g. <code>COPY . /home/app</code> 
- <code>CMD</code>executes an entry point linux command, e.g. <code>CMD ["node", "server.js"]</code>means start the js application with: <code>node server.js</code>
- the difference beteween <code>RUN</code> and <code>CMD</code> is that <code>RUN</code> can be used multiple times and <code>CMD</code> is the default entry point for running the application inside the container, i.e. <code>CMD</code> marks the dockerfile entry point for starting the application inside the container
- e.g. of image layers in case of a custom js application: layer0=alpine:3.10, layer1=node:13-alpine<code>FROM alpine:3.10</code>, layer2=custom-js-app:1.0<code>FROM node:13-alpine</code>

**NOTE**
> built artifact is the pre-requisite, i.e. <code>./gradlew bootJar</code> already done. The mentioned step is normally done by the CI when building the custom java application using gradle wrapper therefore the next step of the pipeline will be building the image based on the dockerfile taking into account that the artifact was built successfully
- e.g. [Dockerfile of custom java application/service](https://github.com/paguerre3/dockerops/blob/master/Dockerfile)
- command for building an image is <code>build -t {app-name:version/-tag} {location of Dockerfile}</code>, e.g. Dockerfile in current directoy:<pre><code>docker build -t dockeropsvc:1.0 .</code></pre> 
- check images created or pulled:<pre><code>docker images
REPOSITORY      TAG          IMAGE ID       CREATED         SIZE
dockeropsvc     1.0          14fd715fc174   4 minutes ago   173MB
redis           6.0-alpine   18e4b21eb324   2 days ago      31.3MB
postgres        9.6          7a313171f464   13 days ago     200MB
postgres        latest       4ea2949e4cb8   13 days ago     314MB
mongo           latest       ca8e14b1fda6   13 days ago     493MB
redis           latest       621ceef7494a   3 weeks ago     104MB
mongo-express   latest       05bf9d904cd0   4 weeks ago     129MB
hello-world     latest       bf756fb1ae65   13 months ago   13.3kB</code></pre> 

**NOTE**
> application code including Dockerfile is commited to git, then CI makes the artifact, builds the docker image, and finally it pushes the image to a docker repository that can be public or private, e.g. published into docker.io or ecr for aws. The mentioned explanation is actually the summary of steps 1, 2 and 3 of the workflow.
- if needed, container removal command is <code>docker rm {container ID}</code>
- image removal command is <code>docker rmi {image ID}</code>
- test locally before publishing artifact:<pre><code>docker run -p 8080:8080 --name dockeropsvc -e JAVA_OPTS="-Dspring.data.mongodb.uri=mongodb://root:pass@mongodb/employeedb?authSource=admin" --net dockerops_default -d dockeropsvc:1.0</code></pre>
- check logs<pre><code>docker logs {container ID}</code></pre>
- enter container terminal<pre><code>docker exec -it {container ID} sh</code></pre>
- once inside the terminal, check environment passed is right<pre><code>env
LANGUAGE=en_US:en
HOSTNAME=22be26528487
SHLVL=1
HOME=/root
JAVA_VERSION=jdk-11.0.10+9
TERM=xterm
PATH=/opt/java/openjdk/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
JAVA_OPTS=-Dspring.data.mongodb.uri=mongodb://root:pass@mongodb/employeedb?authSource=admin
LANG=en_US.UTF-8
LC_ALL=en_US.UTF-8
JAVA_HOME=/opt/java/openjdk
PWD=/usr/app</code></pre>


---
### private docker registry
- docker private repository, i.e. amazon ecr=elastic container registry
- registry options
- build and tag an image
- docker login
- docker logout

**NOTE**
> existent amazon account is the pre-requisite
- go to the browser, login into aws and search for ecr service
- click on get started to create a repository
- select "private" repository visibility
- under repository name write the name of the application, e.g. dockeropsvc
- leave other values as default and click "create repository" 

**NOTE**
> amazon creates one repository per image! so naming the repository as the application name is the best option  
- full repository location e.g. 385021697482.dkr.ecr.sa-east-1.amazonaws.com/dockeropsvc

**NOTE**
> image naming in docker registry=registryDomain/imageName:tag (tag=version/-tag).
> the domain of public repositories from dockerhub starts with docker.io/library therefore when official images are pulled there is no need of specifying the initial address as, by default, docker adds the prefix address, e.g. docker.io/library/mongo:latest. For a private registry, as aws ecr, the registryDomain prefix must be added when pulling the image    
- once the repository is created, click on "repository name", e.g. dockeropsvc
- inside the repository, empty data is shown as its the 1st time. Actually, the versions/-tags of the same image are stored and can be seen clicking on the respository name link. In other words, different version/-tags of the same image are stored in a respository per application
- check "push commands" to see the commands for publishing the local image of the custom application into the private repository of amazon

**NOTE**
> push commands differ between linux and windows. Aws Cli/linux or Aws Tools for power-shell/windows need to be installed and Credentials for login are the pre-requisites
- [Aws Cli](https://docs.aws.amazon.com/AmazonECR/latest/userguide/getting-started-cli.html)
- [Aws tools for power-shell](https://aws.amazon.com/es/powershell/)
- 1 retrieve an authentication token and authenticate docker client to your registry, i.e. using the login command according to the os. Check "push commands" ref according to os type. If everything goes right, a "Login Succeeded" message is displayed
- 2 build docker image, as it was already done previously, there is no need of doing it again<pre><code>docker build -t dockeropsvc:1.0 .</code></pre>
- 3 after build completes, tag image so it can be pushed into the aws repository created. It will copy/rename the image/tag in order to be compliant with aws private repository, e.g. <pre><code>docker tag dockeropsvc:1.0 385021697482.dkr.ecr.sa-east-1.amazonaws.com/dockeropsvc:1.0</code></pre>
- 3.1 check images after tag rename<pre><code>docker images
REPOSITORY                                                 TAG          IMAGE ID       CREATED         SIZE
385021697482.dkr.ecr.sa-east-1.amazonaws.com/dockeropsvc   1.0          453516db9816   5 hours ago     173MB
dockeropsvc                                                1.0          453516db9816   5 hours ago     173MB
redis                                                      6.0-alpine   18e4b21eb324   2 days ago      31.3MB
postgres                                                   9.6          7a313171f464   2 weeks ago     200MB
postgres                                                   latest       4ea2949e4cb8   2 weeks ago     314MB
mongo                                                      latest       ca8e14b1fda6   2 weeks ago     493MB
redis                                                      latest       621ceef7494a   3 weeks ago     104MB
mongo-express                                              latest       05bf9d904cd0   4 weeks ago     129MB
hello-world                                                latest       bf756fb1ae65   13 months ago   13.3kB</code></code>
- 4 run the command to push the image to the newly created aws repository<pre><code>docker push 385021697482.dkr.ecr.sa-east-1.amazonaws.com/dockeropsvc:1.0</code></pre>

**NOTE**
> the push command publishes the image, i.e. layer by layer. Once done, the image tag will be displayed in aws<code>ecr > repositories > dockeropsvc > images</code>


---
### deploying the application
- custom java/service image from private repository, i.e. aws ecr
- dependency images as mongo/mongo-express from public registry, i.e. dockerhub
- deploy multiple containers
- deployment server
- e.g. [complete docker compose file yml](https://github.com/paguerre3/dockerops/blob/master/complete.yml)

**NOTE**
> user must be logged into aws for pulling the private image as pre-requisite, i.e. step1 of the push command section can be reused.
> The complete docker compose file represents the deploy of multiple containers, i.e. including private and public repositories. 
> Please ensure to purge pre-existent containers in order to avoid possible conficts with the reuse of common host ports
- run docker compose command for pulling/starting all deployments<pre><code>docker-compose -f complete.yml up -d
Starting mongoexpress     ... done
Starting dockeropsvc_priv ... done
Starting mongodb          ... done</code></pre>

**NOTE**
> this section maps to steps 4 and 5 of the workflow


---
### docker volumes
- docker volumes are used for data persistence, e.g. data bases or other stateful applications
- as mentioned, a container runs under a host that has docker runtime. The container has its own virtualized file system so the data is gone when restarting or removing the container if there is no volume mounted from host
- the way a Docker Volume works is that a Folder in physical host file system is mounted into the virtual file system of Docker, e.g. host-file-system(physical)=/home/mount/data is attached to container-file-system(virtual)=/var/lib/postgresql/data. When the container writes its data into the virtual file system it gets replicated into the physical host file system        
- 3 volume types: 1. host=you decide where on the host file system the reference is made, e.g. <code>docker run -v /home/mount/data:/var/lib/postgresql/data</code>. 2. anonymous=no host file system reference is specified so Docker takes care of this, i.e. for each container a folder is auto-generated in host and then mounted, e.g. <code>docker run -v /var/lib/postgresql/data</code> is referenced by Docker to /var/lib/docker/volumes/random-hash/_data in the host. 3. named-volume=its an improvement of the previous type where you can reference the volume by a name, e.g.  <code>docker run -v name:/var/lib/postgresql/data</code>. Named voumes are normally used in production as they are simpler to manage, e.g. the same host location reference&data can be shared by "name" among different containers if set under a common section of the docker compose file as the example bellow
- e.g. [complete docker compose file yml with docker volume type named](https://github.com/paguerre3/dockerops/blob/master/complete-v.yml)

**NOTE**
> virtual file system path differs from each data base, e.g. mongo=/data/db, mysql=/var/lib/mysql, postgres=/var/lib/postgresql/data
- run compose with up/down to restart several times and see that data was persisted in host file system<code>docker-compose -f complete-v.yml up -d</code>/<code>docker-compose -f complete-v.yml down</code> and up again. Check mongoexpress or custom java application/service after docker-compose restarts 

**NOTE**
> volumes managed by docker are stored in different paths depending on the os, e.g. linux=/var/lib/docker/volumes. In windows and mac, the address for Docker managed volumes can be confusing as you can't access C:\ProgramData\docker\volumes or /var/lib/docker/volumes directly because of virtualization
- Each volume has its own hash/_data in case of anonymous or in case of a named volume e.g.<pre><code>docker volume inspect dockerops_mongodata
[
&nbsp;{
&nbsp;&nbsp;"CreatedAt": "2021-02-05T01:41:01Z",
&nbsp;&nbsp;"Driver": "local",
&nbsp;&nbsp;"Labels": {
&nbsp;&nbsp;&nbsp;"com.docker.compose.project": "dockerops",
&nbsp;&nbsp;&nbsp;"com.docker.compose.version": "1.27.4",
&nbsp;&nbsp;&nbsp;"com.docker.compose.volume": "mongodata"
&nbsp;&nbsp;},
&nbsp;&nbsp;"Mountpoint": "/var/lib/docker/volumes/dockerops_mongodata/_data",
&nbsp;&nbsp;"Name": "dockerops_mongodata",
&nbsp;&nbsp;"Options": null,
&nbsp;&nbsp;"Scope": "local"
&nbsp;}
]</code></pre>