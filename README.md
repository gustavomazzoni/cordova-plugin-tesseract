# Cordova Plugin OCR Tesseract - For Android and iOS

## Using this plugin

### 1. After downloading this project, copy it to your app root folder and run ionic command to add the plugin:
```bash
$ ionic plugin add cordova-plugin-tesseract
```

### 2. For Android platform:

#### 2.1 Inside android platform, add (paste) folder 'tess-two' (lib tesseract project) and add it as a dependency project:

* Copy and paste tess-two folder to .../platforms/android/
* Open .../platforms/android/build.gradle
* Add to 'dependencies {':
```bash
// SUB-PROJECT DEPENDENCIES END
compile project(':tess-two')
```


#### 2.2. For the Ionic build to work with the tess-two library is necessary to edit the file that generates settings.gradle file:

* Open .../platforms/android/cordova/lib/build.js
* Edit function 'fs.writeFileSync()' (line 273):
```bash
//##### EDITED - Added tess-two library dependency
fs.writeFileSync(path.join(projectPath, 'settings.gradle'),
    '// GENERATED FILE - DO NOT EDIT\n' +
    'include ":"\n' + settingsGradlePaths.join('') +
    'include ":tess-two"');
```

### 3. For iOS platform:

#### 3.1 Inside ios platform, create Podfile, add 'TesseractOCRiOS' dependency and install POD:

* Create .../platforms/ios/Podfile
* Add 'TesseractOCRiOS' dependency (replace 'ocr-translation' with the name of your project):
```bash
source 'https://github.com/CocoaPods/Specs.git'
xcodeproj 'ocr-translation.xcodeproj/'

target 'ocr-translation' do

	pod 'TesseractOCRiOS', '4.0.0'

end
```
* Inside root directory of ios platform, install the dependencies (install the CocoaPods in case you don't have it - https://cocoapods.org/) using the following commands:
```bash
$ pod install
```


Your project is ready to use this plugin.
