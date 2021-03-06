import React, { Component } from 'react';
import {
    Text,
    View,
    StyleSheet,
    TouchableOpacity,
    WebView,
    Dimensions,
} from 'react-native';
import { NativeModules } from 'react-native';
import { StackNavigator } from 'react-navigation';

const url = 'http://mobile-cloud.cloud.saas.hand-china.com';
// const url = 'http://baidu.com';
const { width, height } = Dimensions.get('window');

export default class Login extends Component {

    //脚本注入
    injectJS = () => {
        const script = `
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
                          }, 100);`;

        if (this.webview) {
            this.webview.injectJavaScript(script);
        }
    }
    render() {
        return (
            <View style={styles.container}>
                <WebView
                    ref={(webview) => this.webview = webview}
                    onLoadEnd={this.injectJS}
                    style={{ width: width, height: height - 20, backgroundColor: 'gray' }}
                    source={{ uri: url, method: 'GET' }}
                    javaScriptEnabled={true}
                    domStorageEnabled={false}
                    scalesPageToFit={false}
                    onMessage={(e) => {
                        this.props.navigation.dispatch({
                            key: 'MainScreen',
                            type: 'ReplaceCurrentScreen',
                            routeName: 'MainScreen',
                            params: this.props.navigation.state.params,
                        });
                        console.log(e.nativeEvent.data);
                        NativeModules.NativeManager.setToken(e.nativeEvent.data);
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
