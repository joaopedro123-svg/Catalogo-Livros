# Catálogo de Livros — API Spring Boot

API REST desenvolvida em **Java 21** com **Spring Boot** para gerenciamento de um catálogo de livros, aplicando conceitos de testes automatizados (JUnit 5 e Mockito), empacotamento em JAR, configuração externa por variáveis de ambiente, containerização com Docker/Docker Compose, logs e observabilidade com Spring Boot Actuator.

---

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot** (Web, Data JPA, Actuator)
- **PostgreSQL**
- **JUnit 5 & Mockito** (testes unitários)
- **Maven** (gerenciamento de dependências e build)
- **Docker & Docker Compose**

---

## Estrutura do Projeto

```text
catalogo-livros/
├── src/
│   ├── main/
│   │   ├── java/com/atividade/catalogo_livros/
│   │   │   ├── controller/     # Endpoints REST (LivroController)
│   │   │   ├── exception/      # Exceções personalizadas e GlobalExceptionHandler
│   │   │   ├── model/          # Entidade JPA (Livro)
│   │   │   ├── repository/     # Acesso a dados com Spring Data JPA (LivroRepository)
│   │   │   └── service/        # Regras de negócio e logs (LivroService)
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/atividade/catalogo_livros/
│           └── service/        # Testes unitários do Service (LivroServiceTest)
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

## Endpoints da API

### Livros (`/api/livros`)

| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `POST` | `/api/livros` | Cadastra um novo livro |
| `GET` | `/api/livros` | Lista todos os livros cadastrados |
| `GET` | `/api/livros/{id}` | Busca um livro pelo identificador |
| `PUT` | `/api/livros/{id}` | Atualiza os dados de um livro existente |
| `DELETE` | `/api/livros/{id}` | Exclui um livro pelo identificador |

**Exemplo de corpo JSON (`POST` / `PUT`):**

```json
{
  "titulo": "Dom Casmurro",
  "autor": "Machado de Assis",
  "isbn": "978-8535914849",
  "anoPublicacao": 1899,
  "paginas": 256
}
```

### Observabilidade (Actuator)

| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `GET` | `/actuator/health` | Verifica a saúde da aplicação (`{"status":"UP"}`) |

---

## Configuração por Variáveis de Ambiente

As credenciais e a URL de conexão com o banco de dados não ficam fixas em produção, sendo lidas a partir das seguintes variáveis de ambiente (com valores padrão apenas para desenvolvimento local):

| Variável | Descrição | Valor Padrão (Local) |
| :--- | :--- | :--- |
| `DB_URL` | URL JDBC do PostgreSQL | `jdbc:postgresql://localhost:5432/livros_db` |
| `DB_USERNAME` | Usuário do banco de dados | `postgres` |
| `DB_PASSWORD` | Senha do banco de dados | `postgres` |

---

## Como Executar o Projeto

### Pré-requisitos
- **Java 21** instalado (para execução local e geração do JAR)
- **Docker** e **Docker Compose** instalados (para execução em containers)

### 1. Clonar o repositório

```bash
git clone https://github.com/joaopedro123-svg/Catalogo-Livros.git
cd Catalogo-Livros
```

### 2. Executar os testes automatizados

No **Windows (PowerShell/CMD)**:
```powershell
.\mvnw.cmd test
```

No **Linux/macOS**:
```bash
./mvnw test
```

### 3. Gerar o arquivo JAR (Build)

No **Windows (PowerShell/CMD)**:
```powershell
.\mvnw.cmd clean package
```

No **Linux/macOS**:
```bash
./mvnw clean package
```

O arquivo executável será gerado dentro da pasta `target/` (ex.: `target/catalogo-livros-0.0.1-SNAPSHOT.jar`).

### 4. Executar o JAR fora da IDE

Certifique-se de que há um PostgreSQL ativo e configurado, defina as variáveis de ambiente e execute o JAR diretamente pelo terminal:

**Windows (PowerShell):**
```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/livros_db"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="sua_senha"
java -jar target/catalogo-livros-0.0.1-SNAPSHOT.jar
```

**Linux/macOS:**
```bash
export DB_URL="jdbc:postgresql://localhost:5432/livros_db"
export DB_USERNAME="postgres"
export DB_PASSWORD="sua_senha"
java -jar target/catalogo-livros-0.0.1-SNAPSHOT.jar
```

### 5. Executar com Docker e Docker Compose (API + PostgreSQL)

1. Gere o JAR da aplicação (passo 3).
2. Suba os containers da API e do PostgreSQL (com volume persistente):

```bash
docker compose up --build
```

3. Verifique se a aplicação está saudável acessando no navegador ou via `curl`:

```bash
curl http://localhost:8080/actuator/health
```

Resposta esperada:
```json
{"status":"UP"}
```

4. Para encerrar os containers mantendo os dados salvos no volume do PostgreSQL:

```bash
docker compose down
```