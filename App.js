import React from 'react';
import {
  StyleSheet, Text, View, NativeEventEmitter, NativeModules, Button
} from 'react-native';
import RNFS from 'react-native-fs';
import { uploadFingerprint } from './middleware';


export default class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      fingerprintData: null
    }
  }

  uploadDocument = async function () {
    var exists = await NativeModules.Onyx.enrolledTemplateFileExists();
    if (exists) {
      var path = await NativeModules.Onyx.loadEnrolledTemplateFileName();
      const image = await new Promise((resolve, reject) => {
        RNFS.readFile(path, "base64")
          .then(data => resolve(data))
          .catch(err => reject(err));
      });

      await uploadFingerprint('+17138258982-fingerprint', image);
    }
  }

  downloadDocument = async function () {
    var path = await NativeModules.Onyx.loadEnrolledTemplateFileName();
    RNFS.downloadFile({ fromUrl: 'https://storage.googleapis.com/everid/+17138258982-fingerprint.bin', toFile: path }).promise.then(res => {
      if (res.statusCode == 200) {
        this.setState({ downloaded: true });
      }
    });
  }

  

  render() {
    return (
      <View style={styles.container}>
        <Text>Click to launch the Onyx Demo from React Native</Text>
        <Button title="Demo" onPress={() => NativeModules.Onyx.navigateToOnyx()}>Onyx Demo</Button>
        <Button title="Get Fingerprint" onPress={
          async () => {
            try {
              var data = await NativeModules.Onyx.loadEnrolledTemplateIfExists();
              this.setState({ fingerprintData: data });
              console.log(data);
            } catch (e) {
              console.log(e.message);
            }
          }}>Onyx Demo</Button>
        <Button title="Upload" onPress={this.uploadDocument.bind(this)}>Upload</Button>
        <Button title="Download" onPress={this.downloadDocument.bind(this)}>Download</Button>
        <Button title="Load" onPress={() => NativeModules.Onyx.loadEnrolledTemplateFromData(this.state.fingerprintData)}>Load</Button>
        <Button title="Delete Saved" onPress={() => NativeModules.Onyx.deleteEnrolledTemplateIfExists()}>Delete Saved</Button>
      </View>
    );
  }
}

const eventEmitter = new NativeEventEmitter(NativeModules.Onyx);
eventEmitter.addListener(NativeModules.Onyx.MyEventName, (params) => console.log(params));

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
