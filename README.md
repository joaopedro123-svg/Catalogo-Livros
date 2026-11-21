# 📚 API Catálogo de Livros

API REST desenvolvida com **Java 21** e **Spring Boot** para gerenciamento de um catálogo de livros, com persistência em banco de dados **PostgreSQL**, testes unitários automatizados com **JUnit e Mockito**, empacotamento executável (`.jar`), containerização via **Docker e Docker Compose** (com volume persistente) e monitoramento de saúde com **Spring Boot Actuator**.

---

## 🚀 Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 4.1.2**
- **Spring Web MVC** (API REST)
- **Spring Data JPA / Hibernate** (Persistência ORM)
- **PostgreSQL 16** (Banco de dados relacional)
- **Spring Boot Actuator** (Observabilidade e Health Check)
- **JUnit & Mockito** (Testes unitários)
- **Maven Wrapper (`mvnw`)** (Gerenciamento de dependências e build)
- **Docker & Docker Compose** (Containerização e orquestração)

---

## 📁 Estrutura do Projeto

```text
catalogo-livros/
├── src/
│   ├── main/
│   │   ├── java/com/atividade/catalogo_livros/
│   │   │   ├── controller/     # Endpoints REST (/api/livros)
│   │   │   ├── exception/      # Exceções personalizadas e GlobalExceptionHandler
│   │   │   ├── model/          # Entidade JPA (Livro)
│   │   │   ├── repository/     # Interface Spring Data JPA (LivroRepository)
│   │   │   ├── service/        # Regras de negócio, validações e logs (LivroService)
│   │   │   └── CatalogoLivrosApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/atividade/catalogo_livros/
│           └── service/
│               └── LivroServiceTest.java  # Testes unitários (5 cenários)
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

## ⚙️ Pré-requisitos

- **Java JDK 21** instalado (para build e execução local do `.jar`)
- **Docker** e **Docker Compose** (Docker Desktop no Windows/macOS)
- **Git**

---

## 🔐 Variáveis de Ambiente

A aplicação evita credenciais fixas no código utilizando variáveis de ambiente para conexão com o PostgreSQL:

| Variável | Descrição | Valor Padrão (Docker Compose) |
| :--- | :--- | :--- |
| `SPRING_DATASOURCE_URL` | URL JDBC do PostgreSQL | `jdbc:postgresql://db:5432/catalogo_livros` |
| `SPRING_DATASOURCE_USERNAME` | Usuário do banco de dados | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | Senha do banco de dados | `postgres_password` |

---

## 🧪 Executando os Testes Unitários

Os testes unitários da camada de serviço (`LivroServiceTest`) validam as regras de negócio de forma isolada com **JUnit** e **Mockito** (sem necessidade de banco de dados ativo), cobrindo cadastro com sucesso, validação de ISBN duplicado, número de páginas inválido e busca por ID inexistente.

No terminal (na raiz do projeto):

**Windows (PowerShell):**
```powershell
.\mvnw.cmd test
```

**Linux / macOS:**
```bash
./mvnw test
```

Resultado esperado: `Tests run: 5, Failures: 0, Errors: 0, Skipped: 0` e `BUILD SUCCESS`.

---

## 📦 Build e Execução do `.jar` fora da IDE

### 1. Gerar o artefato `.jar`
```powershell
.\mvnw.cmd clean package
```
O arquivo executável será gerado em `target/catalogo-livros-0.0.1-SNAPSHOT.jar`.

### 2. Subir apenas o banco PostgreSQL (caso não tenha PostgreSQL local)
```powershell
docker compose up -d db
docker stop catalogo-livros-api
```

### 3. Executar o `.jar` com variáveis de ambiente

**Windows (PowerShell):**
```powershell
$env:SPRING_DATASOURCE_URL="jdbc:postgresql://127.0.0.1:5432/catalogo_livros"
$env:SPRING_DATASOURCE_USERNAME="postgres"
$env:SPRING_DATASOURCE_PASSWORD="postgres_password"
java -jar target/catalogo-livros-0.0.1-SNAPSHOT.jar
```

