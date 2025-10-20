import { supabase } from '../../config/supabase.js';
export async function fetchComments(params) {
    const { line_id, user_id, limit = 20, offset = 0 } = params;
    let sbQuery = supabase
        .from('comments')
        .select('*')
        .order('created_at', { ascending: false })
        .range(offset, offset + limit - 1);
    if (line_id)
        sbQuery = sbQuery.eq('line_id', line_id);
    if (user_id)
        sbQuery = sbQuery.eq('user_id', user_id);
    return sbQuery;
}
