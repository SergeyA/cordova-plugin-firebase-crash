var exec = require("cordova/exec");
var PLUGIN_NAME = "FirebaseCrash";

module.exports = {
    log: function(message) {
        return new Promise(function(resolve, reject) {
            exec(resolve, reject, PLUGIN_NAME, "log", [message]);
        });
    },
    logError: function(message, stack) {
        return new Promise(function(resolve, reject) {
            exec(resolve, reject, PLUGIN_NAME, "logError", [message, stack]);
        });
    },
    setUserId: function(userId) {
        return new Promise(function(resolve, reject) {
            exec(resolve, reject, PLUGIN_NAME, "setUserId", [userId]);
        });
    }
};
