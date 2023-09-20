# Decrypting Mobile App Traffic Using AES Killer

## Requirements and Setup
- [Android App](https://github.com/nhott/AES-Android/blob/main/app-release.apk)
- [NodeJS](https://github.com/nhott/AES-Android/blob/main/node-server.js) Server
- Frida
- Burpsuite
- AES killer2
- Emulator

## Run API server
```shell
node node-server.js
```
![](image/run_server.png)

## Capturing the Application Traffic
Install the application using ADB or by drag and drop. Once installed, launch the application and you’ll see the following screen.

![](image/app.png)

This application is compiled with URL http://192.168.18.134 so we need to make a little change in our burp proxy to redirect all requests to our 127.0.0.1:1337.
![](image/proxy_config.png)

Now type any username and press the **Button** to send a request. The application will send an encrypted request to our local server as shown below. Also, in response getting an encrypted string.
![](image/request_encypt.png)
![](image/getRequest.png)

Now let’s move on to getting the encryption keys and understanding the encryption & decryption mechanism.

## Getting Encryption Keys using Frida

One way of getting the encryption keys is to Reverse Engineering the application and analyze its source code but if the application is obfuscated then understanding the source code becomes relatively hard. So we’ll be skipping the Reverse Engineering part as we already have application [source code](https://github.com/nhott/AES-Android/blob/main/node-server.js/my_activity.java) and below is the code segment from the encryption method.

We can see that the application is using `AES/CBC/PKCS5PADDING` encryption using Secret Key `aaaaaaaaaaaaaaaa` and IV Parameter `bbbbbbbbbbbbbbbb`
Using [frida-hook.py](https://github.com/nhott/AES-Android/blob/main/node-server.js) python script to load and execute our JS code using frida

## Hooking with Frida
Now run the python script to launch the application and execute our JS code.
```shell
	python frida-hook.py
```
![](image/run_frida.png)
Our JS Code is successfully loaded now, lets send a request by pressing the **Button**.
![](image/getInfor.png)

## Configuring AES Killer
Now that we have our Secret Key, IV Parameter and Encryption method from Frida hooking too

- Cipher - `AES/CBC/PKCS5PADDING`
    
- Secret Key - `YWFhYWFhYWFhYWFhYWFhYQ==` which is base64 of `aaaaaaaaaaaaaaaa`
    
- IV Parameter - `YmJiYmJiYmJiYmJiYmJiYg==` which is base64 of `bbbbbbbbbbbbbbbb`
    
- As the application is sending complete request body encrypted, so we’ll select `Complete Request Body` from **Request Options**
    
- The application is getting encrypted string in response, so we’ll select `Complete Response Body` from **Response Options**
    
- Host - http://127.0.0.1:1337
    
- Now Press `Start AES Killer`
![](image/AESKiler_config.png)

## Decrypting the Application Traffic using AES Killer
Upon starting AES Killer, we can observe that the Burp has started to show us decrypted traffic while Burp sending encrypted traffic to the application and server.
![](image/request_decrypt.png)