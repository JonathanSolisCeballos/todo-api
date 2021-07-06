
# Restful Server of To-do List

This is an resful api made with maven, JAVA and using sqlite as a DB, was made by [Jonathan solis].

## Installation
In order to build the project, you will need to assemble the dependencies and then package the project into a jar:

```mvn package```

Then do

```some other command```

## Usage
The API can respond to the following endpoints:

```curl -d '{"title":"example title", "completed":"0"}' -H "Content-Type: application/json" -X POST http://localhost:800/api/v1/todos```

Responds with the following:

```
TO-DO ADDED SUCCESFULLY
```
OR
```
ERROR WHEN ADDING TO-DO
```