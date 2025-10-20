import { Router } from 'express';
import usersRouter from './users.routes.js';
import commentsRouter from './comments.routes.js';
import graphRouter from './graph.routes.js';
import lineRankingRouter from './line-ranking.routes.js';

const router = Router();

router.use('/user', usersRouter);
router.use('/comments', commentsRouter);
router.use('/rota', graphRouter);
router.use('/line-ranking', lineRankingRouter);

export default router;


