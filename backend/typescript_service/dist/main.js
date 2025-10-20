import express from 'express';
import cors from 'cors';
import dotenv from 'dotenv';
import supabase from './db.js';
import { linhasDoMapa } from './mapa.js';
dotenv.config();
const app = express();
const PORT = process.env.PORT || 3000;
app.use(cors());
app.use(express.json());
// ================================
// 1. ROTAS DE USUÁRIOS (Supabase)
// ================================
app.post('/api/user', async (req, res) => {
    try {
        const { user_id, email, name, icon } = req.body;
        if (!user_id || !email || !name) {
            return res
                .status(400)
                .json({ error: 'Campos obrigatórios: user_id, email, name' });
        }
        const { data, error } = await supabase
            .from('users')
            .insert([{ user_id, email, name, icon }])
            .select()
            .single();
        if (error)
            throw error;
        return res.status(201).json({ user: data });
    }
    catch (error) {
        console.error('Erro ao criar usuário:', error);
        res.status(500).json({
            success: false,
            error: 'Erro ao criar usuário',
            message: error?.message,
        });
    }
});
// =====================================
// 2. ROTAS DE COMENTÁRIOS (Supabase)
// =====================================
app.get('/api/comments', async (req, res) => {
    try {
        const query = req.query;
        const line_id = query.line_id;
        const user_id = query.user_id;
        const limit = Number(query.limit ?? 20);
        const offset = Number(query.offset ?? 0);
        let sbQuery = supabase
            .from('comments')
            .select('*')
            .order('created_at', { ascending: false })
            .range(offset, offset + limit - 1);
        if (line_id)
            sbQuery = sbQuery.eq('line_id', line_id);
        if (user_id)
            sbQuery = sbQuery.eq('user_id', user_id);
        const { data: comments, error } = await sbQuery;
        if (error)
            throw error;
        const userIds = [...new Set((comments ?? []).map((c) => c.user_id).filter(Boolean))];
        const { data: users, error: userError } = await supabase
            .from('users')
            .select('user_id, name, icon_url')
            .in('user_id', userIds);
        if (userError)
            throw userError;
        const userMap = Object.fromEntries((users ?? []).map((u) => [u.user_id, u]));
        const formatted = (comments ?? []).map((comment) => ({
            ...comment,
            user_name: userMap[comment.user_id]?.name ?? null,
            icon_url: userMap[comment.user_id]?.icon_url ?? null,
        }));
        res.json({ success: true, data: formatted, count: formatted.length });
    }
    catch (error) {
        console.error('Erro ao buscar comentários:', error);
        res.status(500).json({
            success: false,
            error: 'Erro ao buscar comentários',
            message: error?.message,
        });
    }
});
app.post('/api/comments', async (req, res) => {
    try {
        const { line_id, user_id, content } = req.body;
        if (!line_id || !user_id || !content) {
            return res.status(400).json({
                error: 'Campos obrigatórios: line_id, user_id, content',
            });
        }
        const { data, error } = await supabase
            .from('comments')
            .insert([{ user_id, line_id, content: content.trim() }])
            .select()
            .single();
        if (error)
            throw error;
        res.status(201).json({
            success: true,
            message: 'Comentário criado com sucesso',
            comment: data,
        });
    }
    catch (error) {
        console.error('Erro ao criar comentário:', error);
        res.status(500).json({
            success: false,
            error: 'Erro ao criar comentário',
            message: error?.message,
        });
    }
});
app.put('/api/comments/:id', async (req, res) => {
    try {
        const { id } = req.params;
        const { user_id, content } = req.body;
        if (!user_id || !content || !content.trim()) {
            return res.status(400).json({
                error: 'Campos obrigatórios: user_id e content',
            });
        }
        const { data: existing, error: fetchError } = await supabase
            .from('comments')
            .select('comment_id, user_id')
            .eq('comment_id', id)
            .single();
        if (fetchError)
            throw fetchError;
        if (!existing) {
            return res.status(404).json({ error: 'Comentário não encontrado.' });
        }
        if (existing.user_id !== user_id) {
            return res.status(403).json({
                error: 'Você não tem permissão para alterar este comentário.',
            });
        }
        const { data, error } = await supabase
            .from('comments')
            .update({
            content: content.trim(),
            updated_at: new Date().toISOString(),
        })
            .eq('comment_id', id)
            .select()
            .single();
        if (error)
            throw error;
        res.json({
            success: true,
            message: 'Comentário atualizado com sucesso.',
            comment: data,
        });
    }
    catch (error) {
        console.error('Erro ao atualizar comentário:', error);
        res.status(500).json({
            success: false,
            error: 'Erro ao atualizar comentário',
            message: error?.message,
        });
    }
});
app.delete('/api/comments/:id', async (req, res) => {
    try {
        const { id } = req.params;
        const { user_id } = req.body;
        if (!user_id) {
            return res
                .status(400)
                .json({ error: 'O campo "user_id" é obrigatório.' });
        }
        const { data: existing, error: fetchError } = await supabase
            .from('comments')
            .select('comment_id, user_id')
            .eq('comment_id', id)
            .single();
        if (fetchError)
            throw fetchError;
        if (!existing) {
            return res.status(404).json({ error: 'Comentário não encontrado.' });
        }
        if (existing.user_id !== user_id) {
            return res.status(403).json({
                error: 'Você não tem permissão para excluir este comentário.',
            });
        }
        const { error } = await supabase.from('comments').delete().eq('comment_id', id);
        if (error)
            throw error;
        res.json({
            success: true,
            message: `Comentário com ID ${id} excluído com sucesso.`,
        });
    }
    catch (error) {
        console.error('Erro ao excluir comentário:', error);
        res.status(500).json({
            success: false,
            error: 'Erro ao excluir comentário',
            message: error?.message,
        });
    }
});
const temposPorLinha = {
    'Linha 1 - Azul': 2.0,
    'Linha 2 - Verde': 2.2,
    'Linha 3 - Vermelha': 2.0,
    'Linha 4 - Amarela': 2.5,
    'Linha 5 - Lilás': 2.3,
    'Linha 7 - Rubi': 5.0,
    'Linha 8 - Diamante': 4.5,
    'Linha 9 - Esmeralda': 3.5,
    'Linha 10 - Turquesa': 5.5,
    'Linha 11 - Coral': 6.0,
    'Linha 12 - Safira': 5.0,
    'Linha 13 - Jade': 12.0,
    'Linha 15 - Prata': 1.8,
};
const TEMPO_BALDEACAO = 4; // minutos
const tempoPausaPorLinha = {
    'Linha 1 - Azul': 0.6,
    'Linha 2 - Verde': 0.6,
    'Linha 3 - Vermelha': 0.6,
    'Linha 4 - Amarela': 0.5,
    'Linha 5 - Lilás': 0.6,
    'Linha 7 - Rubi': 0.8,
    'Linha 8 - Diamante': 0.8,
    'Linha 9 - Esmeralda': 0.7,
    'Linha 10 - Turquesa': 0.8,
    'Linha 11 - Coral': 0.8,
    'Linha 12 - Safira': 0.8,
    'Linha 13 - Jade': 0.7,
    'Linha 15 - Prata': 0.5,
};
const grafo = new Map();
function adicionarAresta(estacao1, estacao2, linha) {
    if (!grafo.has(estacao1))
        grafo.set(estacao1, []);
    grafo.get(estacao1).push({ estacao: estacao2, linha });
}
linhasDoMapa.forEach(({ linha, estacoes }) => {
    for (let i = 0; i < estacoes.length; i++) {
        const atual = estacoes[i];
        if (i > 0)
            adicionarAresta(atual, estacoes[i - 1], linha);
        if (i < estacoes.length - 1)
            adicionarAresta(atual, estacoes[i + 1], linha);
    }
});
function encontrarCaminho(partida, chegada) {
    if (!grafo.has(partida) || !grafo.has(chegada))
        return null;
    const fila = [[partida]];
    const visitados = new Set([partida]);
    while (fila.length > 0) {
        const caminho = fila.shift();
        const ultima = caminho[caminho.length - 1];
        if (ultima === chegada)
            return caminho;
        const vizinhos = grafo.get(ultima) ?? [];
        for (const { estacao: vizinho } of vizinhos) {
            if (!visitados.has(vizinho)) {
                visitados.add(vizinho);
                fila.push([...caminho, vizinho]);
            }
        }
    }
    return null;
}
function detalharCaminho(caminho) {
    if (!caminho)
        return null;
    const caminhoDetalhado = [];
    const baldeacoes = [];
    let tempoTotal = 0;
    const getLinha = (a, b) => {
        const conexoes = grafo.get(a);
        return conexoes.find((c) => c.estacao === b).linha;
    };
    let linhaAtual = getLinha(caminho[0], caminho[1]);
    caminhoDetalhado.push({ estacao: caminho[0], linha: linhaAtual });
    for (let i = 1; i < caminho.length; i++) {
        const anterior = caminho[i - 1];
        const atual = caminho[i];
        const linhaProx = getLinha(anterior, atual);
        tempoTotal += temposPorLinha[linhaProx] ?? 3;
        if (i > 1) {
            tempoTotal += tempoPausaPorLinha[linhaAtual] ?? 0.6;
        }
        if (linhaProx !== linhaAtual) {
            tempoTotal += TEMPO_BALDEACAO;
            baldeacoes.push({ estacao: anterior, sairDa: linhaAtual, entrarNa: linhaProx });
            linhaAtual = linhaProx;
        }
        caminhoDetalhado.push({ estacao: atual, linha: linhaAtual });
    }
    if (caminho.length > 1) {
        const ultimaLinha = caminhoDetalhado[caminhoDetalhado.length - 1].linha;
        tempoTotal += tempoPausaPorLinha[ultimaLinha] ?? 0.6;
    }
    return {
        caminho: caminhoDetalhado,
        baldeacoes,
        estatisticas: {
            numeroEstacoes: caminho.length,
            numeroBaldeacoes: baldeacoes.length,
            tempoEstimadoMinutos: Math.round(tempoTotal),
            tempoEstimadoFormatado: formatarTempo(tempoTotal),
        },
    };
}
function formatarTempo(minutos) {
    const mins = Math.round(minutos);
    if (mins < 60) {
        return `${mins} minuto${mins !== 1 ? 's' : ''}`;
    }
    const horas = Math.floor(mins / 60);
    const minutosRestantes = mins % 60;
    return `${horas}h${minutosRestantes > 0 ? ` ${minutosRestantes}min` : ''}`;
}
app.get('/api/rota', (req, res) => {
    const query = req.query;
    const partida = query.partida;
    const chegada = query.chegada;
    if (!partida || !chegada) {
        return res
            .status(400)
            .json({ erro: 'Parâmetros "partida" e "chegada" são obrigatórios.' });
    }
    if (!grafo.has(partida) || !grafo.has(chegada)) {
        return res
            .status(404)
            .json({ erro: 'Uma ou ambas as estações não foram encontradas.' });
    }
    const caminhoSimples = encontrarCaminho(partida, chegada);
    const resultado = detalharCaminho(caminhoSimples);
    if (!resultado) {
        return res.status(404).json({
            erro: `Não foi possível encontrar um caminho entre ${partida} e ${chegada}.`,
        });
    }
    res.json(resultado);
});
app.listen(PORT, () => {
    console.log(`✅ Servidor rodando em http://localhost:${PORT}`);
});
