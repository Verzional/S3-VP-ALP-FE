package com.example.vp_alp_test.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.vp_alp_test.R
import com.example.vp_alp_test.model.PostModel
import com.example.vp_alp_test.ui.theme.BackgroundBlue

@Composable
fun PostListCard(
    post: PostModel,
    isLiked: Boolean,
    likeCount: Int,
    commentCount: Int,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit
) {
    val baseURL = "http://10.0.52.150:3000"

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(BackgroundBlue),
        shape = RectangleShape
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.luffy),
                    contentDescription = "Dummy Image",
                    modifier = Modifier
                        .size(54.dp)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(100)),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    Text(
                        text = "User ${post.userId}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                    Text(
                        text = "3 hours ago",
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.padding(vertical = 4.dp))

            Text(
                text = post.title,
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 2.dp)
            )
            Text(
                text = post.content,
                fontSize = 14.sp,
                color = Color.White,
            )

            // Only show image if imageUrl is not null
            post.imageUrl?.let { imageUrl ->
                val fullImageUrl = "${baseURL}$imageUrl"
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(fullImageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Post Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 8.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.FillWidth,
                    loading = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = Color.White
                            )
                        }
                    }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onLikeClick
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (isLiked) {
                                R.drawable.baseline_favorite_24
                            } else {
                                R.drawable.baseline_favorite_border_24
                            }
                        ),
                        contentDescription = if (isLiked) "Unlike" else "Like",
                        modifier = Modifier.size(32.dp),
                        tint = if (isLiked) Color.Red else Color.White
                    )
                }

                Spacer(Modifier.padding(end = 2.dp))

                Text(
                    text = likeCount.toString(),
                    fontSize = 16.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.padding(end = 20.dp))

                IconButton(
                    onClick = onCommentClick
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.rounded_comment_24),
                        contentDescription = "Comment Icon",
                        colorFilter = ColorFilter.tint(Color.White),
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(Modifier.padding(end = 2.dp))

                Text(
                    text = commentCount.toString(),
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}