//
//  TrainViewController.m
//  TrackMyTrain
//
//  Created by Ponnie Rohith on 23/05/15.
//  Copyright (c) 2015 PR. All rights reserved.
//

#import "TrainViewController.h"
#import "HTAutocompleteManager.h"
#import <Parse/Parse.h>
#import "StationsDataSource.h"
#import "Reminder.h"


@interface TrainViewController () < HTAutocompleteTextFieldDelegate , HTAutocompleteDataSource,UITextFieldDelegate >
@property (strong, nonatomic) NSArray *autocompleteList;
@property BOOL isOnBoard;

@end

@implementation TrainViewController

- (instancetype)initWithOnBoardFlag:(BOOL)isOnBoard
{
    self = [super init];
    if (self) {
        self.isOnBoard = isOnBoard;
    }
    return self;
}
- (void)viewDidLoad {
    [super viewDidLoad];
    self.trainNumTF.delegate = self;
    [HTAutocompleteTextField setDefaultAutocompleteDataSource:[HTAutocompleteManager sharedManager]];
    self.stationTF.autocompleteType = HTAutocompleteTypeStation;
    self.stationTF.showAutocompleteButton = YES;
    self.stationTF.keyboardType = UIKeyboardTypeDefault;
    // Dismiss the keyboard when the user taps outside of a text field
    UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleSingleTap:)];
    [self.view addGestureRecognizer:singleTap];
    
    [self setReminder];

}
-(void)textFieldDidBeginEditing:(UITextField *)textField
{
//    if (textField == self.trainNumTF ) {
//        NSMutableArray *autocomplete = [[NSMutableArray alloc]init];
//        [autocomplete removeAllObjects];
//        for(NSString *curString in self.autocompleteList) {
//            NSRange substringRange = [curString rangeOfString:textField.text];
//            if (substringRange.location == 0) {
//                [autocomplete addObject:curString];
//            }
//        }
//        textField.text = [autocomplete firstObject];
//    }
}
- (void)handleSingleTap:(UITapGestureRecognizer *)sender
{
    self.stationTF.text = @"";
    [self.stationTF resignFirstResponder];
}
- (void)scheduleNotificationForDate:(NSDate *)date
{
    // Here we cancel all previously scheduled notifications
    [[UIApplication sharedApplication] cancelAllLocalNotifications];
    
    UILocalNotification *localNotification = [[UILocalNotification alloc] init];
    
    localNotification.fireDate = date;
    NSLog(@"Notification will be shown on: %@",localNotification.fireDate);
    
    localNotification.timeZone = [NSTimeZone defaultTimeZone];
    localNotification.alertBody = [NSString stringWithFormat:@"Your train has reached!"];
    localNotification.alertAction = NSLocalizedString(@"View details", nil);
    
    /* Here we set notification sound and badge on the app's icon "-1"
     means that number indicator on the badge will be decreased by one
     - so there will be no badge on the icon */
    localNotification.soundName = UILocalNotificationDefaultSoundName;
//    localNotification.applicationIconBadgeNumber = -1;
    
    [[UIApplication sharedApplication] scheduleLocalNotification:localNotification];
}

-(void)setReminder
{
    // Setting all the information about our date
    int hour = 12;
    int minutes = 25;
    int day = 23;
    int month = 5;
    int year = 2015;
    
    NSCalendar *calendar = [[NSCalendar alloc] initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
    NSDateComponents *components = [[NSDateComponents alloc] init];
    
    [components setDay:day];
    [components setMonth:month];
    [components setYear:year];
    [components setMinute:minutes];
    [components setHour:hour];
    [components setSecond:0];
    
    NSDate *myNewDate = [calendar dateFromComponents:components];

    [self scheduleNotificationForDate:myNewDate];
    
}
- (IBAction)setReminder:(id)sender {

    PFObject *reminder = [PFObject objectWithClassName:@"Reminder"];
    NSNumber *num = [NSNumber numberWithInteger:self.dateControl.selectedSegmentIndex];
    reminder[@"day"] = num;
    reminder[@"deviceId"] = [UIDevice currentDevice].identifierForVendor.UUIDString;
    reminder[@"status"] = 0;
    if (self.isOnBoard) {
        reminder[@"isOnBoard"] = @"true";
    }
    else{
        reminder[@"isOnBoard"] = @"false";
    }
    reminder[@"stationCode"] = [StationsDataSource stationCodeFromString:self.stationTF.text];
    reminder[@"trainNumber"] = self.trainNumTF.text;
    reminder[@"query"] = [NSString stringWithFormat:@"%@-%@",self.trainNumTF.text,[self.dateControl titleForSegmentAtIndex:self.dateControl.selectedSegmentIndex]];
    [reminder saveInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
        if (succeeded) {
            NSLog(@"The object has been saved.");
            //Reminder *reminderData = [Reminder ]
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@""
                                                            message:@"Alarm has been set"
                                                           delegate:self
                                                  cancelButtonTitle:@"OK"
                                                  otherButtonTitles:nil];
            [alert show];

        } else {
            NSLog(@"There was a problem, check error.description");
        }
    }];
}
//-(NSString*)textField:(HTAutocompleteTextField *)textField completionForPrefix:(NSString *)prefix ignoreCase:(BOOL)ignoreCase
//{
//    return @"";
//}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
