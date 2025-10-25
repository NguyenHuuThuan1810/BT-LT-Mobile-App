package com.example.btvntuan4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.btvntuan4.ui.theme.BTVNTuan4Theme

// ================== Các lớp mô hình ==================

// Tính trừu tượng
abstract class Person(open var name: String) {
    abstract fun showInfo(): String
}

// Tính kế thừa
class Student(
    override var name: String,
    private val borrowedBooks: MutableList<Book> = mutableListOf()
) : Person(name) {

    // Tính đóng gói
    fun borrowBook(book: Book) {
        borrowedBooks.add(book)
    }

    fun clearBorrowedBooks() {
        borrowedBooks.clear()
    }

    fun getBorrowedBooks(): List<Book> = borrowedBooks

    override fun showInfo(): String {
        return "Sinh viên: $name, Số sách đã mượn: ${borrowedBooks.size}"
    }
}

// Tính đa hình
open class Book(open val title: String) {
    open fun getInfo(): String = "Sách: $title"
}

class Novel(title: String) : Book(title) {
    override fun getInfo(): String = "Tiểu thuyết: $title"
}

class TextBook(title: String) : Book(title) {
    override fun getInfo(): String = "Giáo trình: $title"
}

class LibraryManager {
    private val students = mutableListOf<Student>()
    private val books = mutableListOf<Book>()

    fun addStudent(student: Student) {
        students.add(student)
    }

    fun addBook(book: Book) {
        books.add(book)
    }

    fun getStudents(): List<Student> = students
    fun getBooks(): List<Book> = books
}

//  Giao diện

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BTVNTuan4Theme {
                LibraryApp()
            }
        }
    }
}

@Composable
fun LibraryApp() {
    val manager = remember { LibraryManager() }

    // Dữ liệu mẫu
    val allBooks = listOf(
        Novel("Toàn trí độc giả"),
        TextBook("Sách lập trình Android"),
        Novel("Nhân vật ngoài lề tiểu thuyết")
    )

    allBooks.forEach { manager.addBook(it) }

    val studentA = Student("Nguyễn Hữu Thuận")
    val studentB = Student("Nguyễn Ngọc Thiện")
    val studentC = Student("Nguyễn Gia Huy")

    studentA.borrowBook(allBooks[0])
    studentA.borrowBook(allBooks[1])
    studentA.borrowBook(allBooks[2])
    studentB.borrowBook(allBooks[0])

    manager.addStudent(studentA)
    manager.addStudent(studentB)
    manager.addStudent(studentC)

    var selectedStudent by remember { mutableStateOf(studentA) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        // Tiêu đề
        Text(
            "Hệ thống Quản lý Thư viện",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(80.dp))

        // Tên sinh viên
        Text(
            "Sinh viên:",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ),
            modifier = Modifier.align(Alignment.Start)
        )

        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                elevation = CardDefaults.cardElevation(0.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.size(210.dp, 50.dp)
            ) {
                Text(
                    text = selectedStudent.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(10.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {
                selectedStudent = when (selectedStudent.name) {
                    "Nguyễn Hữu Thuận" -> studentB
                    "Nguyễn Ngọc Thiện" -> studentC
                    else -> studentA
                }
            }) {
                Text("Thay đổi")
            }
        }

        Spacer(Modifier.height(10.dp))

        // Danh sách các sách
        Text(
            "Danh sách sách:",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ),
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(12.dp)
        ) {
            val borrowed = selectedStudent.getBorrowedBooks()
            if (borrowed.isEmpty()) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Bạn chưa mượn quyển sách nào.\n")
                        }
                        append("Nhấn \"Thêm\" để bắt đầu hành trình đọc sách!")
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )
            } else {
                LazyColumn {
                    items(borrowed) { book ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                // Icon dấu tích
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .background(Color(0xFF861A12), shape = RoundedCornerShape(6.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                // Text hiển thị tên sách
                                Text(
                                    text = book.getInfo(),
                                    modifier = Modifier.padding(16.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                val newBook = manager.getBooks().random()
                selectedStudent.borrowBook(newBook)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Thêm")
        }
    }
}
