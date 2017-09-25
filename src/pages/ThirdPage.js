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

let iconPath = '';//模块图标路径
var REQUEST_URL = '';
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
        NativeModules.NativeManager.getServerUrl()
        .then((back) => {
            REQUEST_URL = back+"/getData/1";
            console.log(REQUEST_URL);
        })
        .then(() => {
            this._fetch(REQUEST_URL, 5000)
            .then((info)=> {
                console.log("yes");
                this.loadRemoteData();
            }).catch((err)=> {
                console.log("error");
                this.loadLoaclData();
                // throw new Error(err);
            });
        });
        NativeModules.NativeManager.getIconPath((back) => {
            iconPath = back;
            console.log(iconPath);
        });
    }

    componentWillUnmount() {
        this.timer && clearTimeout(this.timer);
    }

     // 超时版的fetch
    _fetch(url, timeout) {
        return Promise.race([
            fetch(url),
            new Promise(function (resolve, reject) {
                setTimeout(() => reject(new Error('request timeout')), timeout);
            })
        ]);
    }

    /**
     * 
     *  加载本地模块
     * 
     * @memberof ThirdPage
     */
    loadLoaclData(){
        NativeModules.NativeManager.getLocalData()
        .then((back) => {
            var bundleData = back.split('{');
            var DATA = [];
            for (var i=1; i<bundleData.length; i++){
                var info = bundleData[i].split(',');
                var id = info[0].split(':')[1];
                var name = info[1].split(':')[1];
                var data = {
                    id,
                    name
                }
                DATA.push(data);
            }
            this.setState({
                dataSource: this.state.dataSource.cloneWithRows(DATA),
                loaded: true,
            });
        });
    }

    /**
     * 加载远程模块并验证
     * 
     * @memberof ThirdPage
     */
    loadRemoteData(){
         NativeModules.NativeManager.downloadIcon((back) => {
            if(back != null){
                iconPath = back;
            }
        });
        this.fetchData();
        this.timer = setTimeout( () => {
                this.mainUpdate();
            },
            2000
        );
    }

    fetchData() {
        fetch(REQUEST_URL)
            .then((response) => response.json())
            .then((responseData) => {
                this.setState({
                    dataSource: this.state.dataSource.cloneWithRows(responseData),
                    loaded: true,
                });
                console.log("*********");
                console.log(responseData);
            });
    }

    mainUpdate () {
        NativeModules.NativeManager.checkMainUpdateAble( (back) => {
            if(back == "netError"){
                Alert.alert("网络或服务器出错，请重启软件再尝试！");
            }else if( back != null ){
                Alert.alert(
                    "是否更新主模块？",
                    back,
                    [
                        {
                            text: '是',
                            onPress: () => {
                                NativeModules.NativeManager.updateMain((type, result) => {
                                    if (type == "success") {
                                        //ToastAndroid.show(result, ToastAndroid.SHORT);
                                    } else {
                                        ToastAndroid.show(result, ToastAndroid.SHORT);
                                    }
                                });
                            }
                        },
                        {
                            text:'否'
                        }
                    ]
                    );
            }
        });
    }

    render() {
        if (!this.state.loaded) {
            return this.renderLoadingView();
        }

        return (
            <View>
                {/*<TouchableHighlight onPress={this.mainUpdate.bind(this)}>
                    <Text>判断主模块更新</Text>
                </TouchableHighlight>*/}
                {/* <Text>更新之后的主模块！</Text> */}
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
        /* if(icon.isMain == "1"){
            return ;
        } */
        const iconUri = 'file://'+iconPath+icon.name+'.png';
        return (
            <TouchableWithoutFeedback onPress={() => this._onIconClick(icon.name, icon.id)}>
                <View style={styles.container}>
                    <Image
                        source={{ uri: iconUri }}
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