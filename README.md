# Sistema de Nutrição

![Java](https://img.shields.io/badge/Java-ED8B00?logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?logo=apachemaven&logoColor=white)
![Status](https://img.shields.io/badge/status-em%20desenvolvimento-yellow)

Sistema de apoio à rotina de nutrição, desenvolvido em **Java** e criado em etapas: começa como uma aplicação de terminal em Java puro, para consolidar os fundamentos da linguagem, e evolui para uma aplicação web com **Spring Boot**, **PostgreSQL** e, possivelmente, **React**.

> **Status:** em desenvolvimento. A versão 1 (Java puro, no terminal) está sendo construída. Veja a seção [Progresso](#progresso).

## Sumário

- [Sobre o projeto](#sobre-o-projeto)
- [Progresso](#progresso)
- [Funcionalidades](#funcionalidades)
- [Tecnologias](#tecnologias)
- [Conceitos de Java praticados](#conceitos-de-java-praticados)
- [Arquitetura](#arquitetura)
- [Diagramas](#diagramas)
- [Regras de negócio](#regras-de-negócio)
- [Como executar](#como-executar)
- [Roadmap](#roadmap)
- [Aviso sobre dados](#aviso-sobre-dados)
- [Autor](#autor)
- [Licença](#licença)

## Sobre o projeto

O objetivo é construir, passo a passo, um sistema para cadastrar pacientes, registrar avaliações (peso, altura e nível de atividade física) e calcular indicadores nutricionais, como IMC, taxa metabólica basal (TMB) e gasto energético total (GET).

O projeto tem dois propósitos:

1. **Aprender na prática:** aplicar os conceitos de Java vistos no curso num problema real, e não em exercícios soltos.
2. **Entregar algo útil:** chegar a uma ferramenta que possa ser usada no trabalho de quem atua com nutrição.

A versão 1 foi desenhada para que a maior parte do código possa ser **reaproveitada na migração para o Spring Boot**. Por isso, as regras do sistema são totalmente separadas da entrada e saída de dados: **somente o pacote `console` usa `Scanner` e `System.out`**.

## Progresso

Estado da versão 1:

- [x] Estrutura de pacotes (`dominio`, `calculo`, `excecao`, `repositorio`, `servico`, `console`)
- [x] Enums de sexo, nível de atividade e classificação de IMC
- [x] Exceções próprias
- [ ] `Pessoa`, `Paciente` e `Nutricionista` completos (validações, sexo e id sequencial)
- [ ] `Avaliacao` completa (data, validações de peso e altura, classificação do IMC)
- [ ] Fórmulas de TMB (`MifflinStJeor` e `HarrisBenedict`) e fatores de atividade
- [ ] `PacienteRepositorio` e `PacienteRepositorioMemoria`
- [ ] `PacienteService`
- [ ] `Menu` com todas as opções
- [ ] Testes unitários

## Funcionalidades

**Versão 1 (terminal)**

- [ ] Cadastrar paciente
- [ ] Listar pacientes
- [ ] Buscar paciente por nome
- [ ] Registrar avaliação (peso, altura e nível de atividade)
- [ ] Ver resultados da última avaliação (IMC, classificação, TMB e GET)
- [ ] Ver histórico de avaliações de um paciente

**Fora do escopo da versão 1:** plano alimentar e cardápios, banco de dados, login, relatórios em PDF, atendimento de crianças e idosos.

## Tecnologias

| Etapa | Tecnologias |
|---|---|
| Versão 1 | Java (sem frameworks), Maven, dados em memória |
| Versão 2 | Spring Boot, Spring Data JPA, PostgreSQL |
| Versão 3 | React (a confirmar) |

## Conceitos de Java praticados

- **Operadores:** cálculos de IMC, TMB e GET; validações com `&&` e `||`.
- **Controle de fluxo:** menu com `do while` e `switch`, validações com `if/else`, listagens com `for`, `break` e `continue` em buscas.
- **Encapsulamento:** atributos privados com getters e validação nos setters.
- **Herança:** `Paciente` e `Nutricionista` herdam de `Pessoa`.
- **Abstração:** classe abstrata `Pessoa` e interfaces `FormulaTmb` e `PacienteRepositorio`.
- **Polimorfismo:** diferentes fórmulas de TMB implementam a mesma interface.
- **Exceções:** exceções próprias para dados inválidos e paciente não encontrado, tratadas com `try/catch` no menu.

## Arquitetura

O projeto segue o layout padrão do Maven e é dividido em pacotes por responsabilidade:

```
SistemaNutri/
├── pom.xml
├── README.md
└── src/
    └── main/
        └── java/
            └── nutricao/
                ├── dominio/       Pessoa, Paciente, Nutricionista, Avaliacao,
                │                  Sexo, NivelAtividade, ClassificacaoImc
                ├── calculo/       FormulaTmb, MifflinStJeor, HarrisBenedict
                ├── excecao/       DadoInvalidoException, PacienteNaoEncontradoException
                ├── repositorio/   PacienteRepositorio, PacienteRepositorioMemoria
                ├── servico/       PacienteService
                └── console/       Menu, Main   (único lugar com Scanner e System.out)
```

### Camadas e dependências

```mermaid
flowchart LR
    subgraph apresentacao["Apresentação"]
        C["console<br/>Menu, Main"]
    end
    subgraph regras["Regras do sistema"]
        S["servico<br/>PacienteService"]
        K["calculo<br/>FormulaTmb e fórmulas"]
    end
    subgraph dados["Dados"]
        R["repositorio<br/>PacienteRepositorio"]
    end
    D["dominio<br/>Pessoa, Paciente, Avaliacao e enums"]
    E["excecao<br/>DadoInvalidoException e PacienteNaoEncontradoException"]

    C --> S
    S --> R
    S --> K
    S --> D
    R --> D
    K --> D
    S -.lança.-> E
    D -.lança.-> E
```

Na migração para o Spring Boot, `dominio` vira as entidades, `repositorio` vira Spring Data JPA, `servico` vira classes `@Service`, `console` é substituído por controllers REST e `calculo` continua igual.

## Diagramas

### Diagrama de classes

```mermaid
classDiagram
    class Pessoa {
        <<abstract>>
        -String nome
        -int idade
        +getNome() String
        +setNome(String nome) void
        +getIdade() int
        +setIdade(int idade) void
    }
    class Paciente {
        -int id
        -Sexo sexo
        -List~Avaliacao~ avaliacoes
        +getId() int
        +getSexo() Sexo
        +adicionarAvaliacao(Avaliacao avaliacao) void
        +getAvaliacoes() List~Avaliacao~
        +getUltimaAvaliacao() Avaliacao
    }
    class Nutricionista {
        -String crn
        +getCrn() String
        +setCrn(String crn) void
    }
    class Avaliacao {
        -LocalDate data
        -double peso
        -double altura
        -NivelAtividade nivelAtividade
        +Avaliacao(double peso, double altura, NivelAtividade nivelAtividade)
        +setPeso(double peso) void
        +setAltura(double altura) void
        +calcularImc() double
        +classificarImc() ClassificacaoImc
    }
    class Sexo {
        <<enumeration>>
        FEMININO
        MASCULINO
    }
    class NivelAtividade {
        <<enumeration>>
        SEDENTARIO
        LEVE
        MODERADO
        INTENSO
        MUITO_INTENSO
        +getFator() double
    }
    class ClassificacaoImc {
        <<enumeration>>
        BAIXO_PESO
        EUTROFIA
        SOBREPESO
        OBESIDADE_GRAU_I
        OBESIDADE_GRAU_II
        OBESIDADE_GRAU_III
        +classificar(double imc)$ ClassificacaoImc
    }
    class FormulaTmb {
        <<interface>>
        +calcular(Sexo sexo, int idade, double pesoKg, double alturaCm) double
        +getNome() String
    }
    class MifflinStJeor {
        +calcular(Sexo sexo, int idade, double pesoKg, double alturaCm) double
        +getNome() String
    }
    class HarrisBenedict {
        +calcular(Sexo sexo, int idade, double pesoKg, double alturaCm) double
        +getNome() String
    }
    class PacienteRepositorio {
        <<interface>>
        +salvar(Paciente paciente) Paciente
        +buscarPorId(int id) Paciente
        +buscarPorNome(String nome) List~Paciente~
        +listarTodos() List~Paciente~
    }
    class PacienteRepositorioMemoria {
        -List~Paciente~ pacientes
        -int proximoId
    }
    class PacienteService {
        -PacienteRepositorio repositorio
        +cadastrarPaciente(String nome, int idade, Sexo sexo) Paciente
        +listarPacientes() List~Paciente~
        +buscarPorNome(String nome) List~Paciente~
        +buscarPorId(int id) Paciente
        +registrarAvaliacao(int idPaciente, double peso, double altura, NivelAtividade nivel) Avaliacao
        +calcularImc(int idPaciente) double
        +classificarImc(int idPaciente) ClassificacaoImc
        +calcularTmb(int idPaciente, FormulaTmb formula) double
        +calcularGastoEnergeticoTotal(int idPaciente, FormulaTmb formula) double
    }
    class DadoInvalidoException {
        +DadoInvalidoException(String mensagem)
    }
    class PacienteNaoEncontradoException {
        +PacienteNaoEncontradoException(String mensagem)
    }
    class Menu {
        -Scanner teclado
        -PacienteService servico
        -List~FormulaTmb~ formulas
        +iniciar() void
    }
    class Main {
        +main(String[] args)$ void
    }

    Pessoa <|-- Paciente
    Pessoa <|-- Nutricionista
    Paciente "1" *-- "0..*" Avaliacao : possui
    Paciente --> Sexo
    Avaliacao --> NivelAtividade
    Avaliacao ..> ClassificacaoImc : usa
    FormulaTmb <|.. MifflinStJeor
    FormulaTmb <|.. HarrisBenedict
    PacienteRepositorio <|.. PacienteRepositorioMemoria
    PacienteService --> PacienteRepositorio
    PacienteService ..> FormulaTmb : usa
    PacienteService ..> DadoInvalidoException : lança
    PacienteService ..> PacienteNaoEncontradoException : lança
    Exception <|-- DadoInvalidoException
    Exception <|-- PacienteNaoEncontradoException
    Menu --> PacienteService
    Menu --> FormulaTmb : lista
    Main ..> Menu : cria
```

### Fluxo do menu principal

```mermaid
flowchart TD
    A(["Início"]) --> B["Mostrar o menu"]
    B --> C["Ler a opção"]
    C --> D{"Entrada é um número?"}
    D -- não --> E["Avisar: digite apenas números"]
    E --> B
    D -- sim --> F{"Qual opção?"}
    F -- 1 --> G1["Cadastrar paciente"]
    F -- 2 --> G2["Listar pacientes"]
    F -- 3 --> G3["Buscar paciente por nome"]
    F -- 4 --> G4["Registrar avaliação"]
    F -- 5 --> G5["Ver resultados da última avaliação"]
    F -- 6 --> G6["Ver histórico de avaliações"]
    F -- 0 --> Z(["Encerrar o programa"])
    F -- outra --> H["Avisar: opção inválida"]
    H --> B
    G1 & G2 & G3 & G4 & G5 & G6 --> I["Mostrar resultado ou mensagem de erro"]
    I --> B
```

### Fluxo: cadastrar paciente

```mermaid
flowchart TD
    A(["Opção 1: cadastrar paciente"]) --> B["Ler nome, idade e sexo"]
    B --> C{"Nome preenchido?"}
    C -- não --> X["Erro: nome obrigatório"]
    C -- sim --> D{"Idade entre 18 e 59 anos?"}
    D -- não --> Y["Erro: apenas adultos de 18 a 59 anos"]
    D -- sim --> E{"Sexo informado?"}
    E -- não --> W["Erro: sexo obrigatório"]
    E -- sim --> F["Gerar o id sequencial"]
    F --> G["Salvar no repositório"]
    G --> H["Mostrar o paciente cadastrado"]
    X --> Z(["Voltar ao menu"])
    Y --> Z
    W --> Z
    H --> Z
```

### Fluxo: registrar avaliação

```mermaid
flowchart TD
    A(["Opção 4: registrar avaliação"]) --> B["Informar o id do paciente"]
    B --> C{"Paciente existe?"}
    C -- não --> X["Erro: paciente não encontrado"]
    C -- sim --> D["Ler peso, altura e nível de atividade"]
    D --> E{"Peso maior que 0 e até 500 kg?"}
    E -- não --> Y["Erro: peso inválido"]
    E -- sim --> F{"Altura entre 0,50 e 2,50 m?"}
    F -- não --> W["Erro: altura inválida"]
    F -- sim --> G["Criar a avaliação com a data de hoje"]
    G --> H["Adicionar ao histórico do paciente"]
    H --> I["Confirmar o registro"]
    X --> Z(["Voltar ao menu"])
    Y --> Z
    W --> Z
    I --> Z
```

### Sequência: ver resultados da última avaliação

```mermaid
sequenceDiagram
    actor U as Usuário
    participant M as Menu
    participant S as PacienteService
    participant R as PacienteRepositorio
    participant P as Paciente
    participant A as Avaliacao
    participant F as FormulaTmb

    U->>M: Opção 5, id do paciente e fórmula
    M->>S: calcular resultados (id, fórmula)
    S->>R: buscarPorId(id)
    R-->>S: Paciente
    alt paciente e avaliação encontrados
        S->>P: getUltimaAvaliacao()
        P-->>S: Avaliacao
        S->>A: calcularImc() e classificarImc()
        A-->>S: IMC e classificação
        S->>F: calcular(sexo, idade, peso, altura em cm)
        F-->>S: TMB
        S->>S: GET = TMB × fator de atividade
        S-->>M: IMC, classificação, TMB e GET
        M-->>U: Mostrar os resultados
    else paciente inexistente ou sem avaliação
        S-->>M: lança exceção com a mensagem
        M-->>U: Mostrar o erro
    end
```

### Classificação do IMC

Os limites são contínuos: cada faixa vale até o limite seguinte, sem lacunas entre valores decimais.

```mermaid
flowchart TD
    A(["IMC calculado"]) --> B{"IMC menor que 18,5?"}
    B -- sim --> R1["Baixo peso"]
    B -- não --> C{"IMC menor que 25?"}
    C -- sim --> R2["Eutrofia"]
    C -- não --> D{"IMC menor que 30?"}
    D -- sim --> R3["Sobrepeso"]
    D -- não --> E{"IMC menor que 35?"}
    E -- sim --> R4["Obesidade grau I"]
    E -- não --> F{"IMC menor que 40?"}
    F -- sim --> R5["Obesidade grau II"]
    F -- não --> R6["Obesidade grau III"]
```

### Modelo de dados previsto para a versão 2

Rascunho das tabelas no PostgreSQL. A idade passa a ser calculada a partir da data de nascimento, e o id passa a ser gerado pelo banco.

```mermaid
erDiagram
    PACIENTE ||--o{ AVALIACAO : possui
    PACIENTE {
        bigint id PK
        varchar nome
        date data_nascimento
        varchar sexo
    }
    AVALIACAO {
        bigint id PK
        bigint paciente_id FK
        date data
        numeric peso_kg
        numeric altura_m
        varchar nivel_atividade
    }
```

## Regras de negócio

> As fórmulas e faixas abaixo são referências comuns na nutrição e devem ser validadas por um profissional da área antes de qualquer uso real.

### Cadastro do paciente

- **RN01:** o nome é obrigatório e não pode ser vazio.
- **RN02:** nesta versão, só são aceitos adultos de 18 a 59 anos (limites incluídos). A classificação do IMC para crianças e idosos usa critérios diferentes.
- **RN03:** o sexo é obrigatório (feminino ou masculino), pois as fórmulas de TMB dependem dele.
- **RN04:** cada paciente recebe um id sequencial automático. Nomes repetidos são permitidos.

### Avaliação

- **RN05:** peso em kg, maior que 0 e no máximo 500 (o 500 é aceito).
- **RN06:** altura em metros, entre 0,50 e 2,50 (os dois limites são aceitos).
- **RN07:** o nível de atividade é obrigatório: sedentário, leve, moderado, intenso ou muito intenso.
- **RN08:** toda avaliação guarda a data em que foi registrada. Avaliações não são editadas nem apagadas, apenas adicionadas, formando o histórico.
- **RN09:** paciente sem avaliação não tem resultados; o sistema mostra uma mensagem clara.

### Regras gerais

- **RN10:** dado inválido nunca gera um resultado estranho: o sistema recusa o dado e explica o motivo, por meio de exceções.
- **RN11:** paciente não encontrado gera uma mensagem de "não encontrado", sem derrubar o programa.

### Cálculos

**IMC** = peso ÷ (altura × altura)

**Classificação do IMC (adultos):**

| IMC | Classificação |
|---|---|
| menor que 18,5 | Baixo peso |
| de 18,5 até menos de 25 | Eutrofia |
| de 25 até menos de 30 | Sobrepeso |
| de 30 até menos de 35 | Obesidade grau I |
| de 35 até menos de 40 | Obesidade grau II |
| 40 ou mais | Obesidade grau III |

**TMB, Mifflin-St Jeor** (peso em kg, altura em cm):

- homem: (10 × peso) + (6,25 × altura) − (5 × idade) + 5
- mulher: (10 × peso) + (6,25 × altura) − (5 × idade) − 161

**TMB, Harris-Benedict revisada** (peso em kg, altura em cm):

- homem: 88,362 + (13,397 × peso) + (4,799 × altura) − (5,677 × idade)
- mulher: 447,593 + (9,247 × peso) + (3,098 × altura) − (4,330 × idade)

**Gasto energético total (GET)** = TMB × fator de atividade:

| Nível de atividade | Fator |
|---|---|
| Sedentário | 1,2 |
| Leve | 1,375 |
| Moderado | 1,55 |
| Intenso | 1,725 |
| Muito intenso | 1,9 |

**Exibição:** IMC com 2 casas decimais; calorias como número inteiro.

## Como executar

Pré-requisitos: **JDK** (a versão está definida no `pom.xml`) e **Maven**, ou uma IDE como o IntelliJ IDEA, que já traz o Maven.

```bash
git clone https://github.com/Eu-Lucas-Hickmann/SistemaNutri.git
cd SistemaNutri
```

**Pela IDE:** abra o projeto e execute a classe `Main`, do pacote `nutricao.console`.

**Pelo terminal:**

```bash
mvn compile
java -cp target/classes nutricao.console.Main
```

## Roadmap

- [ ] **v1:** aplicação de terminal em Java puro, com dados em memória
- [ ] Testes unitários (JUnit) para IMC, classificação e fórmulas de TMB
- [ ] **v2:** migração para Spring Boot com API REST e PostgreSQL
- [ ] **v3:** interface web (React, a confirmar)
- [ ] Atendimento de crianças e idosos
- [ ] Plano alimentar
- [ ] Autenticação e controle de acesso

## Aviso sobre dados

Dados de saúde são considerados **dados sensíveis** pela LGPD. Este repositório deve conter apenas **dados fictícios de teste**. Antes de qualquer uso com pacientes reais, será necessário implementar autenticação, controle de acesso, backup, hospedagem segura e consentimento dos pacientes.

## Autor

**Lucas Hickmann**, estudante de Ciência da Computação na FURB.

## Licença

Este projeto ainda não possui licença definida. Enquanto nenhuma licença for adicionada, todos os direitos permanecem com o autor.