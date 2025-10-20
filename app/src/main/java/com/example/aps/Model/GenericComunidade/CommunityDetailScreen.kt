package com.example.aps.Screens.GenericComunidade

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit // Mantido para o botão de editar/atualizar
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Delete // Adicionado o ícone de Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aps.Model.GenericComunidade.Post
import com.example.aps.viewmodel.CommunityDetailViewModel
import android.util.Log

import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityDetailScreen(
    nomeDaLinha: String,
    numeroLinha: String,
    corLinha: Color,
    textSizeFactor: Float,
    viewModel: CommunityDetailViewModel = viewModel()
) {
    val context = LocalContext.current

    val commentContent by viewModel.commentContent.collectAsState()
    val isSendingComment by viewModel.isSendingComment.collectAsState()
    val userMessage by viewModel.userMessage.collectAsState()
    val postToEdit by viewModel.postToEdit.collectAsState()

    val posts: List<Post> by viewModel.posts.collectAsState()


    LaunchedEffect(posts) {
        if (posts.isNotEmpty()) {
            Log.d("POSTS_DEBUG", "Número de posts recebidos na tela: ${posts.size}")
        }
    }

    LaunchedEffect(numeroLinha) {
        viewModel.fetchPosts(numeroLinha)
    }

    LaunchedEffect(userMessage) {
        userMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            viewModel.clearUserMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .background(corLinha, shape = RoundedCornerShape(4.dp))
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = numeroLinha,
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Comunidade da $nomeDaLinha",
                            fontSize = 20.sp * textSizeFactor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            CommentInputBar(
                commentContent = commentContent,
                onCommentContentChange = viewModel::onCommentContentChange,
                onSend = viewModel::handlePostAction,
                isSendingComment = isSendingComment,
                textSizeFactor = textSizeFactor,
                postToEdit = postToEdit,
                onCancelEdit = viewModel::cancelEditing
            )

            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), thickness = 1.dp)

            // --- 2. LISTA DE POSTS ---
            LazyColumn(
                modifier = Modifier.weight(1f),
            ) {
                items(
                    items = posts,
                    key = { it.id }
                ) { post: Post ->
                    SocialPostItem(
                        post = post,
                        corPrincipal = corLinha,
                        textSizeFactor = textSizeFactor,
                        viewModel = viewModel,
                        onStartEdit = { viewModel.startEditing(post) }
                    )
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SocialPostItem(
    post: Post,
    corPrincipal: Color,
    textSizeFactor: Float,
    viewModel: CommunityDetailViewModel,
    onStartEdit: (Post) -> Unit
) {
    // Lógica de Permissão
    val isAuthor = post.userId == viewModel.currentUserService.userId
    val canEdit = isAuthor

    // canDelete é igual a isAuthor. Apenas o autor pode deletar.
    val canDelete = isAuthor

    // FIM DA LÓGICA DE PERMISSÃO

    // Contador de likes inicializado em 0
    var likesCount by remember { mutableStateOf(0) }
    // Inicializa o estado de curtido
    var isLiked by remember { mutableStateOf(false) }

    // Função para simular o clique no like
    val onLikeClick = {
        if (isLiked) {
            likesCount--
        } else {
            likesCount++
        }
        isLiked = !isLiked
    }
    // FIM DA SIMULAÇÃO

    // Usa o autor da API. Se for nulo, usa o ID truncado como fallback.
    val authorDisplayName = post.author ?: "Usuário: ${post.userId.take(8)}..."

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        // Metadados
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {


            val showTextAvatar = remember(post.iconUrl) {
                // Inicia mostrando o texto se a URL for nula ou vazia
                mutableStateOf(post.iconUrl.isNullOrBlank())
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape) // Garante que a área seja circular
                    .background(corPrincipal.copy(alpha = 0.15f)), // Fundo de fallback
                contentAlignment = Alignment.Center
            ) {

                // Se o estado for TRUE, mostramos o texto (fallback)
                if (showTextAvatar.value) {
                    Text(
                        text = authorDisplayName.firstOrNull()?.toString()?.uppercase() ?: "?",
                        color = corPrincipal,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                // Tenta carregar a imagem se a URL não for nula/vazia inicialmente
                if (!post.iconUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = post.iconUrl, // URL da imagem
                        contentDescription = "Foto de perfil de ${post.author}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop, // Garante que a imagem preencha o círculo
                        // Atualiza o estado: se carregar, garante que não mostre o texto
                        onSuccess = { showTextAvatar.value = false },
                        // Atualiza o estado: se falhar, mostra fallback
                        onError = { showTextAvatar.value = true },
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))

            // Nome e Tempo
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        // Usa o nome de exibição seguro
                        text = authorDisplayName,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp * textSizeFactor,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(6.dp))

                    // ⭐️ MUDANÇA APLICADA AQUI: Valor fixo "hoje"
                    Text(
                        text = "· hoje", // Antes: "· ${post.timeAgo}"
                        fontSize = 13.sp * textSizeFactor,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = post.content,
            fontSize = 15.sp * textSizeFactor,
            modifier = Modifier.padding(start = 50.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 50.dp, end = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(onClick = onLikeClick)
            ) {
                Icon(
                    imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Curtir",
                    modifier = Modifier.size(20.dp),
                    tint = if (isLiked) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = likesCount.toString(),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            if (canDelete) {
                IconButton(
                    onClick = {
                        viewModel.deleteComment(post.id, viewModel.currentUserService.userId)
                    },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Excluir Comentário",
                        tint = Color.Red.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp)) // Espaçamento entre Delete e Edit

            if (canEdit) {
                IconButton(onClick = { onStartEdit(post) }) {
                    Icon(
                        imageVector = Icons.Default.Edit, // Ícone de edição
                        contentDescription = "Atualizar Comentário", // Descrição para acessibilidade
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CommentInputBar(
    commentContent: String,
    onCommentContentChange: (String) -> Unit,
    onSend: () -> Unit,
    isSendingComment: Boolean,
    textSizeFactor: Float,
    postToEdit: Post?,
    onCancelEdit: () -> Unit
) {
    val isEditing = postToEdit != null
    val buttonText = if (isEditing) "Salvar" else "Postar"

    val placeholderAuthor = if (postToEdit?.userId == postToEdit?.author) "você" else postToEdit?.author ?: "você"
    val placeholderText = if (isEditing) "Editando comentário de $placeholderAuthor..." else "O que está acontecendo na linha?"

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 0.dp, bottomEnd = 0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            if (isEditing) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Modo Edição",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = onCancelEdit) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cancelar Edição",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = commentContent,
                    onValueChange = onCommentContentChange,
                    modifier = Modifier.weight(1f),
                    placeholder = { Text(placeholderText, fontSize = 16.sp * textSizeFactor) },
                    textStyle = LocalTextStyle.current.copy(fontSize = 16.sp * textSizeFactor),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    singleLine = false,
                    maxLines = 5,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = onSend,
                    enabled = commentContent.isNotBlank() && !isSendingComment,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.height(36.dp)
                ) {
                    if (isSendingComment) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(text = buttonText, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}