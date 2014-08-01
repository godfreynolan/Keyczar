Keyczar
=======

Hide API keys in Android app using Keyczar

JAR Prerequisites:
==================
•	Download the Keyczar JAR and the KeyczarTool JAR from here: https://code.google.com/p/keyczar/downloads/list (https://code.google.com/p/keyczar/downloads/detail?name=keyczar-0.71g-090613.jar, https://code.google.com/p/keyczar/downloads/detail?name=KeyczarTool-0.71g-090613.jar)

•	Download the GSON JAR from here: https://code.google.com/p/google-gson/downloads/list (https://code.google.com/p/google-gson/downloads/detail?name=google-gson-2.2.4-release.zip)

•	Download the JSS4 JAR from here: ftp://ftp.mozilla.org/pub/mozilla.org/security/jss/releases/JSS_4_3_1_RTM (ftp://ftp.mozilla.org/pub/mozilla.org/security/jss/releases/JSS_4_3_1_RTM/jss4.jar)

•	Download the Log4J JAR from here: https://logging.apache.org/log4j/1.2/download.html (http://www.apache.org/dyn/closer.cgi/logging/log4j/1.2.17/log4j-1.2.17.zip)

•	Download any additional dependencies that may have been added to this list, for your version of Keyczar: https://code.google.com/p/keyczar/wiki/JavaDependencies

•	Include all of the JARs (except KeyczarTool) in your Java project build path (you don’t need KeyczarTool in the project, but you will need it to generate keys) 

Generating the keys:
=====================
•	Navigate in a command prompt/terminal to the location where you saved the KeyczarTool JAR file

•	Read the KeyczarTool documentation here: https://code.google.com/p/keyczar/wiki/KeyczarTool

•	Create a directory to contain your private key: mkdir private

•	Run the command: java -jar KeyczarTool-0.71g-090613.jar create --location=./private --purpose=crypt --name="Key" --asymmetric=rsa

•	Run the command: java -jar KeyczarTool-0.71g-090613.jar addkey --location=./private --status=primary --size=4096

•	Create a directory to contain your public key: mkdir public

•	Run the command: java -jar KeyczarTool-0.71g-090613.jar pubkey --location=./private --destination=./public

Including Keyczar keys in your client application:
=================================================
•	Download and add AndroidKeyczarReader.java to your project from Kenny Root’s GitHub demo: https://github.com/kruton/android-keyczar-demo (https://github.com/kruton/android-keyczar-demo/blob/master/src/com/example/android/keyczardemo/AndroidKeyczarReader.java)

•	Create a folder in your project: “assets/keys” and copy in “1” and “meta” from the “public” folder where you generated the Keyczar keys

•	Initialize a Keyczar Crypter anywhere using: “Encrypter crypter = new Encrypter(new AndroidKeyczarReader(getResources(), "keys"));”

•	Encrypt text using your instance of Encrypter: “crypter.encrypt(plaintext);”

Including Keyczar keys in your server application:
=================================================
•	Create a public class that implements KeyczarReader

  o	Include an empty constructor
  
  o	 Include methods: public String getKey(), public String getKey(int arg0), public String getMetadata()

•	getKey() should return a String of the entire contents of the “1” file from the “private” folder where you generated the Keyczar keys (don’t forget to escape backslashes if hard-coding this value)

•	getKey(int arg0) should just return the output of getKey(), since we are only using one private key

•	getMetadata() should return a String of the entire contents of the “meta” file from the “private” folder where you generated the Keyczar keys (don’t forget to escape backslashes if hard-coding this value)

•	Initialize a Keyczar Crypter anywhere using: “Crypter crypter = new Crypter(new MyKeyczarReader());” (where “MyKeyczarReader” is the name of your custom class)

•	Decrypt text using your instance of Crypter: “crypter.decrypt(ciphertext);”


