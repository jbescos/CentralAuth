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

# Upload a image to a google cloud repository. In my case project_id = botlogic-140309
$ docker tag server-auth eu.gcr.io/botlogic-140309/server-auth
$ gcloud docker push eu.gcr.io/botlogic-140309/server-auth

# Usging kubectl -> http://kubernetes.io/docs/hellonode/
$ gcloud docker build -t eu.gcr.io/botlogic-140309/server-auth .
$ gcloud container clusters create server-auth
$ kubectl run server-auth --image=eu.gcr.io/botlogic-140309/server-auth:v1 --port=8080
$ kubectl cluster-info
$ kubectl expose deployment server-auth --type="LoadBalancer"
$ kubectl get services
