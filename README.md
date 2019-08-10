Forked from https://github.com/chemerisuk/cordova-plugin-firebase-crash

Sends a JavaScript error stack trace to the Crashlytics logError() method.

### logError(_message_, _stack_)
Log non-fatal exceptions in addition to automatically reported app crashes. [Firebase documentation](https://firebase.google.com/docs/crashlytics/customize-crash-reports?authuser=0#log_non-fatal_exceptions).

The _stack_ parameter must be a string consisting of lines separated by '\r', each of which corresponds to one frame of the stack trace in the form: 'className;methodName;fileName;lineNum;columnNum;isEval'. 

Example:
```js
var stack = 'Foo;bar;src/dir/file.js;10;5;0';
```

p.s. You can use the "error-stack-parser" library to get the JavaScript call stack.

```js
cordova.plugins.firebase.crashlytics.logError("my non-fatal exception message", stack);
```
