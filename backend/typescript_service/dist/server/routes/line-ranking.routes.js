import { Router } from 'express';
import { getLatestLineRanking } from '../controllers/line-ranking.controller.js';
const router = Router();
router.get('/', getLatestLineRanking);
export default router;
