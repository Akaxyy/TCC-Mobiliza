import { createClient } from '@supabase/supabase-js';
import dotenv from 'dotenv';
dotenv.config();
const SUPABASE_URL = process.env.SUPABASE_URL;
const SUPABASE_KEY = process.env.SUPABASE_KEY;
if (!SUPABASE_URL || !SUPABASE_KEY) {
    throw new Error('Variáveis SUPABASE_URL e SUPABASE_KEY não definidas.');
}
export const supabase = createClient(SUPABASE_URL, SUPABASE_KEY);
