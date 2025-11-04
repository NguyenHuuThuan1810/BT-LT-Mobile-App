package com.example.btvntuan4_2.screens

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object ApiClient {
    private const val BASE = "https://amock.io/api/researchUTH"

    suspend fun fetchTasks(): Result<List<Task>> = withContext(Dispatchers.IO) {
        try {
            val url = URL("$BASE/tasks")
            val conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = 10_000
                readTimeout = 10_000
            }
            val code = conn.responseCode
            val reader = if (code in 200..299) conn.inputStream else conn.errorStream
            val text = BufferedReader(InputStreamReader(reader)).use { it.readText() }
            Log.d("ApiClient", "fetchTasks response: $text")

            if (code in 200..299) {
                val root = JSONObject(text)
                val arr = root.optJSONArray("data") ?: JSONArray()
                val list = mutableListOf<Task>()
                for (i in 0 until arr.length()) {
                    val o = arr.getJSONObject(i)
                    list.add(jsonToTask(o))
                }
                Result.success(list)
            } else {
                Result.failure(Exception("HTTP $code: $text"))
            }
        } catch (e: Exception) {
            Log.e("ApiClient", "fetchTasks error", e)
            Result.failure(e)
        }
    }

    suspend fun fetchTask(taskId: String): Result<Task> = withContext(Dispatchers.IO) {
        try {
            // Lấy toàn bộ danh sách task
            val allTasksResult = fetchTasks()
            if (allTasksResult.isFailure) return@withContext Result.failure(allTasksResult.exceptionOrNull()!!)
            val allTasks = allTasksResult.getOrThrow()

            // Tìm task theo ID
            val task = allTasks.find { it.id.toString() == taskId }
            if (task != null) {
                Result.success(task)
            } else {
                Result.failure(Exception("Task ID $taskId not found in list"))
            }
        } catch (e: Exception) {
            Log.e("ApiClient", "fetchTask error", e)
            Result.failure(e)
        }
    }


    suspend fun deleteTask(taskId: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val url = URL("$BASE/task/$taskId")
            val conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "DELETE"
                connectTimeout = 10_000
                readTimeout = 10_000
            }
            val code = conn.responseCode
            if (code in 200..299) {
                Result.success(true)
            } else {
                val text = BufferedReader(InputStreamReader(conn.errorStream)).use { it.readText() }
                Result.failure(Exception("HTTP $code: $text"))
            }
        } catch (e: Exception) {
            Log.e("ApiClient", "deleteTask error", e)
            Result.failure(e)
        }
    }

    private fun jsonToTask(o: JSONObject): Task {
        // Hiển thị SubtaskList
        val subtasksList = mutableListOf<Subtask>()
        val sArr = o.optJSONArray("subtasks")
        if (sArr != null) {
            for (i in 0 until sArr.length()) {
                val so = sArr.optJSONObject(i) ?: continue
                subtasksList.add(
                    Subtask(
                        id = so.optString("id", so.optString("subtaskId", "")),
                        title = so.optString("title", ""),
                        isCompleted = so.optBoolean("isCompleted", so.optBoolean("completed", false))
                    )
                )
            }
        }

        // Hiển thị Attachment
        val attachmentsList = mutableListOf<Attachment>()
        val aArr = o.optJSONArray("attachments")
        if (aArr != null) {
            for (i in 0 until aArr.length()) {
                val ao = aArr.optJSONObject(i) ?: continue
                attachmentsList.add(
                    Attachment(
                        id = ao.optString("id", ao.optString("attachmentId", "")),
                        fileName = ao.optString("fileName", ao.optString("name", "")),
                        fileUrl = ao.optString("fileUrl", ao.optString("url", ""))
                    )
                )
            }
        }

        // Hiện thị đúng Status
        val statusRaw = o.opt("status")
        val statusValue = when (statusRaw) {
            -1, "-1" -> "Pending"
            0, "0" -> "In Progress"
            1, "1" -> "Completed"
            else -> statusRaw?.toString() ?: "-"
        }

        return Task(
            id = o.optString("id", o.optString("taskId", "")),
            title = o.optString("title", "No title"),
            description = o.optString("description", "-"),
            status = statusValue,
            priority = o.optString("priority", "-"),
            category = o.optString("category", "-"),
            dueTime = o.optString("dueDate", o.optString("dueTime", "-")),
            subtasks = if (subtasksList.isEmpty()) null else subtasksList,
            attachments = if (attachmentsList.isEmpty()) null else attachmentsList
        )
    }
}
