'use strict';
import React, {
    Component,
} from 'react';

import {
    AppRegistry,
    View,
    WebView,
    Dimensions,
    StyleSheet,
} from 'react-native';

const url = 'http://mobile-cloud.cloud.saas.hand-china.com';
// const url = 'http://baidu.com';
const { width, height } = Dimensions.get('window');

export default class H5Screen extends Component {
    render() {
        return (
            <View style={styles.container}>
                <WebView
                    style={{ width: width, height: height - 20, backgroundColor: 'gray' }}
                    source={{ uri: url, method: 'GET' }}
                    javaScriptEnabled={true}
                    domStorageEnabled={true}
                    scalesPageToFit={false}
                />
            </View>
        )
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
    },
})

AppRegistry.registerComponent('MobileCloud', () => H5Screen);