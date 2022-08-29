package com.github.bogdanovmn.memorydeluge.viewer.web.app.user;

class RegistrationInviteException extends RegistrationException {
    RegistrationInviteException(String msg) {
        super("inviteCode", msg);
    }
}
