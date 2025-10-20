import { type Request, type Response } from 'express';
import { supabase } from '../../config/supabase.js';

export async function createUser(req: Request, res: Response) {
  try {
    const { user_id, email, name, icon } = req.body as {
      user_id?: string;
      email?: string;
      name?: string;
      icon?: string | null;
    };

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

    if (error) throw error as Error;

    return res.status(201).json({ user: data });
  } catch (error: any) {
    console.error('Erro ao criar usuário:', error);
    res.status(500).json({
      success: false,
      error: 'Erro ao criar usuário',
      message: error?.message,
    });
  }
}


