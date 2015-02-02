# Secure Electronic Messaging Protocol

**WARNING: This protocol is a work in progress. Don't base an independent implementation on it - Unless you are prepared for a re-write!**

A client or another server (both called "client" from this point on) connects to a SEMP server with a TCP connection to port 9930. They use an unencrypted connection - Every message will be encrypted individually. This has some advantages - Like, we can support forwarding of messages where the server does not know what exactly was forwarded.

A client can keep the connection to a server open. It can send requests to the server. Some requests are only available when the client has logged in. Some requests are only valid when they are sent in an encrypted envelope. The server can send information to the client as pushes.

The communication uses UTF-8 encoding.

All requests and responses are JSON objects. After every request or response JSON object, the server sends the string "<<<END-MESSAGE<<<" in a single line. Every request and every response contain a "RequestId" parameter. The "RequestId" of the response matches the one from the corresponding request.

A SEMP server must support the following commands:

## GetServerInfo

The client requests the public keys and other information of the server. This request and its response are never sent in an encrypted envelope.

There is only one current public key, all other public keys have an "ObsoleteBy" field that contains the key id of the next public key. Every public key (except the first) must be signed with the previous key. The client should check if the key signatures are consistent. The client should also check if the keys are consistent with any previously saved server keys.

The public verification keys can only be used to verify the signatures of other keys. The client must not encrypt any data with them. The public encryption keys can be used to encrypt data that is sent to the server.

The client can (and should) ask his "own" server for public keys of other servers.

**Client:**
```
{
    "RequestId": /* A unique (within this session) identifier for this request. */
    "GetServerInfo": "semp.example.com"
}
```

**Server:**
```
{
    "RequestId": /* The request id from the corresponding request */
    "ServerPublicVerificationKeys": [
        {
            "KeyId": /* Unique Identifier for this key. */,
            "PublicKey": /* BASE64 encoded public key specification. */
            "ObsoleteBy": /* The "KeyId" of the key that followed this key. */
        },
        {
            "KeyId": /* Unique Identifier for this key. Must be equal to "ObsoleteBy" of the previous key. */,
            "PublicKey": /* BASE64 encoded public key specification. */
            "PreviousKeyId": /* The "KeyId" of the previous server public key. */
            "Signature": /* The signature of this key, signed with the previous key. */
            "ObsoleteBy": /* The "KeyId" of the key that followed this key. */
        },
        {
            "KeyId": /* Unique Identifier for this key. Must be equal to "ObsoleteBy" of the previous key. */,
            "PublicKey": /* BASE64 encoded public key specification. */
            "PreviousKeyId": /* The "KeyId" of the previous server public key. */
            "Signature": /* The signature of this key, signed with the previous key. */
        }
    ]
    "ServerPublicEncryptionKey": {
        "KeyId": /* Unique Identifier for this key. */
        "PublicKey": /* BASE64 encoded public key specification. */
        "Signature": /* The signature of this key, signed with the current verification key. */
        "VerificationKeyId": /* The key id of the verification key used to sign the current public key. This must be the current verification key. */
    }
}
```

## InEncryptedEnvelope

The client sends a request to the server in an encrypted envelope. The server decrypts the envelope, extracts the request and processes the request. It sends the response of the request as payload in another encrypted message.

With every encrypted envelope, the server and the client create a new key for the next encrypted envelope. They use a diffie-hellman key exchange to generate this new key. By doing this, the guarantee "Perfect Forward Security".

**Client:**
```
{
    "RequestId": /* A unique (within this session) identifier for this request. */
    "InEncryptedEnvelope": {
        "KeyId": /* Unique Identifier for the key used for encrypting the session key. References either the server public key or a key agreed by both parties with diffie-hellman. */
        "EncryptedSessionKey": /* The encrypted AES256 session key */
        "EncryptedData": /* The rest of the data is encrypted with "PBEWithSHA256And256BitAES-CBC-BC" and encoded in BASE64. */
    }
}
```

**Where "EncryptedData" is:**
```
{
    "NextKeyParameters": { 
        /* This are the parameters for the next diffie-hellman key exchange. This key will be used for the next encrypted envelope. Optional - Only used when the client expects that there will be a longer conversation. */
    }
    "Payload": /* A request to the server. */
}
```

**Server:**
```
{
    "RequestId": /* The request id from the corresponding request */
    "EncryptedData": /* The response, encrypted with the session key from the corresponding request. */
}
```

**Where "EncryptedData" is:**
```
{
    "NextKeyParameters": {
        /* This are the parameters for the next diffie-hellman key exchange. This key will be used for the next encrypted envelope. */
    }
    "NextKeyId": /* A unique identifier for this next key. The client can use this identifier as "KeyId" in the next encrypted envelope. */
    "Payload": /* The response to the nested request. */
}
```

## RequestLogin

**Only allowed in an encrypted envelope**

The client requests to log in to the server as an actual user. The server sends the stored and password-encrypted private key to client. It also sends a challenge that will prove that the client was able to decrypt the private key. The client decryptes the private key (using a password entered by the user) and solves the challenge. It sends the result back to the server and is now logged in.

For the challenge, the server creates a random string of up to 256 characters and encrypts it with the public key of the user. It sends this encrypted string to the client. The client has to decrypt this string with the private key and send it back to the server. This proves that the client was able to correctly decrypt the private key.

**Client:**
```
{
    "RequestLogin": "example@example.com"
}
```

TK