**Linux / macOS:**
```bash
export SPRING_DATASOURCE_URL="jdbc:postgresql://127.0.0.1:5432/catalogo_livros"
export SPRING_DATASOURCE_USERNAME="postgres"
export SPRING_DATASOURCE_PASSWORD="postgres_password"
java -jar target/catalogo-livros-0.0.1-SNAPSHOT.jar
```

A aplicação ficará disponível em `http://localhost:8080`.

---

## 🐳 Executando com Docker e Docker Compose

O ambiente completo sobe dois containers orquestrados:
- **`catalogo-livros-db`**: PostgreSQL 16 com `healthcheck` e volume persistente (`pgdata`).
- **`catalogo-livros-api`**: API Spring Boot empacotada sobre `eclipse-temurin:21-jre-alpine`, aguardando o banco ficar `Healthy`.

### 1. Gerar o `.jar` e subir os containers
```powershell
.\mvnw.cmd clean package
docker compose up -d --build
```

### 2. Verificar se os containers estão em execução
```powershell
docker ps
```

### 3. Acompanhar logs (opcional)
```powershell
docker compose logs -f api
```

---

## 💾 Validação da Persistência de Dados (Volume Docker)

O `docker-compose.yml` mapeia o volume nomeado `pgdata` para `/var/lib/postgresql/data`, garantindo que os registros não sejam perdidos quando os containers forem recriados.

Para validar:
1. Cadastre um livro via `POST /api/livros`.
2. Derrube e recrie os containers (sem a flag `-v`):
   ```powershell
   docker compose down
   docker compose up -d
   ```
3. Consulte novamente `GET /api/livros` e confirme que os livros cadastrados continuam salvos.

> Para encerrar os containers e **apagar** também o volume de dados, utilize `docker compose down -v`.

---

## 📡 Endpoints da API

URL base: `http://localhost:8080`

| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `POST` | `/api/livros` | Cadastra um novo livro |
| `GET` | `/api/livros` | Lista todos os livros cadastrados |
| `GET` | `/api/livros/{id}` | Busca um livro pelo ID |
| `PUT` | `/api/livros/{id}` | Atualiza os dados de um livro |
| `DELETE` | `/api/livros/{id}` | Remove um livro pelo ID |
| `GET` | `/actuator/health` | Verifica a saúde da aplicação |

### Exemplos de Requisições (PowerShell)

**Cadastrar livro (`POST`):**
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/livros" `
  -Method Post `
  -ContentType "application/json" `
  -Body '{"titulo":"Clean Code","autor":"Robert C. Martin","isbn":"978-0132350884","anoPublicacao":2008,"paginas":464}'
```

**Listar todos os livros (`GET`):**
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/livros" -Method Get
```

**Buscar por ID (`GET`):**
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/livros/1" -Method Get
```

**Atualizar livro (`PUT`):**
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/livros/1" `
  -Method Put `
  -ContentType "application/json" `
  -Body '{"titulo":"Clean Code (Edição Revisada)","autor":"Robert C. Martin","isbn":"978-0132350884","anoPublicacao":2008,"paginas":464}'
```

**Remover livro (`DELETE`):**
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/livros/1" -Method Delete
```

### Tratamento de Erros
A API possui tratamento centralizado via `GlobalExceptionHandler`, retornando respostas JSON padronizadas (`timestamp`, `status` e `erro`) para:
- **`400 Bad Request`**: regras de negócio violadas (ex.: ISBN duplicado ou número de páginas menor ou igual a zero).
- **`404 Not Found`**: recurso não encontrado para o ID informado.

---

## 🩺 Observabilidade (Spring Boot Actuator)

Para verificar o status de saúde da API em execução:

**Navegador ou PowerShell:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/actuator/health"
```

**Resposta esperada (`200 OK`):**
```json
{
  "status": "UP"
}
```
