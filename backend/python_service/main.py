from supabase_db import SupabaseClientDB
from vwsi import AnalisadorSentimentosVWSI

def main():
    try:
        supabase_client = SupabaseClientDB()
        comentarios = supabase_client.pegar_comentarios()

        analisar_sentimentos = AnalisadorSentimentosVWSI(fator_penalizacao=0.5) 
        resultado = analisar_sentimentos.analisar_por_linha(comentarios)

        supabase_client.inserir_no_supabase(resultado)
    
    except Exception as err:
        raise(f"Erro ocorrido: {err}")

if __name__ == "__main__":
    main()

