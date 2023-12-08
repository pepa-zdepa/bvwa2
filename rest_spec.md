Autentizace pomocí session, od loginu po logout v cookie \
**session** - ip uživatele, id uživatele, čas vypršení, role

### response
všechny resty vrací
* 200 - ok
* 401 - invalid credentials

další definované u samotných restů v *response*

# REST
### POST /auth/login
přihlásí uživatele
#### body: form-url-encoded
* user
* password

### POST /auth/logout
odhlásí uživatele

### POST /auth/refresh
prodlouží relaci uživatele

### GET /user
vrátí současného uživatele (id v session)
#### response:
```
{
    "first_name": "string",
    "last_name": "string",
    "email": "string",
    "phone": "string",
    "gender": "string",
    "user": "string",
    "photo": image,
    "role": "string"
}
```

### POST /user
vytvoří nového uživatele
#### body: json/form-url-encoded TODO
```
{
    "first_name": "string",
    "last_name": "string",
    "email": "string",
    "phone": "string",
    "gender": "string",
    "user": "string",
    "password": "string",
    "photo": image
}
```

### PUT /user
upraví uživatele (id v session)
#### body: json/form-url-encoded TODO
```
{
    "first_name": "string",
    "last_name": "string",
    "email": "string",
    "phone": "string",
    "gender": "string",
    "user": "string",
    "photo": image
}
```

### DELETE /user TODO
smaže uživatele (id v session)
#### body: json/form-url-encoded TODO

### GET /user/message
vrátí všechny zprávy uživatele (id v session)
#### query:
* direction: String - incoming, outgoing
#### response: TODO
```
[
    {
        "id": String,
        "from": "string",
        "to": "string",
        "subject": "string",
        "message": "string"
    }
]
```

### GET /user/message/{messageId}
vrátí zprávu uživatele dle messageId
#### response: TODO
```
{
    "id": String, TODO
    "from": "string",
    "to": "string",
    "subject": "string",
    "message": "string"
}
```

### DELETE /user/message/{messageId}
smaže zprávu uživatele dle messageId
#### response: TODO
```
{
    "id": String, TODO
    "from": "string",
    "to": "string",
    "subject": "string",
    "message": "string"
}
```

### PUT /admin/user/{userId}/role
**pouze admin** \
upraví roli užívatele
#### query:
* newRole: String - nová role

### GET, PUT, DELETE /admin/user/{userId}
**pouze admin** \
získá, změní, smaže uživatele dle userId **(viz metody /user)**

### GET /admin/users
**pouze admin** \
získá všechny uživatele
#### response:
```
[
    {
        "first_name": "string",
        "last_name": "string",
        "user": "string"
    }
]
```