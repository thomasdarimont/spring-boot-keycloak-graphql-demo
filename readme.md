Spring Boot Keycloak GraphQL Demo
---

This example show how to secure an application that uses GraphQL and Spring Boot with [Keycloak](https://www.keycloak.org).

![Screenshot](https://raw.githubusercontent.com/thomasdarimont/spring-boot-keycloak-graphql-demo/master/screenshot.png "Screenshot")

This demo application is inspired by [oktadeveloper/okta-graphql-java-example](https://github.com/oktadeveloper/okta-graphql-java-example).
You can find a recording of [Matt Raible's](https://github.com/mraible) [demo on youtube](https://www.youtube.com/watch?v=y_OjfgZa58k&feature=youtu.be) 

# Build
```
mvn clean package -DskipTests
```

# Run

Start Keycloak
```
docker run \
  -it \
  --name graphql-keycloak \
  --rm \
  -e KEYCLOAK_USER=admin \
  -e KEYCLOAK_PASSWORD=admin \
  -e KEYCLOAK_IMPORT=/tmp/graphql-demo-realm.json \
  -v $PWD/graphql-demo-realm.json:/tmp/graphql-demo-realm.json \
  -v $PWD/data:/opt/jboss/keycloak/standalone/data \
  -p 8080:8080 \
  quay.io/keycloak/keycloak:11.0.2
```

Run the app via:
```
mvn
```
Note that we use spring-boot:run as the default goal in [pom.xml](pom.xml).

Point your browser to http://localhost:7777/ and login as user `tester` with password `test`.

After login you can play with the following queries.

# Queries

## Query for wines with `id` and `name`
```
{wines {id name}}
```

Result
```json
{
  "data": {
    "wines": [
      {
        "id": 1,
        "name": "Wine 1"
      },
      {
        "id": 2,
        "name": "Wine 2"
      },
      {
        "id": 3,
        "name": "Wine 3"
      }
    ]
  }
}
```


## Query for wines with `id`, `name` and computed field `rating`
```
{wines {id name rating}}
```

Result
```json
{
  "data": {
    "wines": [
      {
        "id": 1,
        "name": "Wine 1",
        "rating": 10
      },
      {
        "id": 2,
        "name": "Wine 2",
        "rating": 2
      },
      {
        "id": 3,
        "name": "Wine 3",
        "rating": 7
      }
    ]
  }
}
```

## Query wine by ID
```
{wine(id:1) {name, rating}}
```

Result
```json
{
  "data": {
    "wine": {
      "name": "Wine 1",
      "rating": 10
    }
  }
}
```

## Insert new wine
```
mutation {
  saveWine(wine:{name: "Wine 4"})
  {
    id
    name
  }
}
```

Result
```json
{
  "data": {
    "saveWine": {
      "id": 4,
      "name": "Wine 4"
    }
  }
}
```