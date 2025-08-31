package com.example.kotlinassessmentapp.utils

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.example.kotlinassessmentapp.data.model.Expense
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileWriter
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * FileExportManager - Enterprise File Export Utility
 * 
 * Features:
 * - PDF generation using iText 7
 * - CSV export with proper formatting
 * - File naming convention: expense_report.extension
 * - Saves to user's Downloads directory for file manager access
 * - Secure file sharing via FileProvider
 * - Proper error handling and coroutine support
 */
class FileExportManager(private val context: Context) {

    companion object {
        private const val FILE_PROVIDER_AUTHORITY = "com.example.kotlinassessmentapp.fileprovider"
        private const val BASE_FILENAME = "expense_report"
        // Pre-define formatters for better performance
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' HH:mm")
        private val DATE_ONLY_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy")
        private val ISO_DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE
        private val SHORT_DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd")
    }

    private val notificationManager = ExportNotificationManager(context)
    
    /**
     * Export expenses to PDF format
     * Saves to Downloads directory with filename: expense_report.pdf
     */
    suspend fun exportToPDF(expenses: List<Expense>): ExportResult = withContext(Dispatchers.IO) {
        try {
            // Pre-calculate values to avoid repeated calculations
            val totalAmount = expenses.sumOf { it.amount }
            val expenseCount = expenses.size
            val currentDateTime = java.time.LocalDateTime.now().format(DATE_FORMATTER)
            
            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Use MediaStore for Android 10+
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, "$BASE_FILENAME.pdf")
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }

