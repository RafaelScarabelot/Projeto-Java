# Sistema de Gerenciamento de Portfólio de Projetos

Sistema desenvolvido em Java com Spring Boot para gerenciar o portfólio de projetos de uma empresa, permitindo o acompanhamento completo do ciclo de vida de cada projeto.

## 🚀 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.5.7**
- **Spring Data JPA**
- **Spring Security**
- **PostgreSQL**
- **Swagger/OpenAPI**
- **JaCoCo** (cobertura de testes)
- **Maven**

## 📋 Funcionalidades

### Projetos
- CRUD completo de projetos
- Classificação de risco automática (Baixo/Médio/Alto)
- Controle de status com transições lógicas
- Paginação e filtros na listagem
- Validações de regras de negócio

### Membros
- API mockada para gerenciamento de membros
- Alocação de membros em projetos
- Controle de limite (1-10 membros por projeto)
- Máximo 3 projetos ativos por membro

### Relatórios
- Relatório de portfólio com estatísticas
- Quantidade de projetos por status
- Total orçado por status
- Média de duração dos projetos encerrados

## 🏗️ Arquitetura

```
src/
├── main/java/com/projeto/projeto/
│   ├── controller/     # Camada de apresentação
│   ├── service/        # Regras de negócio
│   ├── repository/     # Acesso a dados
│   ├── entity/         # Entidades JPA
│   ├── dtos/           # Data Transfer Objects
│   ├── enums/          # Enumerações
│   ├── exception/      # Tratamento de exceções
│   ├── mapper/         # Mapeamento entre entidades e DTOs
│   ├── config/         # Configurações
│   └── security/       # Configurações de segurança
└── test/               # Testes unitários
```

## ⚙️ Configuração e Execução

### Pré-requisitos
- Java 17+
- Maven 3.6+
- PostgreSQL 12+

### 1. Configurar Banco de Dados

Criar banco PostgreSQL:
```sql
CREATE DATABASE projeto;
```

### 2. Configurar application.properties

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/projeto
spring.datasource.username=seu_username
spring.datasource.password=sua_senha
```

### 3. Executar a Aplicação

```bash
# Compilar
mvn clean compile

# Executar testes
mvn test

# Executar aplicação
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

## 📚 Documentação da API

### Swagger UI
Acesse: `http://localhost:8080/swagger-ui/index.html`

**Credenciais:**
- Usuário: `admin`
- Senha: `1234`

### Principais Endpoints

#### Projetos
- `GET /api/projetos` - Listar projetos (com paginação e filtros)
- `POST /api/projetos` - Criar projeto
- `GET /api/projetos/{id}` - Buscar projeto por ID
- `PUT /api/projetos/{id}` - Atualizar projeto
- `DELETE /api/projetos/{id}` - Excluir projeto
- `PATCH /api/projetos/{id}/status` - Atualizar status

#### Membros (API Mockada)
- `GET /api/membros` - Listar membros
- `POST /api/membros` - Criar membro
- `GET /api/membros/{id}` - Buscar membro
- `PUT /api/membros/{id}` - Editar membro
- `DELETE /api/membros/{id}` - Deletar membro

#### Alocação
- `POST /projetos/{projetoId}/membros/{membroId}` - Alocar membro
- `DELETE /projetos/{projetoId}/membros/{membroId}` - Desalocar membro
- `PUT /projetos/{projetoId}/membros/{membroId}` - Editar alocação
- `GET /projetos/{projetoId}/membros` - Listar membros do projeto

#### Relatórios
- `GET /api/portfolios/relatorio` - Relatório do portfólio

## 🧪 Testes

### Executar Testes
```bash
mvn test
```

### Relatório de Cobertura (JaCoCo)
```bash
mvn test
# Abrir: target/site/jacoco/index.html
```

## 📊 Regras de Negócio

### Status dos Projetos
Sequência obrigatória:
```
EM_ANALISE → ANALISE_REALIZADA → ANALISE_APROVADA → 
INICIADO → PLANEJADO → EM_ANDAMENTO → ENCERRADO
```
- `CANCELADO` pode ser aplicado a qualquer momento

### Classificação de Risco
- **Baixo**: Orçamento ≤ R$ 100.000 E prazo ≤ 3 meses
- **Médio**: Orçamento R$ 100.001-500.000 OU prazo 3-6 meses  
- **Alto**: Orçamento > R$ 500.000 OU prazo > 6 meses

### Restrições
- Projetos INICIADO/EM_ANDAMENTO/ENCERRADO não podem ser excluídos
- Apenas funcionários podem ser alocados em projetos
- 1-10 membros por projeto
- Máximo 3 projetos ativos por membro

## 🔒 Segurança

- Spring Security configurado
- Autenticação básica em memória
- Usuário: `admin` / Senha: `1234`

## 📁 Estrutura do Banco

### Principais Tabelas
- `projetos` - Dados dos projetos
- `membros` - Informações dos membros (mockado)
- `alocacoes` - Relacionamento projeto-membro

## 🚀 Deploy

Para ambiente de produção, ajustar:
- Configurações de banco no `application-prod.properties`
- Configurações de segurança
- Variáveis de ambiente

---

**Desenvolvido por:** Rafael Carlos Scarabelot