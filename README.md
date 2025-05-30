🌍 SafeRoute360 - Monitoramento de Sensores para Eventos Extremos
SafeRoute360 é uma aplicação desenvolvida em Java puro que simula o monitoramento de sensores de alagamento em áreas urbanas. O objetivo é fornecer uma solução leve e funcional para acompanhar, em tempo real, os níveis de risco em diferentes regiões e registrar alertas críticos automaticamente.

🔧 Tecnologias utilizadas:
Java (sem frameworks)

Oracle Database (OracleDB)

Servidor HTTP embutido (com.sun.net.httpserver)

GSON (serialização JSON)

Postman (para testes da API)

⚙️ Funcionalidades:
Cadastro de sensores com localização e nível atual

Atualização de níveis simulada com disparo automático de alertas

API RESTful com endpoints para:

GET /sensores: listar sensores

POST /sensores/post: cadastrar sensor via JSON

GET /alertas: listar alertas gerados

Armazenamento e persistência de dados em banco Oracle

Arquitetura organizada em camadas: controller, model, dao e server

🎯 Finalidade acadêmica:
Este projeto foi desenvolvido como parte do desafio da disciplina Global Solutions da FIAP, com foco na criação de soluções digitais para eventos extremos, como enchentes urbanas.
