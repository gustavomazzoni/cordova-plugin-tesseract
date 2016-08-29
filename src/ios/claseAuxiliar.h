//
//  claseAuxiliar.h
//
//  @author Gustavo Mazzoni - 2016.
//

#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>



#import <TesseractOCR/TesseractOCR.h>

@interface claseAuxiliar : NSObject <G8TesseractDelegate> {
 
    
    G8Tesseract *tesseract;

    
}


-(NSString *) ocrImage: (UIImage *) uiImage withLanguage: (NSString *) language;
-(UIImage *)resizeImage:(UIImage *)image;




@end






