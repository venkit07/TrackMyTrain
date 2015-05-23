//
//  TrainViewController.h
//  TrackMyTrain
//
//  Created by Ponnie Rohith on 23/05/15.
//  Copyright (c) 2015 PR. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HTAutocompleteTextField.h"

@interface TrainViewController : UIViewController
@property (strong, nonatomic) IBOutlet UITextField *trainNumTF;
@property (strong, nonatomic) IBOutlet UISegmentedControl *dateControl;

@property (unsafe_unretained, nonatomic) IBOutlet HTAutocompleteTextField *stationTF;


@end
