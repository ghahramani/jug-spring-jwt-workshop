# Requirements
To be able to route traffic of localhost to https, you need to install LocalTunnel via command below
```shell
npm install -g localtunnel
```

Then you need to run these three commands to route https traffic to localhost

```shell
lt --port 9000 --subdomain auth-server
```
```shell
lt --port 8090 --subdomain auth-user
```
```shell
lt --port 8080 --subdomain auth-client
```

Then run all services and open the link below in your browser
```
https://auth-client.loca.lt/login
```

# Users

There are three usernames:

1. Admin
   1. User: admin
   2. Password: password
   3. Roles: ENDPOINT_ADMIN
2. User
   1. User: user
   2. Password: password
   3. Roles: ENDPOINT_USER
3. Actuator
   1. User: actuator
   2. Password: password
   3. Roles: ACTUATOR

# Clients

Also, there is a client with the details below:

Client Id: navid
Client Secret: navid-pass
Redirect URL: https://auth-client.loca.lt/login/oauth2/code/navid-oidc
Authentication Method: client_secret_basic

Grant Types: 
   1. authorization_code
   2. refresh_token
   3. client_credentials

Scopes: 
   1. ACTUATOR_READ
   2. ENDPOINT_READ
   3. ENDPOINT_WRITE
   4. ENDPOINT_DELETE
   5. openid
   6. profile
   7. email