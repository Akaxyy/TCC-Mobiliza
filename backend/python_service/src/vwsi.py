from transformers import MarianMTModel, MarianTokenizer, AutoTokenizer, AutoModelForSequenceClassification
from collections import defaultdict
from concurrent.futures import ThreadPoolExecutor
import torch
import torch.nn.functional as F
import statistics

# ==========================================================
# CLASSE RESPONSÁVEL PELO CÁLCULO DO ÍNDICE VWSI
# ==========================================================
class CalculadoraVWSI:
    """
    Responsável por calcular métricas de sentimento a partir dos
    scores normalizados (SSN) e probabilidades agregadas.
    """

    def __init__(self, fator_penalizacao=0.5):
        self.fator_penalizacao = fator_penalizacao

    def calcular(self, scores_normalizados, soma_pos, soma_neu, soma_neg, n):
        """
        Recebe os scores e soma das probabilidades e retorna o resumo
        estatístico contendo:
          - médias de referência
          - volatilidade
          - índice VWSI
          - nota em estrelas (1 a 5)
        """
        # --- Cálculo das médias ---
        media_ssn = statistics.mean(scores_normalizados)
        volatilidade_ssn = (
            statistics.stdev(scores_normalizados) if len(scores_normalizados) > 1 else 0
        )

        # --- Índice VWSI ---
        vswi = media_ssn - (volatilidade_ssn * self.fator_penalizacao)

        # --- Conversão linear para 1–5 estrelas ---
        nota_geral = (vswi + 1) * 2 + 1
        nota_geral = max(1.0, min(5.0, nota_geral))

        # --- Médias de referência ---
        resumo = {
            "media_negativo_ref": round(soma_neg / n, 4),
            "media_neutro_ref": round(soma_neu / n, 4),
            "media_positivo_ref": round(soma_pos / n, 4),
            "media_agregada_sentimento_μ_ssn": round(media_ssn, 4),
            "volatilidade_sentimento_σ_ssn": round(volatilidade_ssn, 4),
            "indice_vswi": round(vswi, 4),
            "nota_geral_estrelas_vswi": round(nota_geral, 2),
        }

        return resumo

# ==========================================================
# CLASSE PRINCIPAL DO ANALISADOR DE SENTIMENTOS
# ==========================================================
class AnalisadorSentimentosVWSI:
    def __init__(self, model_name="cardiffnlp/twitter-roberta-base-sentiment-latest", fator_penalizacao=0.5, max_workers=4):
        """
        Inicializa o analisador de sentimentos.

        Parâmetros:
        - model_name (str): nome do modelo de sentimento pré-treinado.
        - fator_penalizacao (float): peso aplicado à volatilidade.
        - max_workers (int): número de threads para tradução paralela.
        """
        
        # Utiliza CUDA se disponível
        self.device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

        # Roberta LLM para sentimento
        self.tokenizer = AutoTokenizer.from_pretrained(model_name)
        self.model = AutoModelForSequenceClassification.from_pretrained(model_name).to(self.device)

        # MarianMT para tradução PT -> EN
        self.tokenizer_trad = MarianTokenizer.from_pretrained("Helsinki-NLP/opus-mt-ROMANCE-en")
        self.model_trad = MarianMTModel.from_pretrained("Helsinki-NLP/opus-mt-ROMANCE-en").to(self.device)

        self.calculadora = CalculadoraVWSI(fator_penalizacao)
        self.max_workers = max_workers  # quantidade de threads para tradução

    def traduzir(self, texto):
        """Traduz um texto do português para o inglês usando MarianMT."""
        inputs = self.tokenizer_trad(texto, return_tensors="pt", padding=True, truncation=True, max_length=128).to(self.device)
        with torch.no_grad():
            translated = self.model_trad.generate(**inputs)
        return self.tokenizer_trad.decode(translated[0], skip_special_tokens=True)

    def traduzir_todos_comentarios(self, comentarios):
        """Traduz todos os comentários de forma paralela usando múltiplas threads."""
        with ThreadPoolExecutor(max_workers=self.max_workers) as executor:
            textos_traduzidos = list(executor.map(self.traduzir, comentarios))
        return textos_traduzidos

    def _analisar_batch(self, comentarios, textos_traduzidos):
        """
        Realiza a análise de sentimento de um conjunto de comentários traduzidos.

        Retorna um dicionário com:
        - lista detalhada dos resultados
        - resumo estatístico com o índice VWSI
        """
        
        # Inferência do modelo em batch
        inputs = self.tokenizer(
            textos_traduzidos,
            return_tensors="pt",
            padding=True,
            truncation=True,
            max_length=512
        ).to(self.device)

        with torch.no_grad():
            logits = self.model(**inputs).logits

        probs = F.softmax(logits, dim=-1).tolist()

        resultados = []
        scores_normalizados = []
        soma_pos, soma_neu, soma_neg = 0, 0, 0

        for original, traducao, p in zip(comentarios, textos_traduzidos, probs):
            neg, neu, pos = p
            ssn = pos - neg
            resultados.append({
                "comentario_original": original,
                "traducao_en": traducao,
                "score_negativo": round(neg, 4),
                "score_neutro": round(neu, 4),
                "score_positivo": round(pos, 4),
                "score_normalizado_ssn": round(ssn, 4)
            })
            scores_normalizados.append(ssn)
            soma_neg += neg
            soma_neu += neu
            soma_pos += pos

        resumo = self.calculadora.calcular(
            scores_normalizados, soma_pos, soma_neu, soma_neg, len(comentarios)
        )

        return {"resultados": resultados, "resumo": resumo}

    def analisar_por_linha(self, lista_comentarios):
        """
        Agrupa comentários por `line_id`, traduz todos os textos e
        realiza a análise de sentimento para cada grupo.

        Parâmetro:
        - lista_comentarios: lista de dicionários contendo 'line_id' e 'content'

        Retorna:
        - dict: resultados por linha, contendo detalhes dos comentários
          e o resumo estatístico da linha.
        """
                
        # Agrupa por line_id
        dados_por_linha = defaultdict(list)
        for item in lista_comentarios:
            dados_por_linha[item['line_id']].append(item['content'])

        # Tradução de todos os comentários antes de qualquer análise
        comentarios_todos = [comentario for line_id, comentarios in dados_por_linha.items() for comentario in comentarios]
        textos_traduzidos = self.traduzir_todos_comentarios(comentarios_todos)

        # Organiza os comentários traduzidos por `line_id`
        traducoes_por_linha = defaultdict(list)
        index = 0
        for line_id, comentarios in dados_por_linha.items():
            traducoes_por_linha[line_id] = textos_traduzidos[index:index+len(comentarios)]
            index += len(comentarios)

        # Análise por linha
        saida = {}
        for linha, comentarios in dados_por_linha.items():
            textos_traduzidos_linha = traducoes_por_linha[linha]
            resultado_linha = self._analisar_batch(comentarios, textos_traduzidos_linha)
            saida[linha] = {
                "comentarios": resultado_linha["resultados"],
                "resumo": resultado_linha["resumo"]
            }

        return saida