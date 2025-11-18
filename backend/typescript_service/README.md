
---

# Como usar:
* cd `"typescript_service"`
* `npm install`
* `npm run build`  
* `npm run dev`   # Ambiente de teste
* `npm run start` # Ambiente de produção

---

# API Endpoints

## **POST /api/user** — Criar usuário

**Body (JSON):**

* `user_id`
* `email`
* `name`
* `icon_url`

**Exemplo:**

```bash
POST /api/user
-H "Content-Type: application/json"
-d '{"user_id":"ID","email":"a@b.com","name":"nome","icon_url":"url"}'
```

---

## **GET /api/comments** — Listar comentários

Suporta paginação e filtros.

**Query params opcionais:**

* `limit`
* `offset`
* `line_id`
* `user_id`

**Exemplos:**

```
GET /api/comments
GET /api/comments?limit=10&offset=20
GET /api/comments?line_id=13
GET /api/comments?user_id=abc123
```

---

## **POST /api/comments** — Criar comentário

**Body (JSON):**

* `line_id`
* `user_id`
* `content`

```
POST /api/comments
-H "Content-Type: application/json"
-d '{"line_id":"13","user_id":"ID","content":"texto"}'
```

---

## **PUT /api/comments/:id** — Atualizar comentário

**Body (JSON):**

* `user_id`
* `content`

```
PUT /api/comments/123
-H "Content-Type: application/json"
-d '{"user_id":"ID","content":"novo"}'
```

---

## **DELETE /api/comments/:id** — Remover comentário

**Body (JSON):**

* `user_id`

```
DELETE /api/comments/123
-H "Content-Type: application/json"
-d '{"user_id":"ID"}'
```

---

## **GET /api/rota** — Calcular rota

**Query params:**

* `partida`
* `chegada`

Exemplo:

```
GET /api/rota?partida=Lapa&chegada=Jurubatuba
```

---

## **GET /api/line-ranking** — Ranking das linhas

```
GET /api/line-ranking
```

---
