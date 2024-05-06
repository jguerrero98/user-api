# User Api

## Requisitos

- Java 8

- Maven

## Ejecutar en entorno local


- El diagrama y query de base datos estan en resources

- No es necesario correr la query de base de datos, ya que se esta usando JPA y crea las tablas al momento de correr el proyecto

- Cualquier modificacion de regexp de email y password estan en el properties

- Al ejecutar el proyecto se desplegara en el puerto 8085

- La ruta del swagger es http://localhost:8085/swagger-ui/

## Abrir consola h2

- Agregar @Bean para poder acceder a la consola de H2, ya que spring security bloquea el acceso

- Solo para ambiente de desarrollo


        @Bean
        org.h2.tools.Server h2Server() {
            Server server = new Server();
            try {
                server.runTool("-tcp");
                server.runTool("-tcpAllowOthers");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return server;
        }

## Endpoints

#### El primero a usar es el http://localhost:8085/user/register de metodo POST donde se enviara un cuerpo, por ejemplo:

        {
            "name": "Jose Carlos",
            "email": "jguerrero98developer@gmail.cl",
            "password": "aA1@ddda",
            "phones": [
                {
                "number": "987654321",
                "citycode": "1",
                "countrycode": "51"
                },
                {
                "number": "987654328",
                "citycode": "1",
                "countrycode": "51"
                }
            ]
        }

- Esto retornara un cuerpo con datos necesarios para poder usar los demas endpoints

#### El localhost:8085/user/modifyUser de tipo PATCH se le enviara un cuerpo, por ejemplo:

        {
            "id": "623e59dc-fab8-40d2-be0f-2d7846794a22",
            "name": "Jose",
            "password": "aA1@dddax",
            "phones": [
                {
                "number": "789456132",
                "citycode": "1",
                "countrycode": "51",
                "flag": "add"
                }
            ]
        }

- Es necesario enviar el token con Authorization Bearer Token que se genera al registrar el usuario o en el login

- El campo flag, acepta 3 parametros: add, modify y delete

- Donde con add se agregara el telefono al usuario


        {
            "number": "789456123",
            "citycode": "1",
            "countrycode": "51",
            "flag": "add"
        }

- Con modify, enviandole el parametro id del telefono, se podra modificar


        {
            "id": 1,
            "number": "789456124",
            "citycode": "1",
            "countrycode": "51",
            "flag": "add"
        }

- Con delete, enviandole el id se eliminara de la bd, cambiando su estado a false


        {
            "id": 1,
            "flag": "delete"
        }

- Retorna detalles del usuario

#### El http://localhost:8085/user/login de tipo POST, se le enviara un cuerpo, por ejemplo:

        {
            "email": "jguerrero98developer@gmail.cl",
            "password": "aA1@dddax"
        }

- Donde retornara el accessToken como refreshToken

#### El http://localhost:8085/user/findById/{userId} de tipo GET

- En vez de {userId}, se enviara el id que genero el usuario al momento de crearse

- Es necesario enviar el token con Authorization Bearer Token que se genera al registrar el usuario o en el login

- Retorna detalles del usuario

#### El http://localhost:8085/user/deleteUser/{userId} de tipo DELETE

- En vez de {userId}, se enviara el id que genero el usuario al momento de crearse

- Es necesario enviar el token con Authorization Bearer Token que se genera al registrar el usuario o en el login

- Retorna un mensaje satisfactorio de usuario eliminado

#### El http://localhost:8085/user/refreshToken de tipo POST, se le enviara un cuerpo, por ejemplo:

        {
            "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqZ3VlcnJlcm85OGRldmVsb3BlcjJAZ21haWwuY2wiLCJleHAiOjE3MTQ5ODc3MjQsIm5hbWUiOiJKb3NlIENhcmxvcyJ9.YyRM9K10Dg3KGz5hJ7N4OE0v5SNEFMXozlqOaUcKp5k"
        }

- Hace que genere un nuevo token para poder ser usado en los endpoints necesarios

- Este endpoint retornara un nuevo accessToken y refreshToken

#### Para mas detalles revisar el swagger
