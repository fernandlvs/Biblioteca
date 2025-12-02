# Biblioteca API - Sistema de Gerenciamento de Biblioteca

API REST desenvolvida em Java com Spring Boot para gerenciamento de biblioteca acadêmica. Projeto da disciplina de Banco de Dados - Fase 2.

**Autores:** Fernanda Alves, Ana Gusmão, Amanda Lanay  
**Versão:** 1.0


## Sobre o Projeto

Sistema completo de gerenciamento de biblioteca que inclui:

- Cadastro e gestão de livros e usuários
- Controle de empréstimos e devoluções
- Cálculo automático de multas por atraso
- Consulta de empréstimos ativos
- Integração com procedures, functions e views do MySQL



## Tecnologias Utilizadas

- **Java 17+**
- **Spring Boot 3.x**
- **Spring JDBC** (JdbcTemplate)
- **MySQL 8.0+**
- **Maven** (gerenciamento de dependências)
- **Jakarta Validation** (validações)



## Requisitos

Antes de rodar o projeto, certifique-se de ter instalado:

- [Java JDK 17+](https://www.oracle.com/java/technologies/downloads/)
- [MySQL 8.0+](https://dev.mysql.com/downloads/mysql/)
- [Maven 3.6+](https://maven.apache.org/download.cgi) (opcional, pode usar o wrapper incluído)
- Uma IDE Java (IntelliJ IDEA, Eclipse, VSCode, etc.)



## Configuração e Instalação

### 1. Clone o Repositório

```bash
git clone https://github.com/anagdinizg/Biblioteca.git
cd Biblioteca
```

### 2. Configure o Banco de Dados

**2.1. Crie o banco de dados no MySQL:**

```sql
CREATE DATABASE biblioteca_fase_2;
```

**2.2. Execute os scripts SQL da Fase 2** (ordem recomendada):

```bash
# 1. Estrutura (tabelas, constraints)
mysql -u root -p biblioteca_fase_2 < scripts/01_estrutura.sql

# 2. Functions
mysql -u root -p biblioteca_fase_2 < scripts/02_functions.sql

# 3. Procedures
mysql -u root -p biblioteca_fase_2 < scripts/03_procedures.sql

# 4. Triggers
mysql -u root -p biblioteca_fase_2 < scripts/04_triggers.sql

# 5. Views
mysql -u root -p biblioteca_fase_2 < scripts/05_views.sql

# 6. Dados de exemplo (opcional)
mysql -u root -p biblioteca_fase_2 < scripts/06_dados_exemplo.sql
```

### 3. Configure as Credenciais do Banco

Edite o arquivo `src/main/resources/application.properties`:

```properties
# URL do banco de dados
spring.datasource.url=jdbc:mysql://localhost:3306/biblioteca_fase_2?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true

# AJUSTE COM SUAS CREDENCIAIS
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

# Driver do MySQL
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### 4. Compile e Execute o Projeto

**Opção 1: Usando Maven Wrapper (recomendado)**

```bash
# Linux/Mac
./mvnw clean install
./mvnw spring-boot:run

# Windows
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```

**Opção 2: Usando Maven instalado**

```bash
mvn clean install
mvn spring-boot:run
```

**Opção 3: Pela IDE**

1. Importe o projeto como projeto Maven
2. Execute a classe `BibliotecaAplicacao.java`

### 5. Verifique se o Servidor Iniciou

Você verá a mensagem:

```
===========================================
Biblioteca API iniciada com sucesso!
Acesse: http://localhost:8080/api
===========================================
```



## Endpoints da API

### Base URL

```
http://localhost:8080/api
```

### Livros

| Método | Endpoint                    | Descrição                       |
| ------ | --------------------------- | ------------------------------- |
| POST   | `/livros`                   | Criar novo livro                |
| GET    | `/livros`                   | Listar todos os livros          |
| GET    | `/livros/{id}`              | Buscar livro por ID             |
| GET    | `/livros/buscar?titulo=...` | Buscar por título               |
| PUT    | `/livros/{id}`              | Atualizar livro                 |
| DELETE | `/livros/{id}`              | Deletar livro                   |
| GET    | `/livros/{id}/autores`      | Total de autores (chama função) |

**Exemplo - Criar Livro:**

```json
POST /api/livros
{
  "isbn": "978-1234567890",
  "titulo": "Fundamentos de Banco de Dados",
  "anoPublicacao": 2023
}
```

### Usuários

| Método | Endpoint                            | Descrição                                  |
| ------ | ----------------------------------- | ------------------------------------------ |
| POST   | `/usuarios`                         | Criar novo usuário                         |
| GET    | `/usuarios`                         | Listar todos os usuários                   |
| GET    | `/usuarios/{id}`                    | Buscar usuário por ID                      |
| PUT    | `/usuarios/{id}`                    | Atualizar usuário                          |
| DELETE | `/usuarios/{id}`                    | Deletar usuário                            |
| GET    | `/usuarios/{id}/emprestimos-ativos` | Total de empréstimos ativos (chama função) |

**Exemplo - Criar Usuário:**

```json
POST /api/usuarios
{
  "matricula": "2023001",
  "nome": "João Silva",
  "email": "joao@email.com",
  "telefone": "83999999999",
  "cpf": "12345678901"
}
```

### Empréstimos

| Método | Endpoint                     | Descrição                             |
| ------ | ---------------------------- | ------------------------------------- |
| POST   | `/emprestimos/{id}/devolver` | Registrar devolução (chama procedure) |
| GET    | `/emprestimos/ativos`        | Listar empréstimos ativos (usa view)  |
| GET    | `/emprestimos/{id}`          | Buscar empréstimo por ID              |

**Exemplo - Registrar Devolução:**

```json
POST /api/emprestimos/1/devolver
{
  "dataDevolucao": "2025-11-29"
}

// Ou sem body para usar data atual
POST /api/emprestimos/1/devolver
```

**Resposta com multa:**

```json
{
  "sucesso": true,
  "mensagem": "Devolução registrada com sucesso!",
  "idEmprestimo": 1,
  "dataDevolucao": "2025-11-29",
  "multaGerada": true,
  "valorMulta": 15.5,
  "mensagemMulta": "Multa de R$ 15,50 gerada por atraso."
}
```



## Estrutura do Projeto

```
biblioteca-api/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/biblioteca/
│       │       ├── BibliotecaAplicacao.java       # Classe principal
│       │       ├── config/
│       │       │   └── DataBaseConfig.java        # Configuração do banco
│       │       ├── controller/                    # Endpoints REST
│       │       │   ├── LivroController.java
│       │       │   ├── UsuarioController.java
│       │       │   └── EmprestimoController.java
│       │       ├── service/                       # Lógica de negócio
│       │       │   ├── LivroService.java
│       │       │   ├── UsuarioService.java
│       │       │   └── EmprestimoService.java
│       │       ├── repositorio/                   # Acesso ao banco
│       │       │   ├── LivroRepositorio.java
│       │       │   └── UsuarioRepositorio.java
│       │       └── model/                         # Entidades
│       │           ├── Livro.java
│       │           └── Usuario.java
│       └── resources/
│           └── application.properties             # Configurações
├── pom.xml                                        # Dependências Maven
└── README.md
```

### Arquitetura em Camadas

```
Controller (REST) → Service (Lógica) → Repository (Banco) → MySQL
```


## Integração com Banco de Dados

### Procedures Utilizadas

**prc_registrar_devolucao** - Endpoint: `POST /emprestimos/{id}/devolver`

- Atualiza data de devolução
- Libera exemplar (status → 'disponível')
- Calcula e registra multa se houver atraso
- Registra auditoria

### Functions Utilizadas

**fn_contar_autores_livro** - Endpoint: `GET /livros/{id}/autores`

- Retorna quantidade de autores de um livro

**fn_obter_total_emprestimos_ativos** - Endpoint: `GET /usuarios/{id}/emprestimos-ativos`

- Retorna quantidade de empréstimos ativos do usuário

### Views Utilizadas

**vw_emprestimos_ativos** - Endpoint: `GET /emprestimos/ativos`

- Lista todos os empréstimos pendentes com informações detalhadas

### Triggers

As triggers criadas na Fase 2 são **ativadas automaticamente** pelas operações CRUD:

- Logs de auditoria em INSERT/UPDATE/DELETE
- Validações de integridade
- Atualizações em cascata



## Testando a API

### Usando cURL

```bash
# Listar todos os livros
curl http://localhost:8080/api/livros

# Criar um livro
curl -X POST http://localhost:8080/api/livros \
  -H "Content-Type: application/json" \
  -d '{
    "isbn": "978-1234567890",
    "titulo": "Clean Code",
    "anoPublicacao": 2008
  }'

# Buscar livro por ID
curl http://localhost:8080/api/livros/1

# Registrar devolução
curl -X POST http://localhost:8080/api/emprestimos/1/devolver
```

### Usando Postman/Insomnia

1. Importe os endpoints manualmente ou use a collection (se disponível)
2. Configure a base URL: `http://localhost:8080/api`
3. Teste os endpoints conforme documentação acima



## Troubleshooting

### Erro: "Access denied for user"

- Verifique usuário e senha no `application.properties`
- Confirme que o usuário tem permissões no banco `biblioteca_fase_2`

### Erro: "Table doesn't exist"

- Execute os scripts SQL na ordem correta
- Verifique se está conectando no banco correto

### Erro: "Port 8080 already in use"

- Outra aplicação está usando a porta 8080
- Altere a porta em `application.properties`:
  ```properties
  server.port=8081
  ```

### Erro ao chamar procedure/function

- Verifique se os scripts de functions e procedures foram executados
- Confira se o nome está correto no MySQL:
  ```sql
  SHOW FUNCTION STATUS WHERE Db = 'biblioteca_fase_2';
  SHOW PROCEDURE STATUS WHERE Db = 'biblioteca_fase_2';
  ```



## Validações Implementadas

### Livro

- ISBN: obrigatório, máx 30 caracteres, único
- Título: obrigatório, máx 255 caracteres
- Ano: obrigatório, valor positivo

### Usuário

- Matrícula: obrigatória, máx 20 caracteres, única
- Nome: obrigatório, máx 150 caracteres
- Email: formato válido, máx 150 caracteres
- CPF: exatamente 11 caracteres
- Telefone: máx 20 caracteres



## Requisitos da Fase 3 Atendidos

- CRUD completo para 2 entidades (Livro e Usuário)
- Chamada de 1 procedure (prc_registrar_devolucao)
- Chamada de 2 functions (fn_contar_autores_livro, fn_obter_total_emprestimos_ativos)
- Uso de view (vw_emprestimos_ativos)
- Triggers ativadas automaticamente
- Arquivo de configuração centralizado
- Separação de responsabilidades (MVC)
- Documentação completa



## Licença

Projeto acadêmico - Disciplina de Conectar Banco de Dados com POO



**Desenvolvido por Fernanda Alves, Ana Gusmão e Amanda Lanay**
