# Sistema de Gerenciamento de Portfólio de Projetos

Sistema robusto desenvolvido em Java com Spring Boot para gerenciar o portfólio de projetos de uma empresa, permitindo o acompanhamento completo do ciclo de vida de cada projeto, desde a análise de viabilidade até a finalização.

## 🚀 Tecnologias Utilizadas

- **Java 17** - Linguagem de programação
- **Spring Boot 3.5.7** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Segurança e autenticação
- **PostgreSQL** - Banco de dados principal
- **H2 Database** - Banco para testes
- **Swagger/OpenAPI** - Documentação da API
- **JaCoCo** - Relatórios de cobertura de testes
- **JUnit 5 + Mockito** - Testes unitários e de integração
- **Maven** - Gerenciamento de dependências

## 📋 Funcionalidades Principais

### 🎯 Gestão de Projetos
- **CRUD Completo**: Criar, listar, atualizar e excluir projetos
- **Classificação de Risco Automática**: 
  - Baixo: ≤ R$ 100.000 E ≤ 3 meses
  - Médio: R$ 100.001-500.000 OU 3-6 meses
  - Alto: > R$ 500.000 OU > 6 meses
- **Controle de Status**: Transições lógicas obrigatórias
- **Paginação e Filtros**: Busca por nome e status
- **Validações Rigorosas**: Regras de negócio implementadas

### 👥 Gestão de Membros
- **API Externa Mockada**: Simulação de sistema de RH
- **Alocação Inteligente**: Controle de capacidade
- **Limites Configurados**: 1-10 membros por projeto
- **Restrições**: Máximo 3 projetos ativos por funcionário
- **Validação de Cargo**: Apenas funcionários podem ser alocados

### 📊 Relatórios Gerenciais
- **Dashboard Completo**: Visão geral do portfólio
- **Métricas por Status**: Quantidade e orçamento
- **Análise Temporal**: Média de duração dos projetos
- **Recursos Humanos**: Total de membros únicos alocados

## 🏗️ Arquitetura do Sistema

O projeto segue os princípios de **Clean Architecture** e **SOLID**, com separação clara de responsabilidades:

```
src/
├── main/java/com/projeto/projeto/
│   ├── controller/     # 🎮 Camada de Apresentação (REST Controllers)
│   ├── service/        # 💼 Regras de Negócio (Business Logic)
│   ├── repository/     # 🗄️ Acesso a Dados (Data Access Layer)
│   ├── entity/         # 📋 Entidades JPA (Domain Models)
│   ├── dtos/           # 📦 Data Transfer Objects
│   ├── enums/          # 🏷️ Enumerações (Status, Atribuições)
│   ├── exception/      # ⚠️ Tratamento Global de Exceções
│   ├── mapper/         # 🔄 Mapeamento Entity ↔ DTO
│   ├── config/         # ⚙️ Configurações (Swagger, Beans)
│   └── security/       # 🔐 Configurações de Segurança
└── test/
    ├── serviceTest/    # 🧪 Testes Unitários (86% cobertura)
    └── controllerTest/ # 🔗 Testes de Integração (100% cobertura)
```

## ⚙️ Configuração e Execução

### 📋 Pré-requisitos
- **Java 17+** (JDK instalado e configurado)
- **Maven 3.6+** (para gerenciamento de dependências)
- **PostgreSQL 12+** (banco de dados principal)
- **Git** (para clonar o repositório)

### 🗄️ 1. Configurar Banco de Dados

**Criar banco PostgreSQL:**
```sql
CREATE DATABASE projeto;
CREATE USER projeto_user WITH PASSWORD 'projeto_pass';
GRANT ALL PRIVILEGES ON DATABASE projeto TO projeto_user;
```

### ⚙️ 2. Configurar application.properties

**Editar:** `src/main/resources/application.properties`
```properties
# Configuração do Banco
spring.datasource.url=jdbc:postgresql://localhost:5432/projeto
spring.datasource.username=projeto_user
spring.datasource.password=projeto_pass

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Porta da aplicação
server.port=8080
```

### 🚀 3. Executar a Aplicação

```bash
# 1. Clonar o repositório
git clone <url-do-repositorio>
cd projeto

# 2. Compilar o projeto
mvn clean compile

# 3. Executar testes (86% cobertura)
mvn test

# 4. Gerar relatório de cobertura
mvn test jacoco:report

# 5. Executar a aplicação
mvn spring-boot:run
```

**🌐 Aplicação disponível em:** `http://localhost:8080`

## 📚 Documentação da API

### 📖 Swagger UI Interativo
**Acesse:** `http://localhost:8080/swagger-ui/index.html`

**🔐 Credenciais de Acesso:**
- **Usuário:** `admin`
- **Senha:** `1234`

### 🛠️ Principais Endpoints

#### 📋 Gestão de Projetos
| Método | Endpoint | Descrição |
|--------|----------|----------|
| `GET` | `/api/projetos` | Listar projetos (paginação + filtros) |
| `POST` | `/api/projetos` | Criar novo projeto |
| `GET` | `/api/projetos/{id}` | Buscar projeto específico |
| `PUT` | `/api/projetos/{id}` | Atualizar projeto completo |
| `DELETE` | `/api/projetos/{id}` | Excluir projeto |
| `PATCH` | `/api/projetos/{id}/status` | Atualizar apenas status |

