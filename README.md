# Financial Control API

API para controle financeiro, onde você pode realizar o cadastro de todos os seus gastos e ganho separados por categorias.

## Tecnologias

![Language Java](https://img.shields.io/badge/Language-Java-organe.svg)
![Gradle Automation](https://img.shields.io/badge/Tool-Maven-blue.svg)
![SpringBoot Framework](https://img.shields.io/badge/Framework-SpringBoot-green.svg)
![Postgres Database](https://img.shields.io/badge/Database-Postgres-blue.svg)
![Docker Container](https://img.shields.io/badge/Container-Docker-lightblue.svg)

## Instruções de Instalação

### Pré-requisitos
1. **Java 17** ou superior instalado;
2. **Docker** para orquestração do container contendo banco de dados e grafana;
3. **IDE** como IntelliJ IDEA ou outra de sua preferência.

### Clonando o Repositório

```
  git clone https://github.com/BiaVediner/financial-control-api.git
```

### Rodando o Projeto

1. Vá até o caminho do projeto instalado:

```
   cd /financial-control-api
```

2. Baixe as dependências do projeto:

```
   mvn clean install
```

4. Inicie o docker

```
   docker-compose up -d
```

5. Inicie o projeto na sua IDE de preferência

```
   mvn spring-boot:run
```

A aplicação estará disponível no endpoint

```http
  http://localhost:8080/v1
```

## Documentação da API

```http
  http://localhost:8080/v1/swagger-ui.html
```