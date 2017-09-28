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

     //脚本注入
    injectJS = () => {
        const script = `alert("开始");
                    function get(a){
                            var arr,reg=new RegExp("(^| )"+a+"=([^;]*)(;|$)"); 
                            if(arr=document.cookie.match(reg)) 
                                return unescape(arr[2]); 
                            else 
                                return null;
                        } 
                        var data = null;
                        var t = setInterval(function(){
                            data = get('access_token');
                            if(data != null){
                              clearInterval(t);
                              window.postMessage(data);
                            }
                          }, 2000);`;
                          
        if (this.webview) {
            this.webview.injectJavaScript(script);
        }
    }
    render() {
        return (
            <View style={styles.container}>
                <WebView
                    ref={(webview) => this.webview = webview}
                    onLoadEnd = {this.injectJS}
                    style={{ width: width, height: height - 20, backgroundColor: 'gray' }}
                    source={{ uri: url, method: 'GET' }}
                    javaScriptEnabled={true}
                    domStorageEnabled={false}
                    scalesPageToFit={false}
                    onMessage = {(e) => {
                      alert(e.nativeEvent.data);
                      console.log(e.nativeEvent.data);
                    }}
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