# Teste de Automação - Agibank

## 📌 Sobre o projeto

Este projeto tem como objetivo validar a funcionalidade de busca do blog do Agibank por meio de testes automatizados.

Tudo foi desenvolvido utilizando boas práticas de testes.

---

## 🧪 Cenários automatizados (Web)

 Cenários cobertos:

- Busca com termo válido
- Busca sem informar termo
- Busca com caracteres especiais
- Busca com termo inexistente

Os testes validam:
- Renderização da página
- Presença de resultados
- Comportamento da aplicação em diferentes entradas

---

## 🛠️ Tecnologias utilizadas

- Java
- JUnit 5
- Selenium WebDriver
- WebDriverManager
- Maven

---

## 📂 Estrutura do projeto
teste-automacao-agibank/
├── teste-blogdoagi/
│ └── src/test/java/web/
│ └── BuscaBlogAgibankTest.java
├── pom.xml
└── README.md


---

## ▶️ Como executar o projeto

### Pré-requisitos

- Java 11+ (ou superior)
- Maven instalado
- Google Chrome instalado

---

### Passos para execução

Clone o repositório:

```bash
git clone https://github.com/Martyello/testes-automacao-agibank.git
