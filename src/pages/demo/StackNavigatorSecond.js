import React, { Component } from 'react';
import {
    Text,
    View,
    StyleSheet,
    TouchableOpacity,
} from 'react-native';
import { StackNavigator } from 'react-navigation';

let next;

export default class StackNavigatorSecond extends Component {

    componentDidMount() {
        next = new Date();
    }


    render() {
        const { navigate } = this.props.navigation;
        return (
            <View
                style={styles.container}>
                <TouchableOpacity
                    onPress={() => {
                        var dateNow = new Date();
                        if (dateNow.getTime() - next.getTime() > 1000) {
                            next = dateNow;
                            navigate('StackNavigatorThird');
                        }
                    }
                    }
                >
                    <Text>Modal</Text>
                </TouchableOpacity>
            </View>
        );
    }

}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#F1F1F1',
    },
});