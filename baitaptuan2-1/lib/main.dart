import 'package:flutter/material.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Thực hành 02',
      home: const PracticeScreen(),
      debugShowCheckedModeBanner: false,
    );
  }
}

class PracticeScreen extends StatefulWidget {
  const PracticeScreen({super.key});

  @override
  State<PracticeScreen> createState() => _PracticeScreenState();
}

class _PracticeScreenState extends State<PracticeScreen> {
  final TextEditingController _controller = TextEditingController();
  String? _errorMessage;
  List<int> _numbers = [];

  void _generateList() {
    final input = _controller.text.trim();
    final number = int.tryParse(input);

    if (number == null || number <= 0) {
      setState(() {
        _errorMessage = 'Dữ liệu bạn nhập không hợp lệ';
        _numbers = [];
      });
    } else {
      setState(() {
        _errorMessage = null;
        _numbers = List.generate(number, (index) => index + 1);
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Thực hành 02')),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            TextField(
              controller: _controller,
              keyboardType: TextInputType.number,
              decoration: const InputDecoration(
                labelText: 'Nhập số lượng',
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 12),
            ElevatedButton(
              onPressed: _generateList,
              child: const Text('Tạo'),
            ),
            const SizedBox(height: 12),
            if (_errorMessage != null)
              Text(
                _errorMessage!,
                style: const TextStyle(color: Colors.red),
              ),
            if (_numbers.isNotEmpty)
              Wrap(
                spacing: 8,
                children: _numbers
                    .map((num) => ElevatedButton(
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.red,
                  ),
                  onPressed: () {},
                  child: Text('$num'),
                ))
                    .toList(),
              ),
          ],
        ),
      ),
    );
  }
}
