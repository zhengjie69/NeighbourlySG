// src/utils/rsaUtil.js
import JSEncrypt from 'jsencrypt';

// Public key generated (replace with your actual key)
const PUBLIC_KEY = `MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq0EO2EwgfmpYEeBKcSwJ
Xz2SFrinVRW1hB5lchpz7lIT4rijD8ZlB7wTJq6pDeDlQ6OR0FIfhrvHRFsZPQTi
QT38NWsNkJR2B+PQnDXO1F//Y3oIDM/6c6oPxFlkARYSobYq+ee7clMI2QADQIFv
Ppu280NfgCJTXziFaLdsuOgl16DQRRuR6tcfem45RQcAUjPaBQ69iy/cA7p7kEmL
ZvjnNRTug98MasfeQZhY5SSQQG8SD8YK1btFZavpsm3vAmu3Z/wMXAC7ZCd4YH5B
TrP9MxRHPiPrh/U4nyos6ALyLTOWcCxpJDozHYOlCMar3ViNzKgUc+HX0AM9VHaD
oQIDAQAB`;

// Utility function for RSA encryption
const rsaEncrypt = (data) => {
  const encrypt = new JSEncrypt();
  encrypt.setPublicKey(PUBLIC_KEY);
  const encryptedData = encrypt.encrypt(data);
  return encryptedData;
};

// Export the public key and utility function
export { rsaEncrypt, PUBLIC_KEY };
