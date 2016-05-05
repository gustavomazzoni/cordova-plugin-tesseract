//
//  claseAuxiliar.h
//  pruebaTesseract
//
//  Created by Admin on 03/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
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






