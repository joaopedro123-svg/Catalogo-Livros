Catálogo de Livros — API Spring BootAPI REST desenvolvida em Java 21 com Spring Boot para gerenciamento de um catálogo de livros, aplicando conceitos de testes automatizados (JUnit 5 e Mockito), empacotamento em JAR, configuração externa por variáveis de ambiente, containerização com Docker/Docker Compose, logs e observabilidade com Spring Boot Actuator.Tecnologias UtilizadasJava 21Spring Boot (Web, Data JPA, Actuator)PostgreSQLJUnit 5 & Mockito (testes unitários)Maven (gerenciamento de dependências e build)Docker & Docker ComposeEstrutura do Projetocatalogo-livros/
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
Endpoints da APILivros (/api/livros)MétodoRotaDescriçãoPOST/api/livrosCadastra um novo livroGET/api/livrosLista todos os livros cadastradosGET/api/livros/{id}Busca um livro pelo identificadorPUT/api/livros/{id}Atualiza os dados de um livro existenteDELETE/api/livros/{id}Exclui um livro pelo identificadorExemplo de corpo JSON (POST / PUT):{
  "titulo": "Dom Casmurro",
  "autor": "Machado de Assis",
  "isbn": "978-8535914849",
  "anoPublicacao": 1899,
  "paginas": 256
}
Observabilidade (Actuator)MétodoRotaDescriçãoGET/actuator/healthVerifica a saúde da aplicação ({"status":"UP"})Configuração por Variáveis de AmbienteAs credenciais e a URL de conexão com o banco de dados não ficam fixas em produção, sendo lidas a partir das seguintes variáveis de ambiente (com valores padrão apenas para desenvolvimento local):VariávelDescriçãoValor Padrão (Local)DB_URLURL JDBC do PostgreSQLjdbc:postgresql://localhost:5432/livros_dbDB_USERNAMEUsuário do banco de dadospostgresDB_PASSWORDSenha do banco de dadospostgresComo Executar o ProjetoPré-requisitosJava 21 instalado (para execução local e geração do JAR)Docker e Docker Compose instalados (para execução em containers)1. Clonar o repositóriogit clone https://github.com/joaopedro123-svg/Catalogo-Livros.git
cd Catalogo-Livros
2. Executar os testes automatizadosNo Windows (PowerShell/CMD):.\mvnw.cmd test
No Linux/macOS:./mvnw test
3. Gerar o arquivo JAR (Build)No Windows (PowerShell/CMD):.\mvnw.cmd clean package
No Linux/macOS:./mvnw clean package
O arquivo executável será gerado dentro da pasta target/ (ex.: target/catalogo-livros-0.0.1-SNAPSHOT.jar).4. Executar o JAR fora da IDECertifique-se de que há um PostgreSQL ativo e configurado, defina as variáveis de ambiente e execute o JAR diretamente pelo terminal:Windows (PowerShell):$env:DB_URL="jdbc:postgresql://localhost:5432/livros_db"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="sua_senha"
java -jar target/catalogo-livros-0.0.1-SNAPSHOT.jar
Linux/macOS:export DB_URL="jdbc:postgresql://localhost:5432/livros_db"
export DB_USERNAME="postgres"
export DB_PASSWORD="sua_senha"
java -jar target/catalogo-livros-0.0.1-SNAPSHOT.jar
5. Executar com Docker e Docker Compose (API + PostgreSQL)Gere o JAR da aplicação (passo 3).Suba os containers da API e do PostgreSQL (com volume persistente):docker compose up --build
Verifique se a aplicação está saudável acessando no navegador ou via curl:curl http://localhost:8080/actuator/health
Resposta esperada:{"status":"UP"}
Para encerrar os containers mantendo os dados salvos no volume do PostgreSQL:docker compose down