#### 👥 API de Membros (Mockada)
| Método | Endpoint | Descrição |
|--------|----------|----------|
| `GET` | `/api/membros` | Listar todos os membros |
| `POST` | `/api/membros` | Criar novo membro |
| `GET` | `/api/membros/{id}` | Buscar membro específico |
| `PUT` | `/api/membros/{id}` | Atualizar dados do membro |
| `DELETE` | `/api/membros/{id}` | Remover membro |

#### 🔗 Gestão de Alocações
| Método | Endpoint | Descrição |
|--------|----------|----------|
| `POST` | `/projetos/{projetoId}/membros/{membroId}` | Alocar membro ao projeto |
| `DELETE` | `/projetos/{projetoId}/membros/{membroId}` | Desalocar membro |
| `PUT` | `/projetos/{projetoId}/membros/{membroId}` | Editar alocação |
| `GET` | `/projetos/{projetoId}/membros` | Listar membros do projeto |

#### 📊 Relatórios Gerenciais
| Método | Endpoint | Descrição |
|--------|----------|----------|
| `GET` | `/api/portfolios/relatorio` | Relatório completo do portfólio |

## 🧪 Estratégia de Testes

### 📊 Cobertura Atual
- **Cobertura Geral:** 84% do projeto
- **Services (Regras de Negócio):** 86% ✅
- **Controllers:** 100% ✅
- **Total de Testes:** 45+ cenários

### 🔬 Tipos de Teste

**🧪 Testes Unitários (Services)**
```bash
# Executar apenas testes unitários
mvn test -Dtest="*ServiceTest"
```
- Testam lógica de negócio isoladamente
- Usam mocks para dependências
- Cobertura: 86% das regras de negócio

**🔗 Testes de Integração (Controllers)**
```bash
# Executar testes de integração
mvn test -Dtest="*ControllerTest"
```
- Testam endpoints completos
- Usam banco H2 em memória
- Cobertura: 100% dos controllers

### 📈 Relatórios de Cobertura
```bash
# Gerar relatório completo
mvn clean test jacoco:report

# Visualizar relatório
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

## 🔒 Segurança e Autenticação

### 🛡️ Configuração de Segurança
- **Framework:** Spring Security
- **Tipo:** Autenticação HTTP Basic
- **Armazenamento:** Em memória (para demonstração)

### 👤 Credenciais de Acesso
- **Usuário:** `admin`
- **Senha:** `1234`
- **Perfil:** Acesso total à API

### 🔐 Endpoints Protegidos
Todos os endpoints da API requerem autenticação básica.

## 🗄️ Estrutura do Banco de Dados

### 📋 Principais Tabelas

**`projetos`** - Dados principais dos projetos
- `id`, `nome`, `data_inicio`, `previsao_termino`
- `data_real_termino`, `orcamento_total`, `descricao`
- `gerente_id`, `status_do_projeto`

**`membros`** - Informações dos membros (API mockada)
- `id`, `nome`, `atribuicao_membro`

**`alocacoes`** - Relacionamento projeto-membro
- `id`, `projeto_id`, `membro_id`, `data_alocacao`

### 🔗 Relacionamentos
- Projeto → Alocações (1:N)
- Membro → Alocações (1:N)
- Projeto → Gerente (N:1)

## 🚀 Deploy e Produção

### 📋 Checklist para Produção
- [ ] Configurar `application-prod.properties`
- [ ] Ajustar credenciais de banco
- [ ] Configurar variáveis de ambiente
- [ ] Implementar autenticação JWT
- [ ] Configurar logs estruturados
- [ ] Adicionar monitoramento (Actuator)

### 🔧 Variáveis de Ambiente
```bash
DB_URL=jdbc:postgresql://prod-server:5432/projeto
DB_USERNAME=${DB_USER}
DB_PASSWORD=${DB_PASS}
SECURITY_USER=${ADMIN_USER}
SECURITY_PASSWORD=${ADMIN_PASS}
```

## ⚠️ Observações Técnicas

### 📝 Sobre @MockBean Deprecated

**Aviso:** Nos testes de integração, utilizei `@MockBean` que está marcado como **deprecated** a partir do Spring Boot 3.4.0.

**Por que mantive:**
- ✅ **Funcionalidade:** Continua funcionando perfeitamente
- ✅ **Praticidade:** Permite testes rápidos sem levantar toda a aplicação
- ✅ **Isolamento:** Simula dependências externas (API de membros)
- ✅ **Performance:** Testes executam mais rapidamente

**Impacto:** ⚠️ **NENHUM** - É apenas um aviso de depreciação, não afeta o funcionamento do código.

**Alternativa futura:** Migrar para `@TestConfiguration` com beans customizados quando necessário.

## 🎯 Conclusão

Sistema completo que atende **100% dos requisitos** solicitados:
- ✅ Arquitetura MVC bem estruturada
- ✅ Regras de negócio implementadas
- ✅ API REST documentada
- ✅ Testes abrangentes (86% cobertura)
- ✅ Segurança configurada
- ✅ Tratamento de exceções
- ✅ Paginação e filtros
- ✅ Relatórios gerenciais

---

**💻 Desenvolvido por:** Rafael Carlos Scarabelot