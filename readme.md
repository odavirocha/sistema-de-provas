<div id="user-content-toc">
  <ul align="center" style="list-style: none;">
    <summary>
      <h1>SISTEMA DE PROVAS</h1>
    </summary>
  </ul>
</div>

Esse sistema foi pensado para o usuário criar sua própria prova ou fazer a de outro usuário.

-- IMAGEM COM O MER DA APLICAÇÃO

<div id="user-content-toc">
  <ul style="list-style: none;">
    <summary>
      <h2>
        Stack 🛠️
      </h2>
    </summary>
  </ul>
</div>

  <picture>
    <source media="(prefers-color-scheme: dark)" srcset="https://raw.githubusercontent.com/oDroca/icones-para-readme/main/icons/java-white.svg" width="50">
    <img alt="Icone java" src="https://raw.githubusercontent.com/oDroca/icones-para-readme/main/icons/java-black.svg" width="50">
  </picture>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
   <picture>
    <source media="(prefers-color-scheme: dark)" srcset="https://cdn.simpleicons.org/spring/white" width="50">
    <img alt="Icone java" src="https://cdn.simpleicons.org/spring/black" width="50">
  </picture>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
  <picture>
    <source media="(prefers-color-scheme: dark)" srcset="https://cdn.simpleicons.org/apachemaven/white" width="50">
    <img alt="Icone java" src="https://cdn.simpleicons.org/apachemaven/black" width="50">
  </picture>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
  <picture>
    <source media="(prefers-color-scheme: dark)" srcset="https://cdn.simpleicons.org/postgresql/white" width="50">
    <img alt="Icone java" src="https://cdn.simpleicons.org/postgresql/black" width="50">
  </picture>
&nbsp;&nbsp;

**Java 17**&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**Spring 3.5.5**&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**Maven 4.0.0**&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**PostgreSQL**

<div id="user-content-toc">
  <ul style="list-style: none;">
    <summary>
      <h2>
        Como instalar 📦
      </h2>
    </summary>
  </ul>
</div>

<div id="user-content-toc">
  <ul style="list-style: none;">
    <summary>
      <h2>
        Rotas
        <picture>
          <source media="(prefers-color-scheme: dark)" srcset="https://raw.githubusercontent.com/oDroca/icones-para-readme/main/icons/endpoint-white.svg" width="20">
          <img alt="Icone java" src="https://raw.githubusercontent.com/oDroca/icones-para-readme/main/icons/endpoint-black.svg" width="20">
        </picture>
      </h2>
    </summary>
  </ul>
</div>

<details>
    <summary><strong> Provas </strong> <sub> (expandir) </sub></summary>

###### *NOTA: Para essas rotas funcionarem, é necessário enviar o Access Token via CookieHttp Only.*

* ### POST /test
  * #### Como criar uma prova `/test/`
    <details>
      <summary><strong> Exemplo de requisição 📤 </strong> <sub> (expandir) </sub></summary>
      &nbsp;

    ```json
    {
        "name": "Prova de teste 01"
    }
    ```
    </details>

    <details>
      <summary><strong> Examplo de resposta 📥 </strong> <sub> (expandir) </sub></summary>
    &nbsp;

    ``` json
    {
        "testId": "5e6863bc-4f69-4a95-b672-c41296ec95a2",
        "name": "Prova de teste 01",
        "totalQuestions": 5
    }
    ```
    </details>

  * #### Como responder uma prova `/test/{testId}`
    <details>
      <summary><strong> Exemplo de requisição 📤 </strong> <sub> (expandir) </sub></summary>
      &nbsp;

    ```json
    {
      "questions": [
        {
          "questionId": "9fa7c520-38d9-453c-957f-6b0f0cc8a293",
          "selectedOptionId": "de823a13-3c12-4cf7-b266-3d16abe98c94"
        }
      ]
    }
    ```
    </details>

    <details>
      <summary><strong> Examplo de resposta 📥 </strong> <sub> (expandir) </sub></summary>
      &nbsp;

    ``` json
    {
      "questions": [
        {
          "questionId": "9fa7c520-38d9-453c-957f-6b0f0cc8a293",
          "selectedOptionId": "de823a13-3c12-4cf7-b266-3d16abe98c94",
          "correctOptionId": "de823a13-3c12-4cf7-b266-3d16abe98c94",
          "isCorrect": true
        }
      ],
      "message": "Prova finalizada.",
      "correctCount": 1,
      "incorrectCount": 0
    }
    ```
    </details>


