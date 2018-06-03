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