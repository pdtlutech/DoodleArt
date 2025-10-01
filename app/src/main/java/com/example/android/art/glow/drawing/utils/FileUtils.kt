package com.example.android.art.glow.drawing.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.android.art.glow.drawing.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.UUID

object FileUtils {
    fun downloadImage(bitmap: Bitmap, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val fileName = "image_" + UUID.randomUUID() + ".png"  // Change the extension if needed (e.g., .png)
                var fos: OutputStream? = null

                // Check if the Android version is Q (API 29) or above
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // Use MediaStore to create and save the image in the "MyImages" folder
                    val resolver: ContentResolver = context.contentResolver
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/png")  // Change MIME type if necessary (e.g., "image/png")
                        // Save inside the "MyImages" folder in the Pictures directory
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/MyImages")
                    }
                    val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    if (imageUri != null) {
                        fos = resolver.openOutputStream(imageUri)
                    }
                } else {
                    // For SDK versions below 29, use traditional FileOutputStream
                    val imagesDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyImages")
                    if (!imagesDir.exists()) {
                        imagesDir.mkdirs()  // Create the folder if it doesn't exist
                    }
                    val image = File(imagesDir, fileName)
                    fos = FileOutputStream(image)
                }

                // If FileOutputStream is successfully initialized, compress and save the image
                if (fos != null) {
                    try {
                        // Compress the bitmap and save the image
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)  // Change to Bitmap.CompressFormat.PNG if saving PNG images
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, context.getString(R.string.saved_successfully), Toast.LENGTH_SHORT).show()
                        }
                    } finally {
                        fos.close()  // Close the output stream
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Error opening file output stream", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("DownloadImage", "Error saving image", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Save error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

   fun getAllImages(context: Context): List<String> {
        val imagePaths = mutableListOf<String>()

        val contentResolver = context.contentResolver
        val projection = arrayOf(MediaStore.Images.Media.DATA)

        // Define the selection to filter by the folder path "MyImages"
        val selection = "${MediaStore.Images.Media.RELATIVE_PATH} LIKE ?"
        val selectionArgs = arrayOf("%MyImages%")

        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null // No sorting
        )

        cursor?.use {
            val columnIndex = it.getColumnIndex(MediaStore.Images.Media.DATA)

            while (it.moveToNext()) {
                // Get the file path of each image
                val imagePath = it.getString(columnIndex)
                imagePaths.add(imagePath)
            }
        }

        return imagePaths
    }


    fun getAllImagesOlderVersion(): List<String> {
        val imagePaths = mutableListOf<String>()

        val myImagesDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyImages")

        // Check if the folder exists
        if (myImagesDir.exists() && myImagesDir.isDirectory) {
            // List all PNG files in the "MyImages" folder
            val files = myImagesDir.listFiles { file ->
                file.isFile && (file.name.endsWith(".png") || file.name.endsWith(".jpg") || file.name.endsWith(".jpeg"))
            }

            // Add the paths of the files to the list
            files?.forEach { file ->
                imagePaths.add(file.absolutePath)
            }
        }

        return imagePaths
    }

    fun deleteFileByPath(filePath: String): Boolean {
        val file = File(filePath)

        // Check if the file exists and is a valid file
        if (file.exists() && file.isFile) {
            // Try to delete the file
            return file.delete()
        }

        // Return false if file doesn't exist or isn't a valid file
        return false
    }

    fun shareVideoOrAudio(context: Context, title: String?, filePaths: List<String>?) {
        val uris = ArrayList<Uri>()
        if(filePaths.isNullOrEmpty()) {
            Log.e("FileUtils", "filePaths is null or empty.")
        } else {
            for(path in filePaths) {
                val file = File(path)
                if (file.exists()) {
                    try {
                        val uri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.provider",
                            file
                        )
                        if(!uris.contains(uri)) {
                            uris.add(uri)
                        }

                    } catch (e: Exception) {
                        Log.e("Share", "Failed to get URI for file: $path", e)
                    }
                } else {
                    Log.w("Share", "File does not exist: $path")
                }
            }
        }

        if (uris.isEmpty()) {
            Log.e("Share", "No valid files to share.")
            return
        }

        val shareIntent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
            type = "*/*"
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TITLE, title)
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val chooser = Intent.createChooser(shareIntent, "Share files via")
        if (context !is Activity) {
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            context.startActivity(chooser)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}