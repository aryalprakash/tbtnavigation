//
//  TbtNavigation.m
//  TbtNavigation
//
//  Created by Hamza El Yousfi on 11/14/18.
//  Copyright © 2018 Facebook. All rights reserved.
//

#import "React/RCTBridgeModule.h"
@interface RCT_EXTERN_REMAP_MODULE(MapBoxDirections, TbtNavigation, NSObject)

RCT_EXTERN_METHOD(startNavigation: (NSDictionary *)options)

@end

