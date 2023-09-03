import 'package:flutter/services.dart';
import 'package:flutter_native/service/data_service.dart';

class FlutterMethodChannel {
  static const channelName = 'channel';
  late MethodChannel methodChannel;

  static final FlutterMethodChannel instance = FlutterMethodChannel._init();
  FlutterMethodChannel._init();

  void configureChannel() {
    methodChannel = MethodChannel(channelName);
    methodChannel.setMethodCallHandler(methodHandler); // set method handler
  }

  Future<void> methodHandler(MethodCall call) async {
    final String idea = call.arguments;
    const platform = MethodChannel('channel');
    bool isPlay = true;
   print("getting data from native");
    switch (call.method) {
      case "onPlayActionClicked": // this method name needs to be the same from invokeMethod in Android
        DataService.instance.addIdea('play'); // you can handle the data here. In this example, we will simply update the view via a data service
        try {
          await platform.invokeMethod('showPersistentNotification', {
            'title': 'Persistent Notification',
            'content': 'This is a persistent notification.',
            'isPlay': false
          });
        } on PlatformException catch (e) {
          print("Error: ${e.message}");
        }
        break;
      case "onPauseActionClicked": // this method name needs to be the same from invokeMethod in Android
        DataService.instance.addIdea('pause'); // you can handle the data here. In this example, we will simply update the view via a data service
        try {
        await platform.invokeMethod('showPersistentNotification', {
          'title': 'Persistent Notification',
          'content': 'This is a persistent notification.',
          'isPlay': true
        });
      } on PlatformException catch (e) {
        print("Error: ${e.message}");
      }
        break;
      case "onPreviousActionClicked": // this method name needs to be the same from invokeMethod in Android
        DataService.instance.addIdea('idea'); // you can handle the data here. In this example, we will simply update the view via a data service
        break;
      case "onNextActionClicked": // this method name needs to be the same from invokeMethod in Android
        DataService.instance.addIdea('idea'); // you can handle the data here. In this example, we will simply update the view via a data service
        break;
      default:
        print('no method handler for method ${call.method}');
    }
  }
}