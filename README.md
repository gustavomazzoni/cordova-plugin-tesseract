# Cordova Tesseract-OCR Plugin - For Android and iOS

This is a Cordova/Ionic plugin for OCR process using Tesseract library for both Android and iOS. [Tesseract](https://github.com/tesseract-ocr/tesseract) is an Open Source library for OCR (Optical Character Recognition) process.

This plugin defines a global `TesseractPlugin` object, which provides an API for recognizing text on images.
```bash
  TesseractPlugin.recognizeText(imageData, language, function(recognizedText) {
    deferred.resolve(recognizedText);
  }, function(reason) {
    deferred.reject('Error on recognizing text from image. ' + reason);
  });
```

## Installation

### Before installing this plugin, make sure you have added the platform for your app:
```bash
$ ionic platform add android
```
-> substitute android with ios to build for iOS.

-> substitute ionic with cordova to build on cordova.

### 1. Download or clone this project, copy it to your app root folder and run ionic command to add the plugin:

#### Ionic 
```bash
$ git clone https://github.com/gustavomazzoni/cordova-plugin-tesseract
$ cp -rf cordova-plugin-tesseract your-project/cordova-plugin-tesseract
$ cd your-project/
$ ionic plugin add cordova-plugin-tesseract
```

#### Cordova

```bash
$ cordova plugin add https://github.com/gustavomazzoni/cordova-plugin-tesseract
```


### 2. For Android platform:

#### 2.5 Your project is ready to use this plugin on Android platform. Build your project:
```bash
$ ionic build android
```

### 3. For iOS platform:

#### 3.1 Inside root directory of your ios platform, create Podfile and add [Tesseract OCR iOS](https://github.com/gali8/Tesseract-OCR-iOS) (Tesseract library for iOS7+) as a dependency:

* Create `your-project/platforms/ios/Podfile`
* Add 'TesseractOCRiOS' dependency (replace 'ocr-translation' with the name of your project):
```bash
source 'https://github.com/CocoaPods/Specs.git'
xcodeproj 'ocr-translation.xcodeproj/'

target 'ocr-translation' do

	pod 'TesseractOCRiOS', '4.0.0'

end
```

#### 3.2 Still at your ios platform folder, install the dependencies ([install the CocoaPods](https://cocoapods.org/) in case you don't have it yet) using the following commands:
```bash
$ pod install
```

#### 3.3 Your project is ready to use this plugin on iOS platform. Build your project:
```bash
$ ionic build ios
```

Your project is ready to use this plugin.

## Usage
cordova-plugin-tesseract is designed to recognize text in images in many languages, but for that to work we need to have the tessdata of the language you want the text to be recognized.

To use this plugin and recognize text in images, you need to:

### 1. Download the language
As soon as you enter on your OCR use case, call `TesseractPlugin.loadLanguage` function to download the tessdata for your language.

Language must be in format like `eng` . For a list of compatible languages check [this link](https://github.com/tesseract-ocr/tessdata/tree/3.04.00).

```bash
TesseractPlugin.loadLanguage(language, function(response) {
  deferred.resolve(response);
}, function(reason) {
  deferred.reject('Error on loading OCR file for your language. ' + reason);
});
```

### 2. Get image data from your photo
Load the image you want the text to be recognized from. On your angular Controller use [`$cordovaCamera`](http://ngcordova.com/docs/plugins/camera/) or [`cordova-plugin-camera`](https://github.com/apache/cordova-plugin-camera) plugin to take the photo or load an image:
```bash
$cordovaCamera.getPicture(options).then(function(imageData) {
  $scope.image = "data:image/jpeg;base64," + imageData;
  $scope.text = null;

  $timeout(function() {
    // DOM has finished rendering
    // insert here the call to TesseractPlugin.recognizeText function to recognize the text
    
  });
}, function(err) {
  // error
  console.log('ERROR with camera plugin. Error: ' + err);
});
```

### 3. Recognize text from image
Then, after loaded the image, just call `TesseractPlugin.recognizeText` function with the image data, the language of the text in the image and a callback function to be called after the operation is done.
```bash
TesseractPlugin.recognizeText(imageData, language, function(recognizedText) {
  $scope.text = recognizedText;
}, function(reason) {
  console.log('Error on recognizing text from image. ' + reason);
});
```

