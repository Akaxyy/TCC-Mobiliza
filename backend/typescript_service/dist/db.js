import { createClient } from '@supabase/supabase-js';
import dotenv from 'dotenv';
dotenv.config();
const supabaseUrl = process.env.SUPABASE_URL;
const supabaseKey = process.env.SUPABASE_KEY;
if (!supabaseUrl || !supabaseKey) {
    throw new Error('Variáveis SUPABASE_URL e SUPABASE_KEY não definidas no ambiente.');
}
const supabase = createClient(supabaseUrl, supabaseKey);
export default supabase;
