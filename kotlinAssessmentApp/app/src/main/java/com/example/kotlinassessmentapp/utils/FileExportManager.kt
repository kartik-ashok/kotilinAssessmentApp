package com.example.kotlinassessmentapp.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import com.example.kotlinassessmentapp.data.model.Expense
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
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
    }
    
    /**
     * Export expenses to PDF format
     * Saves to Downloads directory with filename: expense_report.pdf
     */
    suspend fun exportToPDF(expenses: List<Expense>): ExportResult = withContext(Dispatchers.IO) {
        try {
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!downloadsDir.exists()) {
                downloadsDir.mkdirs()
            }
            
            val file = File(downloadsDir, "$BASE_FILENAME.pdf")
            val pdfWriter = PdfWriter(file)
            val pdfDocument = PdfDocument(pdfWriter)
            val document = Document(pdfDocument)
            
            // Add title
            document.add(
                Paragraph("Expense Report")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(20f)
                    .setBold()
            )
            
            document.add(Paragraph("\n"))
            
            // Add summary
            val totalAmount = expenses.sumOf { it.amount }
            val expenseCount = expenses.size
            
            document.add(
                Paragraph("Summary")
                    .setFontSize(16f)
                    .setBold()
            )
            document.add(Paragraph("Total Expenses: $${String.format("%.2f", totalAmount)}"))
            document.add(Paragraph("Number of Expenses: $expenseCount"))
            document.add(Paragraph("Generated on: ${java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' HH:mm"))}"))
            
            document.add(Paragraph("\n"))
            
            // Add expenses table
            if (expenses.isNotEmpty()) {
                document.add(
                    Paragraph("Expense Details")
                        .setFontSize(16f)
                        .setBold()
                )
                
                val table = Table(UnitValue.createPercentArray(floatArrayOf(20f, 25f, 20f, 15f, 20f)))
                    .setWidth(UnitValue.createPercentValue(100f))
                
                // Table headers
                table.addHeaderCell("Date")
                table.addHeaderCell("Title")
                table.addHeaderCell("Category")
                table.addHeaderCell("Amount")
                table.addHeaderCell("Description")
                
                // Table data
                expenses.sortedByDescending { it.date }.forEach { expense ->
                    table.addCell(expense.date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")))
                    table.addCell(expense.title)
                    table.addCell(expense.category.name)
                    table.addCell("$${String.format("%.2f", expense.amount)}")
                    table.addCell(expense.description.take(50) + if (expense.description.length > 50) "..." else "")
                }
                
                document.add(table)
            }
            
            document.close()
            
            val uri = FileProvider.getUriForFile(
                context,
                FILE_PROVIDER_AUTHORITY,
                file
            )
            
            ExportResult.Success(file.absolutePath, uri, "PDF")
            
        } catch (e: Exception) {
            ExportResult.Error("Failed to export PDF: ${e.message}")
        }
    }
    
    /**
     * Export expenses to CSV format
     * Saves to Downloads directory with filename: expense_report.csv
     */
    suspend fun exportToCSV(expenses: List<Expense>): ExportResult = withContext(Dispatchers.IO) {
        try {
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!downloadsDir.exists()) {
                downloadsDir.mkdirs()
            }
            
            val file = File(downloadsDir, "$BASE_FILENAME.csv")
            val writer = FileWriter(file)
            
            // CSV Header
            writer.append("Date,Title,Category,Amount,Description\n")
            
            // CSV Data
            expenses.sortedByDescending { it.date }.forEach { expense ->
                writer.append("${expense.date.format(DateTimeFormatter.ISO_LOCAL_DATE)},")
                writer.append("\"${expense.title.replace("\"", "\"\"")}\",")
                writer.append("\"${expense.category.name}\",")
                writer.append("${expense.amount},")
                writer.append("\"${expense.description.replace("\"", "\"\"")}\"\n")
            }
            
            writer.close()
            
            val uri = FileProvider.getUriForFile(
                context,
                FILE_PROVIDER_AUTHORITY,
                file
            )
            
            ExportResult.Success(file.absolutePath, uri, "CSV")
            
        } catch (e: Exception) {
            ExportResult.Error("Failed to export CSV: ${e.message}")
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
