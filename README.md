# WorkMotionTask
## Requirements
  Java 11,
  Maven,
  Docker
  
  
To make a docker image/container we have to have an executable Jar file so run
### maven install

Then to go to the terminal and get over the folder and run this command
### docker-compose up -d
This will create the image and run the container in detached mode(Background)

## API Contract
Go to the browser and type : localhost:8080/swagger-ui/

## H2 Database console
Go to the browser and type : localhost:8080/h2-console/
and put this 'jdbc:h2:mem:testdb' in the url textbox

Now you're good to go.
