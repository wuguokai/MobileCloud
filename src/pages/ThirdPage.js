'use strict';
import React, {
    Component,
} from 'react';
import { NativeModules } from 'react-native';
import {
    AppRegistry,
    Image,
    ListView,
    StyleSheet,
    Text,
    View,
    Alert,
    TouchableWithoutFeedback,
    RefreshControl,
    Dimensions,
    ToastAndroid,
    TouchableHighlight
} from 'react-native';

var REQUEST_URL = 'http://10.211.97.242:8378/v1/bundle/getData/1';
var width = Dimensions.get('window').width;
export default class ThirdPage extends Component {

    constructor(props) {
        super(props);
        this.state = {
            dataSource: new ListView.DataSource({
                rowHasChanged: (row1, row2) => row1 !== row2,
            }),
            loaded: false,
        };

        this.fetchData = this.fetchData.bind(this);
    }

    componentDidMount() {
        this.fetchData();
    }

    fetchData() {
        fetch(REQUEST_URL)
            .then((response) => response.json())
            .then((responseData) => {
                this.setState({
                    dataSource: this.state.dataSource.cloneWithRows(responseData),
                    loaded: true,
                });
                console.log(responseData);
            });
    }

    mainUpdate () {
        NativeModules.NativeManager.checkMainUpdateAble( (back) => {
            if( back != null ){
                Alert.alert("主模块有更新"+back);
            }
        });
    }

    render() {
        if (!this.state.loaded) {
            return this.renderLoadingView();
        }

        return (
            <View>
                <TouchableHighlight onPress={this.mainUpdate.bind(this)}>
                    <Text>判断主模块更新</Text>
                </TouchableHighlight>
                <Text style={styles.title}>可用模块</Text>
                <ListView
                    dataSource={this.state.dataSource}
                    renderRow={this.renderIcon.bind(this)}
                    contentContainerStyle={styles.listView}
                />
            </View>
        );
    }

    renderLoadingView() {
        return (
            <View style={styles.containerLoading}>
                <Text>
                    Loading...
                </Text>
            </View>
        );
    }

    _onIconClick(name, id) {
        this.show(name, id);
    }

    show(name, id) {
        NativeModules.NativeManager.openBundle(name, (type) => {
            let title = ""
            if (type == "netError") {
                Alert.alert("网络或服务器出错，请重启软件再尝试！");
            }else{
                if (type == "update") {
                    title = "发现新版本,是否升级?"
                } else {
                    title = "未安装应用，是否安装？"
                }
                Alert.alert(
                    title,
                    title,
                    [
                        {
                            text: '是',
                            onPress: () => {
                                NativeModules.NativeManager.downloadAndOpenBundle(name, id, (type, result) => {
                                    if (type == "netError") {
                                        Alert.alert("网络或服务器出错，请重启软件再尝试！");
                                    } else if (type == "success") {
                                        //ToastAndroid.show(result, ToastAndroid.SHORT);
                                    } else {
                                        ToastAndroid.show(result, ToastAndroid.SHORT);
                                    }
                                });
                            }
                        },
                        {
                            text: '否'
                        }
                    ]
                );
            }
        });
    }

    renderIcon(icon) {
        return (
            <TouchableWithoutFeedback onPress={() => this._onIconClick(icon.name, icon.id)}>
                <View style={styles.container}>
                    <Image
                        source={{ uri: icon.iconPath }}
                        style={styles.thumbnail}
                    />
                    <View style={styles.rightContainer}>
                        <Text style={styles.year}>{icon.name}</Text>
                    </View>
                </View>
            </TouchableWithoutFeedback>
        );
    }
}

var styles = StyleSheet.create({
    containerLoading: {
        flex: 1,
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
    },
    container: {
        justifyContent: 'center',
        width: width / 4,
        height: 80,
        alignItems: 'center',
    },
    rightContainer: {
        flex: 1,
    },
    title: {
        textAlign: 'left',
        fontSize: 18,
        marginTop: 10,
        marginLeft: 10,
    },
    year: {
        textAlign: 'center',
        fontSize: 12,
    },
    thumbnail: {
        width: 50,
        height: 50,
        borderRadius: 5,
    },
    listView: {
        marginTop: 10,
        justifyContent: 'flex-start',
        flexDirection: 'row',
        flexWrap: 'wrap'
    },
});