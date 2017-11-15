/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bob;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import javax.swing.JOptionPane;

/**
 *
 * @author falcu
 */
public class Bob {

    private File publickKey;
    private File signature;
    private File data;

    public void setPublicKey(File publicKey) {
        this.publickKey = publicKey;
    }

    public void setSignature(File signature) {
        this.signature = signature;
    }

    public void setData(File data) {
        this.data = data;
    }

    public void verify() {

        
        if (publickKey == null) {
            JOptionPane.showMessageDialog(null, "You didn't select a public key File!");
        } else if (signature == null) {
            JOptionPane.showMessageDialog(null, "You didn't select a signature File!");
        } else if (data == null) {
            JOptionPane.showMessageDialog(null, "You didn't select a data File!");
        } else {
            try {
                
                /* Importovanje enkodovanog javnog kluca */ 
                FileInputStream keyfis = new FileInputStream(publickKey);
                byte[] encKey = new byte[keyfis.available()];
                keyfis.read(encKey);

                keyfis.close();

                X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
                KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
                PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
                    
                
                /* Input bajtova potpisa */
                FileInputStream sigfis = new FileInputStream(signature);
                byte[] sigToVerify = new byte[sigfis.available()];
                sigfis.read(sigToVerify);
                sigfis.close();
                              
                /* Kreiranje Potpisa i inicijalizacije istog sa javnim kljucem */ 
                Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
                sig.initVerify(pubKey);
                
                /* Azuriranje i verifikacija podataka */
                FileInputStream datafis = new FileInputStream(data);
                BufferedInputStream bufin = new BufferedInputStream(datafis);

                byte[] buffer = new byte[1024];
                int len;
                while (bufin.available() != 0) {
                    len = bufin.read(buffer);
                    sig.update(buffer, 0, len);
                };

                bufin.close();
                System.out.println("Public key is: " + pubKey.toString());

                boolean verifies = sig.verify(sigToVerify);

                if (verifies) {
                    System.out.println("The file was not tampered with!");
                    JOptionPane.showMessageDialog(null, "The File is autenthic!");
                } else {
                    System.out.println("The file was tampered with! ");
                    JOptionPane.showMessageDialog(null, "The File was tampered with!");
                }

                
            } catch (Exception e) {
                System.err.println("Caught exception " + e.toString());
            }
        }
    }
}