* ### GET /test

  * #### Retorna todas as provas do usuário `/test/`

    <details>
        <summary><strong> Examplo de resposta 📥 </strong> <sub> (expandir) </sub></summary>
    &nbsp;

    ``` json
    [
      {
        "testId": "07469299-616f-4f0a-8d09-12da01570437",
        "name": "Prova de Teste 1",
        "totalQuestions": 1
      }
    ]
    ```
    </details>
  
  * #### Retorna uma prova `/test/${testId}`

    <details>
      <summary><strong> Examplo de resposta 📥 </strong> <sub> (expandir) </sub></summary>
    &nbsp;
    
    ``` json
    {
      "testName": "Prova de Teste 1",
      "questions": [
        {
          "id": "9fa7c520-38d9-453c-957f-6b0f0cc8a293",
          "question": "Questão número 6",
          "options": [
            {
              "id": "1f8a0fd2-1992-49bc-b2d9-be97609b653c",
              "value": "1",
              "isCorrect": false
            },
            {
              "id": "e84e89bf-cb4d-4de0-95ce-2c4ea9e555b8",
              "value": "2",
              "isCorrect": false
            },
            {
              "id": "b9fdc438-6fbb-4165-ae29-c38b30087110",
              "value": "3",
              "isCorrect": false
            },
            {
              "id": "bdd3c697-94da-4032-a7c1-deef3eb9f14b",
              "value": "4",
              "isCorrect": false
            },
            {
              "id": "de823a13-3c12-4cf7-b266-3d16abe98c94",
              "value": "5",
              "isCorrect": true
            }
          ]
        }
      ]
    }
    ```
    </details>

* ### DELETE /test
    
    - #### Deleta uma prova `/test/${testId}`

      <details>
        <summary><strong> Examplo de resposta 📥 </strong> <sub> (expandir) </sub></summary>
      &nbsp;

      ``` json
      {
        "id": "dae17f4c-3b63-4ce6-92eb-7ce4c950b496",
        "message": "Prova deletada com sucesso!"
      }
      ```
      </details>


---
* ### POST /question

    - #### Como criar uma questão `/question/{testId}`

      <details>
        <summary><strong> Examplo de requisição 📤 </strong> <sub> (expandir) </sub></summary>
      &nbsp;

      ```json
      {
        "question": {
          "question": "Quanto é 1+1?",
          "options": [
            {"value": "1", "isCorrect": false},
            {"value": "2", "isCorrect": true},
            {"value": "3", "isCorrect": false},
            {"value": "4", "isCorrect": false}
          ]
        }
      }
      ```
      
      </details>

      <details>
        <summary><strong> Examplo de resposta 📥 </strong> <sub> (expandir) </sub></summary>
      &nbsp;

      ```json
      {
        "id": "486fce7b-2012-4a88-b23b-5cc353060643",
        "question": "Quanto é 1+1?",
        "totalOptions": 4,
        "correctOptionId": "d8bebc0f-16f2-4fb3-840f-5b106f0a78d8",
        "message": "Questão criada com sucesso!"
      }
      ```
      
      </details>

    - #### Como criar varias questões `/question/{testId}/batch`

      <details>
        <summary><strong> Examplo de requisição 📤 </strong> <sub> (expandir) </sub></summary>
      &nbsp;

      ```json
      {
        "questions": [
            {
              "question": "Quanto é 1+1?",
              "options": [
                {"value": "1", "isCorrect": false},
                {"value": "2", "isCorrect": true},
                {"value": "3", "isCorrect": false},
                {"value": "4", "isCorrect": false}
              ]
            },
            {
              "question": "Quanto é 1+2?",
              "options": [
                {"value": "1", "isCorrect": false},
                {"value": "2", "isCorrect": false},
                {"value": "3", "isCorrect": true},
                {"value": "4", "isCorrect": false}
              ]
            },
            {
              "question": "Quanto é 1+3?",
              "options": [
                {"value": "1", "isCorrect": false},
                {"value": "2", "isCorrect": false},
                {"value": "3", "isCorrect": false},
                {"value": "4", "isCorrect": true}
              ]
            }
        ]
      }
      ```
      
      </details>

      <details>
        <summary><strong> Examplo de resposta 📥 </strong> <sub> (expandir) </sub></summary>
      &nbsp;

      ```json
      {
        "id": "07469299-616f-4f0a-8d09-12da01570437",
        "totalCreatedQuestions": 3,
        "message": "Questões criadas com sucesso!"
      }
      ```
      
      </details>

</details>

---

### Variáveis de Ambiente (.env)
<details>
  <summary><strong> Se necessário </strong> <sub> (expandir) </sub></summary>
&nbsp;

Para se conectar ao banco de dados sem expor a URL de conexão, eu tive que por essa configuraçãaao para o projeto conseguir ler o arquivo ".env" em `.vscode/launch.json`.

```json
{
  "version": "0.2.0",
  "configurations": [
    {
      "type": "java",
      "name": "Spring Boot App",
      "request": "launch",
      "mainClass": "dev.odroca.api_provas.ApiProvasApplication",
      "projectName": "api_provas",
      "envFile": "${workspaceFolder}/.env"
    }
  ]
}
```

*O arquivo .env deve ficar na raiz do projeto para ser lido pelo launch.json e pelo docker — se estiver com PostgreSQL via docker.*

</details>

## Roadmap 🗺️
- [ ] Provas públicas e privadas