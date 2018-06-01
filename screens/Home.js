import React, { Component } from 'react';
import { StyleSheet, Dimensions, TouchableOpacity, StatusBar, View, AsyncStorage, Button, Text } from 'react-native';
import { RNCamera } from 'react-native-camera';

const { width, height } = Dimensions.get('window');

class Home extends Component {

  constructor(props) {
    super(props);
  }

  static navigationOptions = (props) => {
    return {
      title: 'Picture',
      headerLeft: (<View style={styles.headerSide} />),
      headerRight: (<View style={styles.headerSide} />),
    };
  }

  render() {
    this.navigation = this.props.navigation;
    console.log('navigate: ', this.navigation);

    return (
      <View style={styles.container}>
        <RNCamera
          ref={(cam) => { this.camera = cam }}
          style={styles.preview}
          type={RNCamera.Constants.Type.front}
          flashMode={RNCamera.Constants.FlashMode.auto}
          orientation='portrait'
          permissionDialogTitle={'Permission to use camera'}
          permissionDialogMessage={'We need your permission to use your camera phone'}
        >
          <Button
            title='Take Photo'
            onPress={this.takePicture.bind(this)}
          />
        </RNCamera>
      </View>
    );
  }

  takePicture = async function () {
    if (this.camera) {
      const options = { quality: 0.5, base64: true };
      const data = await this.camera.takePictureAsync(options);
      this.navigation.navigate('Finger');
    }
  };
}

const styles = StyleSheet.create({
  headerSide: {
    width: 70,
    alignItems: 'center',
    justifyContent: 'center',
  },
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  preview: {
    flex: 1,
    justifyContent: 'flex-end',
    alignItems: 'center',
    height: Dimensions.get('window').height,
    width: Dimensions.get('window').width
  },
});

export default Home;
