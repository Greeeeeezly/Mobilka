package com.example.lab3

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.IOException

object FileUtils {
    private const val DIRECTORY_NAME = "Documents"  // Строка вместо Environment.DIRECTORY_DOCUMENTS
    private const val BACKUP_FILE_NAME = "backup_heroes.txt"

    fun saveHeroesToFile(context: Context, fileName: String, heroes: List<String>): File? {
        val file = File(context.getExternalFilesDir(DIRECTORY_NAME), fileName)
        return try {
            file.writeText(heroes.joinToString("\n"))
            file
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
    fun isFileExists(context: Context, fileName: String): Boolean {
        val file = File(context.getExternalFilesDir(DIRECTORY_NAME), fileName)
        Log.d("FileCheck", "File path: ${file.absolutePath}, exists: ${file.exists()}")
        return file.exists()
    }

    fun deleteFile(context: Context, fileName: String): Boolean {
        val file = File(context.getExternalFilesDir(DIRECTORY_NAME), fileName)
        return file.exists() && file.delete()
    }

    fun backupFileToInternal(context: Context, sourceFileName: String): Boolean {
        val sourceFile = File(context.getExternalFilesDir(DIRECTORY_NAME), sourceFileName)
        val backupFile = File(context.filesDir, BACKUP_FILE_NAME)
        Log.d("FileUtils", "Backup source file path: ${sourceFile.absolutePath}")
        Log.d("FileUtils", "Backup target file path: ${backupFile.absolutePath}")

        return try {
            if (sourceFile.exists()) {
                sourceFile.copyTo(backupFile, overwrite = true)
                Log.d("FileUtils", "Backup successful")
                true
            } else {
                Log.d("FileUtils", "Source file does not exist")
                false
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("FileUtils", "Error during backup: ${e.localizedMessage}")
            false
        }
    }

    fun restoreFile(context: Context, targetFileName: String): Boolean {
        val backupFile = File(context.filesDir, BACKUP_FILE_NAME)
        val restoredFile = File(context.getExternalFilesDir(DIRECTORY_NAME), targetFileName)
        return try {
            if (backupFile.exists()) {
                backupFile.copyTo(restoredFile, overwrite = true)
                true
            } else false
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }
}
