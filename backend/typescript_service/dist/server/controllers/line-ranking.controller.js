import { supabase } from '../../config/supabase.js';
export async function getLatestLineRanking(req, res) {
    try {
        // 1️⃣ Buscar o registro mais recente para descobrir a última data
        const { data: latest, error: latestError } = await supabase
            .from('line_ranking')
            .select('created_at')
            .order('created_at', { ascending: false })
            .limit(1)
            .maybeSingle();
        if (latestError) {
            console.error('Erro ao buscar data mais recente:', latestError);
            return res.status(500).json({
                erro: 'Erro ao buscar data mais recente do line_ranking',
                detalhes: latestError.message
            });
        }
        if (!latest) {
            return res.status(404).json({
                erro: 'Nenhum registro encontrado na tabela line_ranking'
            });
        }
        const ultimaData = latest.created_at;
        // 2️⃣ Buscar todos os registros com essa mesma data
        const { data, error } = await supabase
            .from('line_ranking')
            .select('line_id, score, created_at')
            .eq('created_at', ultimaData)
            .order('line_id', { ascending: true });
        if (error) {
            console.error('Erro ao buscar registros mais recentes:', error);
            return res.status(500).json({
                erro: 'Erro interno do servidor ao buscar line_ranking',
                detalhes: error.message
            });
        }
        if (!data || data.length === 0) {
            return res.status(404).json({
                erro: 'Nenhum registro encontrado para a data mais recente'
            });
        }
        res.json({
            sucesso: true,
            data_atual: ultimaData,
            total_registros: data.length,
            dados: data
        });
    }
    catch (error) {
        console.error('Erro inesperado:', error);
        res.status(500).json({
            erro: 'Erro interno do servidor',
            detalhes: error instanceof Error ? error.message : 'Erro desconhecido'
        });
    }
}
