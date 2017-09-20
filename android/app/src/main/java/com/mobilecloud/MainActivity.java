package com.mobilecloud;

import com.mobilecloud.ext.ExtReactActivity;

public class MainActivity extends ExtReactActivity {


    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "MobileCloud";
    }


}
