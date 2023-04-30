package com.focuslearning.example.kafka.crypto;

import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JceCMSContentEncryptorBuilder;
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipient;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientInfoGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OutputEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collection;

@Component
public class CipherUtil {

    Logger logger = LoggerFactory.getLogger(CipherUtil.class);

    X509Certificate certificate =  null;
    PrivateKey key = null;

    @PostConstruct
    public void init() throws CertificateException, NoSuchProviderException,
            IOException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, CMSException {
        Security.setProperty("crypto.policy", "unlimited");
        int maxKeySize = 0;
        try {
            maxKeySize = javax.crypto.Cipher.getMaxAllowedKeyLength("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        logger.info("Max Key Size for AES : [{}]" , maxKeySize);
        Security.addProvider(new BouncyCastleProvider());
        CertificateFactory certificateFactory =  CertificateFactory.getInstance("X.509", "BC");
        this.certificate = (X509Certificate) certificateFactory
                .generateCertificate(new FileInputStream("C:\\Users\\91982\\IdeaProjects\\kafka-core\\application\\src\\main\\resources\\Baeldung.cer"));
        char[] keystorePassword = "password".toCharArray();
        char[] keyPassword = "password".toCharArray();
        KeyStore keystore = KeyStore.getInstance("PKCS12");
        keystore.load(new FileInputStream("C:\\Users\\91982\\IdeaProjects\\kafka-core\\application\\src\\main\\resources\\Baeldung.p12"), keystorePassword);
        this.key = (PrivateKey) keystore.getKey("baeldung", keyPassword);
        logger.info("Private Key {}",this.key);
        byte[]  encryptedData = encryptData("anurag".getBytes(StandardCharsets.UTF_8));
        logger.info("Encrypted data [{}]",new String(encryptedData));
        byte[]  decryptedData = decryptData(encryptedData);
        logger.info("Decrypted data [{}]",new String(decryptedData));
    }


    public byte[] encryptData(byte[] data)
            throws CertificateEncodingException, CMSException, IOException {

        byte[] encryptedData = null;
        if (null != data && null != this.certificate) {
            CMSEnvelopedDataGenerator cmsEnvelopedDataGenerator
                    = new CMSEnvelopedDataGenerator();

            JceKeyTransRecipientInfoGenerator jceKey
                    = new JceKeyTransRecipientInfoGenerator(this.certificate);
            cmsEnvelopedDataGenerator.addRecipientInfoGenerator(jceKey);
            CMSTypedData msg = new CMSProcessableByteArray(data);
            OutputEncryptor encryptor
                    = new JceCMSContentEncryptorBuilder(CMSAlgorithm.AES128_CBC)
                    .setProvider("BC").build();
            CMSEnvelopedData cmsEnvelopedData = cmsEnvelopedDataGenerator
                    .generate(msg,encryptor);
            encryptedData = cmsEnvelopedData.getEncoded();
        }
        return encryptedData;
    }

    public byte[] decryptData(
            byte[] encryptedData)
            throws CMSException {

        byte[] decryptedData = null;
        if (null != encryptedData && null != this.key) {
            CMSEnvelopedData envelopedData = new CMSEnvelopedData(encryptedData);

            Collection<RecipientInformation> recipients
                    = envelopedData.getRecipientInfos().getRecipients();
            KeyTransRecipientInformation recipientInfo
                    = (KeyTransRecipientInformation) recipients.iterator().next();
            JceKeyTransRecipient recipient
                    = new JceKeyTransEnvelopedRecipient(this.key);

            return recipientInfo.getContent(recipient);
        }
        return decryptedData;
    }

    public static void main(String[] args) throws UnrecoverableKeyException, CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, NoSuchProviderException, CMSException {
        CipherUtil cipherUtil = new CipherUtil();
        cipherUtil.init();
        byte[] encryptedBytes  = cipherUtil.encryptData("1234567".getBytes(StandardCharsets.UTF_8));
        byte[] decryptedBytes =  cipherUtil.decryptData(encryptedBytes);
        System.out.println(new String(decryptedBytes));

    }
}
