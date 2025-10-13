import 'package:flutter/material.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Thực hành 01',
      home: const AgeCheckScreen(),
      debugShowCheckedModeBanner: false,
    );
  }
}

class AgeCheckScreen extends StatefulWidget {
  const AgeCheckScreen({super.key});

  @override
  State<AgeCheckScreen> createState() => _AgeCheckScreenState();
}

class _AgeCheckScreenState extends State<AgeCheckScreen> {
  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _ageController = TextEditingController();
  String? _result;

  void _checkAgeCategory() {
    final name = _nameController.text.trim();
    final ageText = _ageController.text.trim();
    final age = int.tryParse(ageText);

    if (name.isEmpty || age == null || age < 0) {
      setState(() {
        _result = 'Thông tin không hợp lệ';
      });
      return;
    }

    String category;
    if (age < 2) {
      category = 'Em bé';
    } else if (age < 6) {
      category = 'Trẻ em';
    } else if (age <= 65) {
      category = 'Người lớn';
    } else {
      category = 'Người già';
    }

    setState(() {
      _result = 'Họ tên: $name\nTuổi: $age\nPhân loại: $category';
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Thực hành 01')),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            TextField(
              controller: _nameController,
              decoration: const InputDecoration(
                labelText: 'Họ và tên',
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 12),
            TextField(
              controller: _ageController,
              keyboardType: TextInputType.number,
              decoration: const InputDecoration(
                labelText: 'Tuổi',
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 12),
            ElevatedButton(
              onPressed: _checkAgeCategory,
              child: const Text('Kiểm tra'),
            ),
            const SizedBox(height: 12),
            if (_result != null)
              Text(
                _result!,
                style: const TextStyle(fontSize: 16),
              ),
          ],
        ),
      ),
    );
  }
}
