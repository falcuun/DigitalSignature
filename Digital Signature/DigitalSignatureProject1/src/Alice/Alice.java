/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Alice;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import javax.swing.JOptionPane;

/**
 *
 * @author falcu
 */
public class Alice {

    private File f = null;

    public void setFile(File f) {
        this.f = f;
    }

    public void sign() {

        if (f == null) {
            JOptionPane.showMessageDialog(null, "You didn't select a file to sign!");
        } else {
            boolean bool = false;
            try {
                //  f = new File("data");
                bool = f.createNewFile();
                System.out.println("File Created " + bool);
            } catch (Exception e) {

            }
            try {

                /* Generisanje kljuceva */
                KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
                SecureRandom random = SecureRandom.getInstanceStrong();//("SHA1PRNG", "SUN");
                // Duzina od 1024 bita
                keyGen.initialize(1024, random);
                KeyPair pair = keyGen.generateKeyPair();
                // Privatni kljuc
                PrivateKey priv = pair.getPrivate();
                // Javni kljuc
                PublicKey pub = pair.getPublic();

                System.out.println("Private key is: " + priv.toString() + " Public key is: " + pub.toString());


                /* Create a Signature object and initialize it with the private key */
                Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");

                dsa.initSign(priv);

                /* Update and sign the data */
                FileInputStream fis = new FileInputStream(f);
                BufferedInputStream bufin = new BufferedInputStream(fis);
                byte[] buffer = new byte[1024];
                int len;
                while (bufin.available() != 0) {
                    len = bufin.read(buffer);
                    dsa.update(buffer, 0, len);
                };

                bufin.close();

                /* Now that all the data to be signed has been read in, 
                    generate a signature for it */
                byte[] realSig = dsa.sign();

                /* Save the signature in a file */
                FileOutputStream sigfos = new FileOutputStream("Signature");
                sigfos.write(realSig);

                sigfos.close();


                /* Save the public key in a file */
                byte[] key = pub.getEncoded();
                FileOutputStream keyfos = new FileOutputStream("PublicKey");
                keyfos.write(key);

                keyfos.close();
                
                JOptionPane.showMessageDialog(null, "The File was successfully signed!");
            } catch (Exception e) {
                System.err.println("Caught exception " + e.toString());
            }
        }
    }
}
