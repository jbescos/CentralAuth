# To deploy it in Docker just do this:

$ docker build -t server-auth .
$ docker run -it  -p 8080:8080 server-auth

# You can test it now using: 
http://localhost:8080/server-auth/

# To list the available images
$ docker images

# Remove image
$ docker rmi -f IMAGE_ID

# To list containers
$ docker ps -a

# To remove container
$ docker rm CONTAINER_ID