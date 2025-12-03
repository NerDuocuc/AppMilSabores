package com.example.appmilsabores.presentation.ui.components

import androidx.compose.foundation.Image
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import com.example.appmilsabores.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appmilsabores.domain.model.ProductSummary
import com.example.appmilsabores.presentation.ui.theme.PrimaryPurple

@Composable
fun ProductListItem(
    product: ProductSummary,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .padding(8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            val context = LocalContext.current
            // Prefer local drawable resource when available. Fallback to remote URL only if local resource is 0.
            if (product.imageRes != 0) {
                // Safe usage of painterResource: imageRes is non-zero (we ensure placeholder in sync step)
                Image(
                    painter = painterResource(id = product.imageRes),
                    contentDescription = product.name,
                    modifier = Modifier
                        .height(120.dp)
                        .fillMaxWidth()
                )
            } else if (!product.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(product.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = product.name,
                    modifier = Modifier
                        .height(120.dp)
                        .fillMaxWidth(),
                    placeholder = painterResource(id = R.drawable.avatar_placeholder),
                    error = painterResource(id = R.drawable.avatar_placeholder)
                )
            } else {
                // final fallback
                Image(
                    painter = painterResource(id = R.drawable.avatar_placeholder),
                    contentDescription = product.name,
                    modifier = Modifier
                        .height(120.dp)
                        .fillMaxWidth()
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = product.name,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
            Text(
                text = product.price,
                color = PrimaryPurple,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}
