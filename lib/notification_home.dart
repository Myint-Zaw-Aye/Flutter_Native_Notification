import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_native/service/data_service.dart';

class MyHomePage extends StatefulWidget {
  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
    static const platform = MethodChannel('channel');
  Future<void> methodHandler(MethodCall call) async {
    final String idea = call.arguments;
    switch (call.method) {
      case "onPlayActionClicked": // this method name needs to be the same from invokeMethod in Android
        DataService.instance.addIdea(idea); // you can handle the data here. In this example, we will simply update the view via a data service
        break;
      default:
        print('no method handler for method ${call.method}');
    }
  }

  @override
  void initState() {
    super.initState();
   // platform.setMethodCallHandler(methodHandler); // set method handler
  }

  @override
  Widget build(BuildContext context) {
    
    return Scaffold(
      appBar: AppBar(
        title: Text('Persistent Notification Example'),
      ),
      body: Column(
        children: [
          StreamBuilder(
            stream: DataService.instance.ideaController.stream,
            builder: (context, snapshot) {
              if (snapshot.data != null) {
                return Text(
                  snapshot.data!,
                  style: Theme.of(context).textTheme.headline4,
                );
              }

              return Text(
                "Waiting for new idea",
                style: Theme.of(context).textTheme.headline4,
              );
            }),

          Center(
            child: ElevatedButton(
              onPressed: () {
                showPersistentNotification();
              },
              child: Text('Show Persistent Notification'),
            ),
          ),
        ],
      ),
    );
  }

  void showPersistentNotification() async {
    try {
      await platform.invokeMethod('showPersistentNotification', {
        'title': 'Persistent Notification',
        'content': 'This is a persistent notification.',
        'isPlay': true
      });
    } on PlatformException catch (e) {
      print("Error: ${e.message}");
    }
  }
}
