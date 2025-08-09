package com.example.kotlinassessmentapp.ui.screens
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinassessmentapp.data.model.Categories
import com.example.kotlinassessmentapp.data.model.Category
import com.example.kotlinassessmentapp.ui.viewmodel.ExpenseViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalDateTime
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import android.Manifest
import androidx.compose.ui.platform.LocalContext
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.os.Build

@OptIn(ExperimentalMaterial3Api::class)
/**
 * AddExpenseScreen following Enterprise Form Handling Patterns
 * 
 * This screen demonstrates:
 * - FORM VALIDATION with proper error handling
 * - STATE MANAGEMENT with local UI state and ViewModel business logic
 * - USER EXPERIENCE patterns (loading states, error messages, validation feedback)
 * - ACCESSIBILITY considerations (content descriptions, semantic markup)
 * 
 * Pattern used by enterprise apps like:
 * - Google Pay (form validation and error handling)
 * - Netflix (user input validation)
 * - Airbnb (booking forms with category selection)
 */
@Composable
fun AddExpenseScreen(
    onBackClick: () -> Unit,
    onExpenseAdded: () -> Unit,
    expenseViewModel: ExpenseViewModel = viewModel()
) {
    // Form state
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var receiptImageUri by remember { mutableStateOf<Uri?>(null) }
    
    // UI state
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }
    var showSuccessAnimation by remember { mutableStateOf(false) }
    
    // Context and coroutine scope
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        receiptImageUri = uri
    }

    // Permission launcher for storage access
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted, launch image picker
            imagePickerLauncher.launch("image/*")
        } else {
            // Permission denied, show message
            android.widget.Toast.makeText(context, "Storage permission is required to select images", android.widget.Toast.LENGTH_LONG).show()
        }
    }
    
    // Get today's total expenses - Reactive StateFlow (no manual refresh needed)
    val uiState by expenseViewModel.uiState.collectAsState()
    val todayExpenses by expenseViewModel.getTodaysTotalExpenses().collectAsState()
    
    // Animation states
    val submitButtonScale by animateFloatAsState(
        targetValue = if (isSubmitting) 0.95f else 1f,
        animationSpec = tween(100)
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = "Add Expense",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Total Spent Today Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Total Spent Today",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "₹${String.format("%.2f", todayExpenses)}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Title Input (Required, non-empty validation)
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = showError && title.isBlank(),
            supportingText = if (showError && title.isBlank()) {
                { Text("Title is required", color = MaterialTheme.colorScheme.error) }
            } else null
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Amount Input (₹, must be > 0)
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount (₹) *") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            leadingIcon = { Text("₹", style = MaterialTheme.typography.bodyLarge) },
            singleLine = true,
            isError = showError && (amount.isBlank() || amount.toDoubleOrNull()?.let { it <= 0 } == true),
            supportingText = if (showError && (amount.isBlank() || amount.toDoubleOrNull()?.let { it <= 0 } == true)) {
                { Text("Amount must be greater than 0", color = MaterialTheme.colorScheme.error) }
            } else null
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Notes Input (Optional, max 100 chars)
        OutlinedTextField(
            value = notes,
            onValueChange = { if (it.length <= 100) notes = it },
            label = { Text("Notes (Optional)") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3,
            supportingText = { 
                Text(
                    "${notes.length}/100 characters",
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Category Selection (Dropdown: Staff, Travel, Food, Utility)
        Text(
            text = "Category *",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.height(120.dp)
        ) {
            items(Categories.all) { category ->
                CategoryItem(
                    category = category,
                    isSelected = selectedCategory == category,
                    onClick = { selectedCategory = category }
                )
            }
        }
        
        if (showError && selectedCategory == null) {
            Text(
                text = "Please select a category",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Receipt Image (Optional → Upload/Mock image)
        Text(
            text = "Receipt Image (Optional)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (receiptImageUri != null) 200.dp else 100.dp)
                .clickable {
                    // Check permission before launching image picker
                    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        Manifest.permission.READ_MEDIA_IMAGES
                    } else {
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    }

                    when {
                        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED -> {
                            // Permission already granted
                            imagePickerLauncher.launch("image/*")
                        }
                        else -> {
                            // Request permission
                            permissionLauncher.launch(permission)
                        }
                    }
                },
            colors = CardDefaults.cardColors(
                containerColor = if (receiptImageUri != null)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.surfaceVariant
            ),
            border = BorderStroke(
                1.dp,
                if (receiptImageUri != null)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.outline
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (receiptImageUri != null) {
                    // Show selected image
                    AsyncImage(
                        model = receiptImageUri,
                        contentDescription = "Receipt Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Remove button overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        IconButton(
                            onClick = { receiptImageUri = null },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Remove Image",
                                tint = Color.White
                            )
                        }
                    }
                } else {
                    // Show upload prompt
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudUpload,
                            contentDescription = "Upload Receipt",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(32.dp)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Tap to add receipt",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Error Message
        if (showError) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Text(
                    text = errorMessage,
                    modifier = Modifier.padding(12.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Submit Button with Animation
        Button(
            onClick = {
                // Validate inputs
                when {
                    title.isBlank() -> {
                        showError = true
                        errorMessage = "Title is required"
                    }
                    amount.isBlank() || amount.toDoubleOrNull() == null || amount.toDouble() <= 0 -> {
                        showError = true
                        errorMessage = "Amount must be greater than 0"
                    }
                    selectedCategory == null -> {
                        showError = true
                        errorMessage = "Please select a category"
                    }
                    else -> {
                        // All validations passed
                        showError = false
                        isSubmitting = true
                        
                        // Add expense to repository
                        expenseViewModel.addExpense(
                            title = title,
                            amount = amount.toDouble(),
                            category = selectedCategory!!,
                            description = notes,
                            receiptImageUri = receiptImageUri?.toString()
                        )
                        
                        // Show success animation and toast
                        scope.launch {
                            showSuccessAnimation = true
                            
                            // Show Toast: "Expense Added"
                            android.widget.Toast.makeText(context, "Expense Added", android.widget.Toast.LENGTH_SHORT).show()
                            
                            // Animate expense entry
                            delay(300)
                            
                            isSubmitting = false
                            onExpenseAdded()
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .scale(submitButtonScale),
            shape = RoundedCornerShape(12.dp),
            enabled = !isSubmitting
        ) {
            if (isSubmitting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Adding...")
            } else {
                Text(
                    text = "Submit",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        
        // Success Animation
        AnimatedVisibility(
            visible = showSuccessAnimation,
            enter = scaleIn(animationSpec = tween(300)) + fadeIn(),
            exit = scaleOut(animationSpec = tween(300)) + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Success",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}



@Composable
private fun CategoryItem(
    category: Category,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        ),
        border = if (isSelected) {
            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        } else {
            BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        },
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 2.dp
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = category.name,
                tint = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    Color(category.color)
                },
                modifier = Modifier.size(28.dp)
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = category.name,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                textAlign = TextAlign.Center
            )
        }
    }
} 