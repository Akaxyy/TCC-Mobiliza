import { supabase } from '../../config/supabase.js';
export async function getComments(req, res) {
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
}
export async function createComment(req, res) {
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
}
export async function updateComment(req, res) {
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
}
export async function deleteComment(req, res) {
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
}
