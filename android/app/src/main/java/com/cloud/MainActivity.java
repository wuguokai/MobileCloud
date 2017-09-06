package com.cloud;

import com.cloud.ext.ExtReactActivity;

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
