//
//  StationsDataSource.h
//  TrackMyTrain
//
//  Created by Ponnie Rohith on 23/05/15.
//  Copyright (c) 2015 PR. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface StationsDataSource : NSObject

+(NSArray*)stationList;
+(NSString*)stationCodeFromString:(NSString*)name;
+(NSString*)stationNameFromCode:(NSString*)code;

@end
