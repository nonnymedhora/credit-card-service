# The below scripts show the commands used to generate the self-signed keys for this sample.
# If you use the below script to create your own keys be sure to change the passwords used here
# DO NOT USE THE SUPPLIED KEYS IN PRODUCTION--everyone has them!!
# For production recommended to use keys signed by a third-party certificate authority (CA)

# Create the combination keystore/truststore for the service.
# Note you can create separate keystores/truststores for both if desired
keytool -genkeypair -validity 730 -alias myservicekey -keystore serviceKeystore.jks -dname "cn=localhost" -keypass skpass -storepass sspass

# Place server public cert in client key/truststore
keytool -export -rfc -keystore serviceKeystore.jks -alias myservicekey -file MyService.cer -storepass sspass
keytool -import -noprompt -trustcacerts -file MyService.cer -alias myservicekey -keystore clientKeystore.jks -storepass cspass

