
---

# Passos mínimos para rodar o serviço Python

## Pré-requisitos

* Python 3.9+ instalado
* Espaço em disco disponível (1,62 GB+)

---

## 1. Entrar na pasta do serviço

```powershell
cd python_service
```

---

## 2. Criar e ativar o ambiente virtual

```powershell
python3 -m venv .venv
.\.venv\Scripts\Activate.ps1  # Windows
source .venv/bin/activate     # Linux (Ubuntu)
```

---

## 3. Criar arquivo `.env`

Criar o arquivo na raiz do projeto com as variáveis:

```
SUPABASE_URL=...
SUPABASE_KEY=...
```

---

## 4. Instalar dependências (CPU)

```powershell
pip install -r requirements.txt
```

*Para GPU:* instalar `torch` seguindo o instalador oficial:
[https://pytorch.org/get-started/locally/](https://pytorch.org/get-started/locally/)

---

## 5. Rodar o serviço

```powershell
python main.py
```

---

## Observações

* No primeiro run, os modelos HuggingFace serão baixados (processo demorado e consome vários GB).
* Em máquinas sem GPU, usar apenas a instalação padrão de CPU. Caso o `torch` dê erro, instalar manualmente via site oficial.
* Problemas de memória: reduzir número de threads, por exemplo:

  ```python
  AnalisadorSentimentosVWSI(max_workers=1)
  ```
* Logs e erros aparecem diretamente no terminal.

