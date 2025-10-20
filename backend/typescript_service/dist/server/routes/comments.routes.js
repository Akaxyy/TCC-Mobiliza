import { Router } from 'express';
import { createComment, deleteComment, getComments, updateComment } from '../controllers/comments.controller.js';
const router = Router();
router.get('/', getComments);
router.post('/', createComment);
router.put('/:id', updateComment);
router.delete('/:id', deleteComment);
export default router;
