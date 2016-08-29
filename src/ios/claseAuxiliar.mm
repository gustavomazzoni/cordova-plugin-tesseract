//
//  claseAuxiliar.m
//
//  @author Gustavo Mazzoni - 2016.
//

#import "claseAuxiliar.h"


#import <TesseractOCR/TesseractOCR.h>

static inline double radians (double degrees) {return degrees * M_PI/180;}

@implementation claseAuxiliar

-(id)init {
    self = [super init];
    if (self) {
        // Set up the tessdata path. This is included in the application bundle
        // but is copied to the Documents directory on the first run.
        NSArray *documentPaths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
        NSString *documentPath = ([documentPaths count] > 0) ? [documentPaths objectAtIndex:0] : nil;
        
        NSString *dataPath = [documentPath stringByAppendingPathComponent:@"tessdata"];
        NSFileManager *fileManager = [NSFileManager defaultManager];
        // If the expected store doesn't exist, copy the default store.
        if (![fileManager fileExistsAtPath:dataPath]) {
            // get the path to the app bundle (with the tessdata dir)
            NSString *bundlePath = [[NSBundle mainBundle] bundlePath];
            NSString *tessdataPath = [bundlePath stringByAppendingPathComponent:@"tessdata"];
            if (tessdataPath) {
                [fileManager copyItemAtPath:tessdataPath toPath:dataPath error:NULL];
            }
        }
        
        setenv("TESSDATA_PREFIX", [[documentPath stringByAppendingString:@"/"] UTF8String], 1);
    }
    return self;
}


- (NSString *) ocrImage: (UIImage *) uiImage withLanguage: (NSString *) language
{
    //### Using Tesseract OCR iOS ###
    //@see @source https://github.com/gali8/Tesseract-OCR-iOS
    //@see wiki https://github.com/gali8/Tesseract-OCR-iOS/wiki/Using-Tesseract-OCR-iOS
    
    // Create your G8Tesseract object using the initWithLanguage method:
    tesseract = [[G8Tesseract alloc] initWithLanguage:language];
    
    // Set up the delegate to receive Tesseract's callbacks.
    // self should respond to TesseractDelegate and implement a
    // "- (BOOL)shouldCancelImageRecognitionForTesseract:(G8Tesseract *)tesseract"
    // method to receive a callback to decide whether or not to interrupt
    // Tesseract before it finishes a recognition.
    tesseract.delegate = self;
    
    // Specify the image Tesseract should recognize on
    tesseract.image = [uiImage g8_blackAndWhite];
    
    // Start the recognition
    NSLog(@"Recognizing text...");
    [tesseract recognize];
    
    // Retrieve the recognized text
    NSLog(@"Converted text: %@", [tesseract recognizedText]);
    
    return [tesseract recognizedText];
}


// *** Method not used!
//http://www.iphonedevsdk.com/forum/iphone-sdk-development/7307-resizing-photo-new-uiimage.html#post33912
-(UIImage *)resizeImage:(UIImage *)image {
    
    CGImageRef imageRef = [image CGImage];
    CGImageAlphaInfo alphaInfo = CGImageGetAlphaInfo(imageRef);
    CGColorSpaceRef colorSpaceInfo = CGColorSpaceCreateDeviceRGB();
    
    if (alphaInfo == kCGImageAlphaNone)
        alphaInfo = kCGImageAlphaNoneSkipLast;
    
    int width, height;
    
    width = 640;//[image size].width;
    height = 640;//[image size].height;
    
    CGContextRef bitmap;
    
    if (image.imageOrientation == UIImageOrientationUp | image.imageOrientation == UIImageOrientationDown) {
        bitmap = CGBitmapContextCreate(NULL, width, height, CGImageGetBitsPerComponent(imageRef), CGImageGetBytesPerRow(imageRef), colorSpaceInfo, alphaInfo);
        
    } else {
        bitmap = CGBitmapContextCreate(NULL, height, width, CGImageGetBitsPerComponent(imageRef), CGImageGetBytesPerRow(imageRef), colorSpaceInfo, alphaInfo);
        
    }
    
    if (image.imageOrientation == UIImageOrientationLeft) {
        NSLog(@"image orientation left");
        CGContextRotateCTM (bitmap, radians(90));
        CGContextTranslateCTM (bitmap, 0, -height);
        
    } else if (image.imageOrientation == UIImageOrientationRight) {
        NSLog(@"image orientation right");
        CGContextRotateCTM (bitmap, radians(-90));
        CGContextTranslateCTM (bitmap, -width, 0);
        
    } else if (image.imageOrientation == UIImageOrientationUp) {
        NSLog(@"image orientation up"); 
        
    } else if (image.imageOrientation == UIImageOrientationDown) {
        NSLog(@"image orientation down");   
        CGContextTranslateCTM (bitmap, width,height);
        CGContextRotateCTM (bitmap, radians(-180.));
        
    }
    
    CGContextDrawImage(bitmap, CGRectMake(0, 0, width, height), imageRef);
    CGImageRef ref = CGBitmapContextCreateImage(bitmap);
    UIImage *result = [UIImage imageWithCGImage:ref];
    
    CGContextRelease(bitmap);
    CGImageRelease(ref);
    
    return result;  
}



@end
