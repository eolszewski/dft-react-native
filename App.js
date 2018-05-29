import React from 'react';
import { StyleSheet, Text, View, NativeModules, Button } from 'react-native';


export default class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      fingerprintData: null
    }
  }

  render() {
    return (
      <View style={styles.container}>
        <Text>Click to launch the Onyx Demo from React Native</Text>
        <Button title="Demo" onPress={() => NativeModules.DFT.navigateToDFT()}>Onyx Demo</Button>
        <Button title="Get Fingerprint" onPress={
          async () => {
            try {
              var data = await NativeModules.DFT.loadEnrolledTemplateIfExists();
              this.setState({ fingerprintData: data });
              console.log(data);
            } catch (e) {
              console.log(e.message);
            }
          }}>Get Fingerprint</Button>
        <Text numberOfLines={1}>{this.state.fingerprintData}</Text>
        <Button title="Load From State" onPress={
          () => { if (this.state.fingerprintData != null) {
            try {
              NativeModules.DFT.loadEnrolledTemplateFromData(this.state.fingerprintData);
            } catch (e) {
              console.log(e.message);
            }
          }}}>Get Fingerprint</Button>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
