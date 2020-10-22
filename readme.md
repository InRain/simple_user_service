# Simple User service

Hi!
This simple REST service represents basic CRUD operations for users and roles.
This service developed using Spring BOOT and Hibernate ORM.

# Prerequisities

- You need to install PostgreSQL Server on your local machine to store information about users and roles.

- You need to configure src\main\resources\application.properties file (url, username, password)

```java
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=
```

# Getting Started

To use this service you need to have client that can send HTTP requests(GET, POST, UPDATE, DELETE). For example you can use Postman

By default, service endpoint url is like http://localhost:8090. You can specify another port in properties file.

# Service API Documentation

First of all you need to add new user role.

### Add new ROLE

URL: /roleapi/add
Method: POST
Body example:

```json
        {
            "name": "ADMIN"
        }
```

Response example:

```json
{
    "success": true,
    "object": {
        "id": 1,
        "created": "2020-10-22T10:21:57.739+00:00",
        "updated": "2020-10-22T10:21:57.739+00:00",
        "name": "ADMIN"
    },
    "errors": []
}
```

Then you can watch all roles in database

### Get all ROLES

URL: /roleapi/getall
Method: GET

Response example:

```json
{
    "success": true,
    "object": [
        {
            "id": 2,
            "created": "2020-10-20T18:14:32.039+00:00",
            "updated": "2020-10-20T18:14:32.039+00:00",
            "name": "USER"
        },
        {
            "id": 1,
            "created": "2020-10-20T18:18:00.179+00:00",
            "updated": "2020-10-20T18:18:00.179+00:00",
            "name": "ADMIN"
        }
    ],
    "errors": []
}
```

Now, when you have some roles, you can add new user

!!!Important
Password shoud have one uppercase letter, one lowercase letter and one number

### Add new USER

URL: userapi/add
Method: POST
Body example:

```json
{
    "login": "John",
    "name":"John Smith",
    "password":"aZdfskj2323",
    "roles":[
        {
            "id":1,
            "name":"ADMIN"
        }
    ]    
}
```

Response example:

```json
{
    "success": true,
    "object": {
        "id": 49,
        "created": "2020-10-22T08:50:45.542+00:00",
        "updated": "2020-10-22T08:50:45.544+00:00",
        "login": "John",
        "name": "John Smith",
        "password": "aZdfskj2323",
        "roles": [
            {
                "id": 1,
                "created": "2020-10-22T08:50:45.542+00:00",
                "updated": "2020-10-22T08:50:45.542+00:00",
                "name": "ADMIN"
            }
        ],
    },
    "errors": []
}
```

You can update existing user.
You can update user using id. You can update fields and roles of user


### Update USER

URL: userapi/update
Method: PUT
Body example:

```json
{
    "login": "John",
    "name":"John Smithson",
    "password":"aZdfskj2323",
    "roles":[
        {
            "id":1,
            "name":"ADMIN"
        }
    ]    
}
```

Response example:

```json
{
    "success": true,
    "object": {
        "id": 49,
        "created": "2020-10-22T08:50:45.542+00:00",
        "updated": "2020-10-22T08:50:48.235+00:00",
        "login": "John",
        "name": "John Smithson",
        "password": "aZdfskj2323",
        "roles": [
            {
                "id": 1,
                "created": "2020-10-22T08:50:45.542+00:00",
                "updated": "2020-10-22T08:50:45.542+00:00",
                "name": "ADMIN"
            }
        ],
    },
    "errors": []
}
```

And finally you can delete user by login and id

### Delete USER by login

URL: userapi/delete/login/{user_login}
Method: DELETE


Response example:

```json
{
    "success": true,
    "object": null,
    "errors": []
}
```

### Delete USER by id

URL: userapi/delete/login/{user_id}
Method: DELETE


Response example:

```json
{
    "success": true,
    "object": null,
    "errors": []
}
```
