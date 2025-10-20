import { Router } from 'express';
import { getRoute } from '../controllers/graph.controller.js';
const router = Router();
router.get('/', getRoute);
export default router;
