import React, {
    Component
} from 'react';
import { NativeModules } from 'react-native';
import {
    Text,
    View,
    Image,
    Dimensions,
} from 'react-native';
import { StackNavigator } from 'react-navigation';

const { height, width } = Dimensions.get('window');
export default class Welcome extends Component {
    componentDidMount() {
        setTimeout(() => {
            this.checkToken();
        }, 3000)//这里设定欢迎页时间，3s
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
                        key: 'Login',
                        type: 'ReplaceCurrentScreen',
                        routeName: 'Login',
                        params: this.props.navigation.state.params,
                    });
                }
            });
    }

    render() {
        return (
            <View>
                <Image
                    style={{ width: width, height: height }}
                    source={require('../images/welcome.jpg')}
                />
            </View>
        );
    }
}
