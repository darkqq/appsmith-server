package com.appsmith.server.configurations.annotation;

import com.appsmith.server.exceptions.AppsmithError;
import com.appsmith.server.exceptions.AppsmithException;

public class AccessRestrictedException extends AppsmithException {
    public AccessRestrictedException(AppsmithError error, Object... args) {
        super(error, args);
    }
}
