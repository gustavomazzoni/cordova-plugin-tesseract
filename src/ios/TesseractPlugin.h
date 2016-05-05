//
//  TesseractPlugin.h
//  pruebaTesseract
//
//  Created by Admin on 09/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//
#import <Cordova/CDV.h>
@class claseAuxiliar;

@interface TesseractPlugin : CDVPlugin 

@property (nonatomic, copy) NSString* callbackID;

- (void) recognizeText:(CDVInvokedUrlCommand*)command;

@end