                val uri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                uri?.let { fileUri ->
                    context.contentResolver.openOutputStream(fileUri)?.use { outputStream ->
                        val document = Document(PageSize.A4)
                        val writer = PdfWriter.getInstance(document, outputStream)

                        document.open()

                        // Add title
                        val titleFont = Font(Font.FontFamily.HELVETICA, 20f, Font.BOLD)
                        val title = Paragraph("Expense Report", titleFont)
                        title.alignment = Element.ALIGN_CENTER
                        document.add(title)
                        document.add(Paragraph(" "))

                        // Add summary
                        val summaryFont = Font(Font.FontFamily.HELVETICA, 16f, Font.BOLD)
                        document.add(Paragraph("Summary", summaryFont))
                        document.add(Paragraph("Total Expenses: $${String.format("%.2f", totalAmount)}"))
                        document.add(Paragraph("Number of Expenses: $expenseCount"))
                        document.add(Paragraph("Generated on: $currentDateTime"))
                        document.add(Paragraph(" "))

                        // Add expenses table
                        if (expenses.isNotEmpty()) {
                            document.add(Paragraph("Expense Details", summaryFont))
                            document.add(Paragraph(" "))

                            val table = PdfPTable(5)
                            table.widthPercentage = 100f
                            table.setWidths(floatArrayOf(20f, 25f, 20f, 15f, 20f))

                            // Table headers
                            table.addCell("Date")
                            table.addCell("Title")
                            table.addCell("Category")
                            table.addCell("Amount")
                            table.addCell("Description")

                            // Table data - use sorted list to avoid repeated sorting
                            val sortedExpenses = expenses.sortedByDescending { it.date }
                            sortedExpenses.forEach { expense ->
                                table.addCell(expense.date.format(DATE_ONLY_FORMATTER))
                                table.addCell(expense.title)
                                table.addCell(expense.category.name)
                                table.addCell("$${String.format("%.2f", expense.amount)}")
                                table.addCell(expense.description.take(50) + if (expense.description.length > 50) "..." else "")
                            }

                            document.add(table)
                        }

                        document.close()
                        writer.close()
                    }
                }
                uri
            } else {
                // Use legacy file system for older Android versions
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!downloadsDir.exists()) {
                    downloadsDir.mkdirs()
                }

                val file = File(downloadsDir, "$BASE_FILENAME.pdf")
                val document = Document(PageSize.A4)
                val writer = PdfWriter.getInstance(document, file.outputStream())

                document.open()

                // Add title
                val titleFont = Font(Font.FontFamily.HELVETICA, 20f, Font.BOLD)
                val title = Paragraph("Expense Report", titleFont)
                title.alignment = Element.ALIGN_CENTER
                document.add(title)
                document.add(Paragraph(" "))

                // Add summary
                val summaryFont = Font(Font.FontFamily.HELVETICA, 16f, Font.BOLD)
                document.add(Paragraph("Summary", summaryFont))
                document.add(Paragraph("Total Expenses: $${String.format("%.2f", totalAmount)}"))
                document.add(Paragraph("Number of Expenses: $expenseCount"))
                document.add(Paragraph("Generated on: $currentDateTime"))
                document.add(Paragraph(" "))

                // Add expenses table
                if (expenses.isNotEmpty()) {
                    document.add(Paragraph("Expense Details", summaryFont))
                    document.add(Paragraph(" "))

                    val table = PdfPTable(5)
                    table.widthPercentage = 100f
                    table.setWidths(floatArrayOf(20f, 25f, 20f, 15f, 20f))

                    // Table headers
                    table.addCell("Date")
                    table.addCell("Title")
                    table.addCell("Category")
                    table.addCell("Amount")
                    table.addCell("Description")

                    // Table data - use sorted list to avoid repeated sorting
                    val sortedExpenses = expenses.sortedByDescending { it.date }
                    sortedExpenses.forEach { expense ->
                        table.addCell(expense.date.format(SHORT_DATE_FORMATTER))
                        table.addCell(expense.title)
                        table.addCell(expense.category.name)
                        table.addCell("$${String.format("%.2f", expense.amount)}")
                        table.addCell(expense.description.take(50) + if (expense.description.length > 50) "..." else "")
                    }

                    document.add(table)
                }

                document.close()
                writer.close()

                FileProvider.getUriForFile(context, FILE_PROVIDER_AUTHORITY, file)
            }

            uri?.let { fileUri ->
                val result = ExportResult.Success("Downloads/$BASE_FILENAME.pdf", fileUri, "PDF")
                // Show notification
                notificationManager.showExportSuccessNotification("$BASE_FILENAME.pdf", "PDF", fileUri)
                result
            } ?: ExportResult.Error("Failed to create PDF file")

        } catch (e: Exception) {
            val errorResult = ExportResult.Error("Failed to export PDF: ${e.message}")
            // Show error notification
            notificationManager.showExportFailureNotification("$BASE_FILENAME.pdf", e.message ?: "Unknown error")
            errorResult
        }
    }
    
    /**
     * Export expenses to CSV format
     * Saves to Downloads directory with filename: expense_report.csv
     */
    suspend fun exportToCSV(expenses: List<Expense>): ExportResult = withContext(Dispatchers.IO) {
        try {
            // Pre-sort expenses to avoid repeated sorting
            val sortedExpenses = expenses.sortedByDescending { it.date }
            
            val csvContent = buildString {
                // CSV Header
                appendLine("Date,Title,Category,Amount,Description")

                // CSV Data
                sortedExpenses.forEach { expense ->
                    append("${expense.date.format(ISO_DATE_FORMATTER)},")
                    append("\"${expense.title.replace("\"", "\"\"")}\",")
                    append("\"${expense.category.name}\",")
                    append("${expense.amount},")
                    appendLine("\"${expense.description.replace("\"", "\"\"")}\"")
                }
            }

            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Use MediaStore for Android 10+
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, "$BASE_FILENAME.csv")
                    put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }

                val uri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                uri?.let { fileUri ->
                    context.contentResolver.openOutputStream(fileUri)?.use { outputStream ->
                        outputStream.write(csvContent.toByteArray())
                    }
                }
                uri
            } else {
                // Use legacy file system for older Android versions
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!downloadsDir.exists()) {
                    downloadsDir.mkdirs()
                }

                val file = File(downloadsDir, "$BASE_FILENAME.csv")
                file.writeText(csvContent)

                FileProvider.getUriForFile(context, FILE_PROVIDER_AUTHORITY, file)
            }

            uri?.let { fileUri ->
                val result = ExportResult.Success("Downloads/$BASE_FILENAME.csv", fileUri, "CSV")
                // Show notification
                notificationManager.showExportSuccessNotification("$BASE_FILENAME.csv", "CSV", fileUri)
                result
            } ?: ExportResult.Error("Failed to create CSV file")

        } catch (e: Exception) {
            val errorResult = ExportResult.Error("Failed to export CSV: ${e.message}")
            // Show error notification
            notificationManager.showExportFailureNotification("$BASE_FILENAME.csv", e.message ?: "Unknown error")
            errorResult
        }
    }
    
    /**
     * Create share intent for PDF only (as per requirement)
     */
    fun createShareIntent(pdfUri: Uri): Intent {
        return Intent().apply {
            action = Intent.ACTION_SEND
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, pdfUri)
            putExtra(Intent.EXTRA_SUBJECT, "Expense Report")
            putExtra(Intent.EXTRA_TEXT, "Please find attached expense report.")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }
    
    /**
     * Fallback method to create a simple text report if PDF fails
     */
    suspend fun exportToTextReport(expenses: List<Expense>): ExportResult = withContext(Dispatchers.IO) {
        try {
            // Pre-calculate values to avoid repeated calculations
            val totalAmount = expenses.sumOf { it.amount }
            val expenseCount = expenses.size
            val currentDateTime = java.time.LocalDateTime.now().format(DATE_FORMATTER)
            val sortedExpenses = expenses.sortedByDescending { it.date }
            
            val reportContent = buildString {
                appendLine("EXPENSE REPORT")
                appendLine("=".repeat(50))
                appendLine()

                appendLine("SUMMARY")
                appendLine("-".repeat(20))
                appendLine("Total Expenses: $${String.format("%.2f", totalAmount)}")
                appendLine("Number of Expenses: $expenseCount")
                appendLine("Generated on: $currentDateTime")
                appendLine()

                if (sortedExpenses.isNotEmpty()) {
                    appendLine("EXPENSE DETAILS")
                    appendLine("-".repeat(50))
                    appendLine("Date\t\tTitle\t\tCategory\tAmount\tDescription")
                    appendLine("-".repeat(50))

                    sortedExpenses.forEach { expense ->
                        appendLine("${expense.date.format(SHORT_DATE_FORMATTER)}\t\t${expense.title.take(15)}\t\t${expense.category.name}\t$${String.format("%.2f", expense.amount)}\t${expense.description.take(30)}")
                    }
                }
            }

            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, "$BASE_FILENAME.txt")
                    put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }

                val uri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                uri?.let { fileUri ->
                    context.contentResolver.openOutputStream(fileUri)?.use { outputStream ->
                        outputStream.write(reportContent.toByteArray())
                    }
                }
                uri
            } else {
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!downloadsDir.exists()) {
                    downloadsDir.mkdirs()
                }

                val file = File(downloadsDir, "$BASE_FILENAME.txt")
                file.writeText(reportContent)

                FileProvider.getUriForFile(context, FILE_PROVIDER_AUTHORITY, file)
            }

            uri?.let { fileUri ->
                val result = ExportResult.Success("Downloads/$BASE_FILENAME.txt", fileUri, "TXT")
                // Show notification
                notificationManager.showExportSuccessNotification("$BASE_FILENAME.txt", "TXT", fileUri)
                result
            } ?: ExportResult.Error("Failed to create text report")

        } catch (e: Exception) {
            val errorResult = ExportResult.Error("Failed to export text report: ${e.message}")
            // Show error notification
            notificationManager.showExportFailureNotification("$BASE_FILENAME.txt", e.message ?: "Unknown error")
            errorResult
        }
    }

    /**
     * Check if external storage is available for writing
     */
    fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }
}

/**
 * Sealed class for export operation results
 */
sealed class ExportResult {
    data class Success(
        val filePath: String,
        val uri: Uri,
        val format: String
    ) : ExportResult()
    
    data class Error(val message: String) : ExportResult()
}
