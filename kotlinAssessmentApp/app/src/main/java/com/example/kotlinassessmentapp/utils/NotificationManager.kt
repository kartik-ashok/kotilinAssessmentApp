package com.example.kotlinassessmentapp.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.kotlinassessmentapp.R

/**
 * NotificationManager for Export Completion Notifications
 * 
 * Features:
 * - Shows notification when file export is complete
 * - Allows user to open the exported file directly
 * - Handles notification channels for Android 8.0+
 * - Different notification styles for PDF, CSV, and TXT files
 */
class ExportNotificationManager(private val context: Context) {
    
    companion object {
        private const val CHANNEL_ID = "export_notifications"
        private const val CHANNEL_NAME = "Export Notifications"
        private const val CHANNEL_DESCRIPTION = "Notifications for completed file exports"
        private const val NOTIFICATION_ID_BASE = 1000
    }
    
    private val notificationManager = NotificationManagerCompat.from(context)
    
    init {
        createNotificationChannel()
    }
    
    /**
     * Create notification channel for Android 8.0+
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESCRIPTION
                enableVibration(true)
                setShowBadge(true)
            }
            
            val systemNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            systemNotificationManager.createNotificationChannel(channel)
        }
    }
    
    /**
     * Show notification for successful export
     */
    fun showExportSuccessNotification(
        fileName: String,
        fileType: String,
        fileUri: Uri
    ) {
        val intent = createOpenFileIntent(fileUri, fileType)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        // Optimize notification building by reducing string operations
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(getNotificationIcon(fileType))
            .setContentTitle("Export Complete")
            .setContentText("$fileName saved to Downloads")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .addAction(
                getActionIcon(fileType),
                "Open File",
                pendingIntent
            )
            .build()
        
        val notificationId = NOTIFICATION_ID_BASE + System.currentTimeMillis().toInt()
        
        try {
            if (hasNotificationPermission()) {
                notificationManager.notify(notificationId, notification)
            }
        } catch (e: SecurityException) {
            // Handle case where notification permission is not granted
            // Silently fail - user will still see toast message
        }
    }
    
    /**
     * Show notification for export failure
     */
    fun showExportFailureNotification(fileName: String, errorMessage: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_notify_error)
            .setContentTitle("Export Failed")
            .setContentText("Failed to export $fileName")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ERROR)
            .build()
        
        val notificationId = NOTIFICATION_ID_BASE + System.currentTimeMillis().toInt()
        
        try {
            if (hasNotificationPermission()) {
                notificationManager.notify(notificationId, notification)
            }
        } catch (e: SecurityException) {
            // Handle case where notification permission is not granted
            // Silently fail - user will still see toast message
        }
    }
    
    /**
     * Create intent to open the exported file
     */
    private fun createOpenFileIntent(fileUri: Uri, fileType: String): Intent {
        val mimeType = when (fileType.uppercase()) {
            "PDF" -> "application/pdf"
            "CSV" -> "text/csv"
            "TXT" -> "text/plain"
            else -> "*/*"
        }
        
        return Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(fileUri, mimeType)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
    
    /**
     * Get appropriate notification icon based on file type
     */
    private fun getNotificationIcon(fileType: String): Int {
        return when (fileType.uppercase()) {
            "PDF" -> android.R.drawable.stat_sys_download_done
            "CSV" -> android.R.drawable.stat_sys_download_done
            "TXT" -> android.R.drawable.stat_sys_download_done
            else -> android.R.drawable.stat_sys_download_done
        }
    }
    
    /**
     * Get appropriate action icon based on file type
     */
    private fun getActionIcon(fileType: String): Int {
        return when (fileType.uppercase()) {
            "PDF" -> android.R.drawable.ic_menu_view
            "CSV" -> android.R.drawable.ic_menu_view
            "TXT" -> android.R.drawable.ic_menu_view
            else -> android.R.drawable.ic_menu_view
        }
    }
    
    /**
     * Check if notification permission is granted (Android 13+)
     */
    fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            notificationManager.areNotificationsEnabled()
        }
    }
}
