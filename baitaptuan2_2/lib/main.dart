import 'package:flutter/material.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Thực hành 03',
      home: const EmailCheckScreen(),
      debugShowCheckedModeBanner: false,
    );
  }
}

class EmailCheckScreen extends StatefulWidget {
  const EmailCheckScreen({super.key});

  @override
  State<EmailCheckScreen> createState() => _EmailCheckScreenState();
}

class _EmailCheckScreenState extends State<EmailCheckScreen> {
  final TextEditingController _emailController = TextEditingController();
  String? _message;

  void _checkEmail() {
    final email = _emailController.text.trim();

    if (email.isEmpty) {
      setState(() {
        _message = 'Email không hợp lệ';
      });
    } else if (!email.contains('@')) {
      setState(() {
        _message = 'Email không đúng định dạng';
      });
    } else {
      setState(() {
        _message = 'Bạn đã nhập email hợp lệ';
      });
    }
  }
  // void _checkEmail() {
  //   final email = _emailController.text.trim();
  //
  //   if (email.isEmpty) {
  //     setState(() {
  //       _message = 'Email không hợp lệ';
  //     });
  //   } else {
  //     // Biểu thức kiểm tra: ít nhất 1 ký tự trước và sau @, có dấu chấm sau @
  //     final emailRegex = RegExp(r'^[^@]+@[^@]+\.[^@]+$');
  //
  //     if (!emailRegex.hasMatch(email)) {
  //       setState(() {
  //         _message = 'Email không đúng định dạng';
  //       });
  //     } else {
  //       setState(() {
  //         _message = 'Bạn đã nhập email hợp lệ';
  //       });
  //     }
  //   }
  // }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Thực hành 03')),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            TextField(
              controller: _emailController,
              decoration: const InputDecoration(
                labelText: 'Email',
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 12),
            ElevatedButton(
              onPressed: _checkEmail,
              child: const Text('Kiểm tra'),
            ),
            const SizedBox(height: 12),
            if (_message != null)
              Text(
                _message!,
                style: TextStyle(
                  color: _message == 'Bạn đã nhập email hợp lệ'
                      ? Colors.green
                      : Colors.red,
                ),
              ),
          ],
        ),
      ),
    );
  }
}
