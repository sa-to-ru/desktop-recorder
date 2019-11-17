# Overview
Javaで実装したWindowsデスクトップの動画キャプチャツールです。
JavaCV(OpenCV, FFmpeg)を使って、MPEG4でエンコーディングした動画ファイルを生成します。

# Usage
## 単体ツールとして使用する場合
```
java -jar desktop-recorder-xxx.jar
```
## Javaプログラム内から利用する場合
```
DesktopRecorder recorder = new DesktopRecorder("c:/temp/capture"); // 作成されるファイルはc:/temp/capture.mp4
recorder.start();
...
recorder.stop();
