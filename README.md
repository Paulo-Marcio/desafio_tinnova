# Desafio Backend – Tinnova

API REST para gerenciamento de veículos, desenvolvida em Java com Spring Boot.

---

## Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- Spring Validation
- Spring Cache
- H2 Database
- Swagger / OpenAPI
- Maven

---

### Executar a aplicação


mvn clean spring-boot:run
A aplicação será iniciada em:

http://localhost:8080
Banco de Dados
Banco em memória H2.

Acessar o console:
http://localhost:8080/h2-console
Configurações:

JDBC URL: jdbc:h2:mem:testdb

User: sa

Password: (em branco)

Documentação da API (Swagger)
Após subir a aplicação, acesse:


http://localhost:8080/swagger-ui.html
Segurança
A API utiliza autenticação simples via header Authorization.

Tokens disponíveis:
Token	Perfil
admin-token	ADMIN
user-token	USER

Exemplo de header:

Authorization: admin-token
Funcionalidades
CRUD de veículos

Filtros combinados por marca, ano, cor e faixa de preço

Paginação e ordenação

Soft delete (remoção lógica)

Validação de placa única

Conversão de preço para dólar

Cotação do dólar via API externa

Fallback automático de cotação

Cache de cotação