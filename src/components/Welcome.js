import React, {
    Component
} from 'react';
import { NativeModules } from 'react-native';
import {
    Text,
    View,
} from 'react-native';
import { StackNavigator } from 'react-navigation';

export default class Welcome extends Component {



    componentDidMount() {
        setTimeout(() => {
            this.checkToken();
        }, 5000)
    }

    checkToken() {
        NativeModules.NativeManager.getConfigData()
            .then((back) => {
                token = back['token'];
                console.log(token);
            })
            .then(() => {
                if (token.length > 0) {
                    this.props.navigation.dispatch({
                        key: 'MainScreen',
                        type: 'ReplaceCurrentScreen',
                        routeName: 'MainScreen',
                        params: this.props.navigation.state.params,
                    });
                } else {
                    this.props.navigation.dispatch({
                        key: 'StackNavigatorSecond',
                        type: 'ReplaceCurrentScreen',
                        routeName: 'StackNavigatorSecond',
                        params: this.props.navigation.state.params,
                    });
                }
            });
    }

    go() {
        this.props.navigation.navigate('MainScreen');
    }

    render() {
        return (
            <View>
                <Text>Welcome</Text>
            </View>
        );
    }
}
