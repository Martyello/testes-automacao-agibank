# 🚀 Framework de Testes Unificados

Este repositório contém uma solução de automação **Full Stack**, cobrindo testes de **Interface Web**, **API REST** e **Performance**. A arquitetura utiliza um modelo **multi-módulo Maven**, permitindo a gestão independente de cada contexto de teste.

---

## 🏗️ Arquitetura e Tecnologias

Projeto estruturado para escalabilidade e manutenção simplificada:

* **Linguagem:** Java 17 (LTS)
* **Core:** JUnit 5 (Jupyter)
* **API:** REST Assured com validação de **JSON Schema**
* **Web:** Selenium WebDriver (configurado para **Headless Mode** em CI)
* **Performance:** Apache JMeter 5.5 integrado via Maven
* **Relatórios:** Allure Framework com histórico de execução (Trends)
* **CI/CD:** GitHub Actions com deploy automático para **GitHub Pages**

---

## 🧪 Estratégia de Testes

### 1. Módulo Web (`teste-blogdoagi`)
Validação do mecanismo de busca do Blog Agibank.
* **Técnicas:** Testes de busca positiva, negativa, caracteres especiais e termos vazios.
* **Destaque:** Implementação de esperas (Waits) dinâmicas para garantir estabilidade em elementos assíncronos.

### 2. Módulo API (`teste-dogapi`)
Consumo da [Dog CEO API](https://dog.ceo/dog-api/).
* **Validações:** Status codes, estruturas de contrato (Schemas) e SLA de resposta.
* **Cenários:** Cobertura de endpoints de raças, sub-raças e fluxos de exceção (404).

### 3. Módulo Performance (`teste-performance`)
Teste de carga e resiliência na plataforma [BlazeDemo](https://www.blazedemo.com).
* **Cenário:** Fluxo transacional (Home > Reserva > Confirmação).
* **Métricas Alvo:** 250 req/s | 90th percentile < 2s.

#### 📊 Resultados Obtidos (Carga Estabilizada)

| Sampler | Throughput (Req/s) | 90th Percentile | Taxa de Erro |
| :--- | :--- | :--- | :--- |
| **Geral (Fluxo Completo)** | **243.00/s** | **978 ms** | 0.00% |
| **00 - Home (GET)** | **81.00/s** | **925 ms** | 0.00% |
| **01 - Reservar (POST)** | **81.00/s** | **1102 ms** | 0.00% |
| **02 - Confirmar (POST)** | **81.00/s** | **1133 ms** | 0.00% |

> **Parecer Técnico:** O SLA de latência foi totalmente atendido (1.1s vs limite de 2s). A vazão estabilizou em 243 req/s sem erros durante o período de stress. observa-se que o relatório inclui sub-samplers de recursos estáticos (CSS/JS), isto garante que o teste reflite a carga real processada pelo servidor durante a navegação.

---

## 📊 Relatórios e Observabilidade

Os resultados são consolidados e publicados automaticamente a cada execução da pipeline.

* 🧪 **[Dashboard Allure Report](https://martyello.github.io/testes-automacao-Maven/)**
  *(Visão consolidada de Web e API com screenshots e histórico de tendências)*

* 📈 **[Relatório de Performance (JMeter HTML)](https://martyello.github.io/testes-automacao-Maven/performance/)**
  *(Métricas detalhadas de Throughput, Latência e Gráficos de Over Time)*
---

## 🛠️ Execução Local

### Pré-requisitos
* JDK 17+ | Maven 3.6+ | Google Chrome atualizado.

### Comandos Principais

| Objetivo | Comando |
| :--- | :--- |
| **Executar Todos os Testes** | `mvn clean verify` |
| **Apenas Web** | `mvn clean test -pl teste-blogdoagi -am` |
| **Apenas API** | `mvn clean test -pl teste-dogapi -am` |
| **Apenas Performance** | `mvn clean verify -pl teste-performance -am` |
| **Gerar/Abrir Allure** | `mvn allure:serve` |

---

## ⚙️ Pipeline de CI/CD

O workflow no GitHub Actions (`.github/workflows/ci.yml`) segue as etapas:
1. Setup de ambiente e dependências.
2. Execução das suítes de teste via Maven.
3. Consolidação de resultados e deploy para **GitHub Pages**.

---
**Autor:** Marcelo Alexandre do Nascimento (Senior Test Analyst)
