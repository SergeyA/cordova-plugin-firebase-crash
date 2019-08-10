#import "FirebaseCrashPlugin.h"
#import <Fabric/Fabric.h>
#import <Crashlytics/Crashlytics.h>

@implementation FirebaseCrashPlugin

- (void)pluginInitialize {
    NSLog(@"Starting Firebase Crash plugin");
}

- (void)log:(CDVInvokedUrlCommand *)command {
    [self.commandDelegate runInBackground:^{
        NSString* errorMessage = [command.arguments objectAtIndex:0];
        CLSNSLog(@"%@", errorMessage);
        CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)logError:(CDVInvokedUrlCommand*)command {
    NSString *description = NSLocalizedString([command argumentAtIndex:0 withDefault:@"No Message Provided"], nil);

    NSString *bundleId = [[NSBundle mainBundle] bundleIdentifier];
    NSString *stack = NSLocalizedString([command argumentAtIndex:1 withDefault:bundleId], nil);

    NSDictionary *userInfo = @{ NSLocalizedDescriptionKey: description };

    NSNumber *defaultCode = [NSNumber numberWithInt:-1];
    int code = [[command argumentAtIndex:2 withDefault:defaultCode] intValue];

    NSError *error = [NSError errorWithDomain: stack code: code userInfo: userInfo];

    [[Crashlytics sharedInstance] recordError:error];
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)setUserId:(CDVInvokedUrlCommand *)command {
    NSString* userId = [command.arguments objectAtIndex:0];

    [CrashlyticsKit setUserIdentifier:userId];
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end
