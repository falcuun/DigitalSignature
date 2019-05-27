# DigitalSignature
  ### Purpose
    - This project is used to digitally a file, and after that check the same file if it has been tempered with or not.
  ### How It Works
    - User Signing the file (Alice) chooses file to sign, using provided GUI form, and hits sign button.
    - The program then uses a set of public and private key and an encrypting algorithm to sign the file.
    - It Generates Public Key file, for later verification.
    
    - User Verifying the file (Bob) chooses file to verify and chooses Public key file that came with it.
    - The Program then uses the set of keys and decrypting algorithm to veryfy the integrity of the file.
