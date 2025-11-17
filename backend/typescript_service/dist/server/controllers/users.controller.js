import { supabase } from '../../config/supabase.js';
export async function createUser(req, res) {
    try {
        const { user_id, email, name, icon_url } = req.body;
        if (!user_id || !email || !name) {
            return res
                .status(400)
                .json({ error: 'Campos obrigatórios: user_id, email, name' });
        }
        const { data, error } = await supabase
            .from('users')
            .insert([{ user_id, email, name, icon_url }])
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
}
