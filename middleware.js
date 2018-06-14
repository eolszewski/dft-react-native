const EthCrypto = require('eth-crypto');
// const web3 = require('web3');

export const sendCode = async (phone) => {
  const response = await fetch('https://us-central1-everid-44197.cloudfunctions.net/sendCode', {
    method: 'POST',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      phone: phone
    }),
  });

  if (!response.ok) {
    throw new Error('Failed to send code');
  }

  return response.json();
}

export const testSigning = async () => {
  const lowercaseAddress = "0xDbc23AE43a150ff8884B02Cea117b22D1c3b9796".toLowerCase();
  console.log('lowercaseAddress: ', lowercaseAddress);
  const payload = `{"birth_date":"2018-06-01T16:10:45.780Z","birth_place":"CA","child1_name":"Frank","child2_name":"Bob","child3_name":"Timmy","eth_address":"${lowercaseAddress}","father_name":"Jason","first_name":"Wendy","last_name":"Matthews","mother_name":"Wendy","nationality":"USA","phone_number":"+17138258982","photo":"QmWDdAuytJnMQqzUDL82p4JvJWjHX9HYqBC3jLjPNKjsKm","sex":"male","sibling1_name":"Barbara","sibling2_name":"Emily","sibling3_name":"Jan"}`
  console.log(payload);
  const private_key = "000000000000000000000000000000000000000000000000000000000000000c";

  const messageHash = EthCrypto.hash.keccak256("\x19Ethereum Signed Message:\n" + payload.length + payload);

  console.log('messageHash: ', messageHash);

  const signature = EthCrypto.sign(private_key, messageHash);
  // const signature2 = elliptic.ec('secp256k1').sign(messageHash, private_key, 'hex', { canonical: true })

  console.log(signature);
  // console.log(signature2);
}

export const verifyCode = async (phone, code) => {
  const response = await fetch('https://us-central1-everid-44197.cloudfunctions.net/verifyPhone', {
    method: 'POST',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      phone: phone,
      code: code,
    }),
  });

  if (!response.ok) {
    throw new Error('Invalid Code');
  }

  return response.json();
}

export const sendTransactionNotification = async (fromName, toAddress, amount, currency) => {
  const response = await fetch('https://us-central1-everid-44197.cloudfunctions.net/sendTransactionNotification', {
    method: 'POST',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      fromName: fromName,
      toAddress: toAddress,
      amount: amount,
      currency: currency
    }),
  });

  if (!response.ok) {
    throw new Error('Failed to notify user');
  }

  return response.json();
}

export const createUser = async (phone, face, PIN, wallet, privateKey) => {
  const response = await fetch('https://us-central1-everid-44197.cloudfunctions.net/createUser', {
    method: 'POST',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      phone,
      face,
      PIN,
      wallet,
      privateKey
    }),
  });

  if (!response.ok) {
    throw new Error('Failed to create user');
  }

  return response.json();
}

export const getUser = async (phone, face, PIN) => {
  const response = await fetch('https://us-central1-everid-44197.cloudfunctions.net/getUser', {
    method: 'POST',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      phone,
      face,
      PIN
    }),
  });

  if (!response.ok) {
    throw new Error('Invalid PIN');
  }

  return response.json();
}

export const updateUser = async (phone, info) => {
  const response = await fetch('https://us-central1-everid-44197.cloudfunctions.net/updateUser', {
    method: 'POST',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      phone,
      info,
    }),
  });

  if (!response.ok) {
    throw new Error('Failed to update user');
  }

  return response.ok;
}

export const uploadFace = async (ID, image) => {
  const response = await fetch("https://us-central1-everid-44197.cloudfunctions.net/uploadImage", {
    method: "POST",
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json"
    },
    body: JSON.stringify({
      image,
      ID
    })
  }
  );

  if (!response.ok) {
    throw new Error("Failed to upload image");
  }

  return response.ok;
};

export const getFingerprint = async (phone) => {
  const response = await fetch("https://us-central1-everid-44197.cloudfunctions.net/getFingerprint", {
    method: "POST",
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json"
    },
    body: JSON.stringify({
      phone
    })
  }
  );

  if (!response.ok) {
    throw new Error("Failed to get fingerprint");
  }

  return response.json();
};

// export const uploadFingerprint = async (phone, data) => {
//   const response = await fetch("https://us-central1-everid-44197.cloudfunctions.net/uploadFingerprint", {
//     method: "POST",
//     headers: {
//       Accept: "application/json",
//       "Content-Type": "application/json"
//     },
//     body: JSON.stringify({
//       phone,
//       data
//     })
//   }
//   );

//   console.log('response: ', response);
//   if (!response.ok) {
//     throw new Error("Failed to save fingerprint");
//   }

//   return response.json();
// };

export const uploadFingerprint = async (ID, image) => {
  const response = await fetch(
    "https://us-central1-everid-44197.cloudfunctions.net/uploadFingerprint",
    {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        image,
        ID
      })
    }
  );

  if (!response.ok) {
    throw new Error("Failed to upload image");
  }

  return response.ok;
};