## Criar uma prova `POST /test`

**Example Request**
``` json
{
    "name": "Prova de teste 01"
}
```

**Example Response**
``` json
{
    "testId": "5e6863bc-4f69-4a95-b672-c41296ec95a2",
    "name": "Prova de teste 01"
}
```
## Criar uma questão `POST /questions`

**Example Request**
``` json
{
    "testId": "5e6863bc-4f69-4a95-b672-c41296ec95a2",
    "question": {
    "question": "1+1",
    "options": [ 
            { "value": "3", "isCorrect": false },
            { "value": "2", "isCorrect": true },
            { "value": "4", "isCorrect": false },
            { "value": "6", "isCorrect": false },
            { "value": "7", "isCorrect": false }
        ]
    }
}
```

**Example Response**
``` json
{
    "id": "e7baa643-6ee6-4ffc-b41b-4aa248b4c144",
    "question": "1+1",
    "totalOptions": 5,
    "correctOptionId": "00b3841f-245f-44ce-9ac2-0cffd18e93ab",
    "message": "Questão criada com sucesso!"
}
```

## Criar uma questão `PUT /questions/{questionId}`

**Example Request**
``` json
{
    "question": "10+10",
    "options": [
        {"optionId": "63d25ed5-f5f9-4a60-a5c0-3718bf9f9a03", "value": "2", "isCorrect": false},
        {"optionId": "00b3841f-245f-44ce-9ac2-0cffd18e93ab", "value": "10", "isCorrect": false},
        {"optionId": "4b8f5e4a-892e-41e2-ab58-b9e7dd907b70", "value": "15", "isCorrect": false},
        {"optionId": "22fd1613-f319-406d-897c-ba3d6d36458c", "value": "13", "isCorrect": false},
        {"optionId": "0034398d-c602-43ba-9aff-6f0081244b30", "value": "20", "isCorrect": true}
    ]
}
```

**Example Response**
``` json
{
    "id": "5e6863bc-4f69-4a95-b672-c41296ec95a2",
    "message": "Questão alterada com sucesso!"
}
```