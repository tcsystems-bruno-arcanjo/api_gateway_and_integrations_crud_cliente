# CRUD Clientes

## Pré-requisitos

- Maven
- Java 11

## Inicialização do projeto

Na raiz do projeto, utilize o plugin do Maven spring-boot para iniciá-lo:

> mvn spring-boot:run

## Documentação

Toda documentação da API pode ser encontrada no endereço http://localhost:8080/swagger-ui/index.html

## Segurança

Os serviços do Cliente estão protegidos. Para testá-los utilize o serviço do Login para se autenticar e receber o Token de autorização.

O usuário padrão é `admin` e senha `admin`;

## Banco de dados

O banco de dados pode ser acessado a partir do endereço http://localhost:8080/h2-console

Para se conectar utilize os dados abaixo:
> JDBC URL: jdbc:h2:mem:testdb
> 
> User Name: sa
> 
> Password:

## Nota
Por padrão o cabeçalho "Authorization" está desabilitado no OpenApi e por isso foi utilizado o cabeçalho "X-Authorization" em seu lugar.