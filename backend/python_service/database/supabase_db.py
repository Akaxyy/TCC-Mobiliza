from supabase import create_client, Client
from dotenv import load_dotenv
import os

load_dotenv()

# ==========================================================
# CLASSE PRINCIPAL DO CLIENTE SUPABASE
# ==========================================================
class SupabaseClientDB:
    def __init__(self):    
        self.url: str = os.environ.get("SUPABASE_URL")
        self.key: str = os.environ.get("SUPABASE_KEY")
        self.supabase: Client = create_client(self.url, self.key)

    def pegar_comentarios(self):
        """
        Busca todos os comentários armazenados na tabela 'comments' do Supabase.

        O carregamento é feito em lotes (batch) para evitar sobrecarga na API.

        Retorna:
            list[dict]: lista de dicionários contendo 'line_id' e 'content'.
        """

        response = (
            self.supabase.table("comments")
            .select("line_id, content")
            .order("created_at", desc=True)
            .range(0, 1000)
            .execute()
        )
        all_data = response.data

        return all_data

    def inserir_no_supabase(self, results: dict):
        """
        Recebe o dicionário de resultados da análise VWSI e insere ou atualiza
        os valores na tabela 'line_ranking' do Supabase.

        Parâmetros:
            results (dict): saída do método 'analisar_por_linha',
            contendo scores agregados por 'line_id'.
        """
        records = []
        for line_id, info in results.items():
            try:
                score = info["resumo"]["nota_geral_estrelas_vswi"]
                records.append({"line_id": int(line_id), "score": float(score)})
            except Exception as e:
                raise(f"Erro ao processar linha {line_id}: {e}")

        # Inserção em lotes para evitar limite de payload do Supabase
        batch_size = 500
        for i in range(0, len(records), batch_size):
            batch = records[i:i + batch_size]
            try:
                self.supabase.table("line_ranking").upsert(batch).execute()
            except Exception as e:
                raise(f"Erro ao inserir lote {i // batch_size + 1}: {e}")
