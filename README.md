# order-management

## Descrição
Projeto Spring Boot de um sistema de pedidos, realizado para estudos de microserviços.

## Como usar

1. **Clonar o Repositório**

Primeiro, clone o repositório do projeto:

```bash
git clone https://github.com/angelovlima/order-management
cd order-management/
```

2. **Subir o docker compose**

Entre nos serviços que deseja rodar, e rode seu Docker Compose no diretório raiz do projeto, execute o seguinte comando:

```bash
docker-compose up -d
```

3. **Acessar a Documentação (Swagger)**

Após subir os containers e rodar os serviços, para acessar a documentação Swagger e visualizar os endpoints detalhados, acesse as seguintes URLs:

```bash
customer-management: http://localhost:8081/swagger-ui.html

product-catalog: http://localhost:8082/swagger-ui.html

order-processing: http://localhost:8083/swagger-ui.html

delivery-logistics: http://localhost:8084/swagger-ui.html
```

4. **Descrição detalhada**

Para um detalhamento mais profundo, leia a documentação.pdf no repositório do projeto.
