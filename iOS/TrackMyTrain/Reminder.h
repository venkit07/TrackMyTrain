//
//  Reminder.h
//  TrackMyTrain
//
//  Created by Ponnie Rohith on 23/05/15.
//  Copyright (c) 2015 PR. All rights reserved.
//

#import <CoreData/CoreData.h>

@interface Reminder : NSManagedObject

@property (strong, nonatomic) NSString *date;
@property (strong, nonatomic) NSString *station;
@property (strong, nonatomic) NSNumber *trainNumber;

@end
