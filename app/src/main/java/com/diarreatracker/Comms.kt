package com.diarreatracker

import android.util.Log
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket
import java.security.KeyFactory
import java.security.PublicKey
import java.security.SecureRandom
import java.security.Security
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class Comms {
    init {
        Security.addProvider(BouncyCastleProvider())
    }

    private val serverAddress = "192.168.100.141"
    private val serverPort = 12346
    private lateinit var uuid : String
    private val aesKey = generateAESKey()
    fun authenticate(hashedEmail: String, hashedPass: String): Int{
        try {
            val client = Socket(serverAddress, serverPort)
            //Send auth request to server
            val outputStream = client.getOutputStream()
            val writer = BufferedWriter(OutputStreamWriter(outputStream))
            writer.write(uuid + encryptAES("Auth;${hashedEmail};${hashedPass}", aesKey))
            writer.flush()

            val inputStream = client.getInputStream()
            val reader = BufferedReader(InputStreamReader(inputStream))
            val encryptedMessage = reader.readLine()

            writer.write("Disconnect")
            writer.flush()
            client.close()

            return decryptAES(encryptedMessage, aesKey).toInt()
        } catch (e: Exception) {
            e.printStackTrace()
            return -1
        }
    }

    fun initContact(){ //Works perfectly. Please do not mess with this
        try {
            val client = Socket(serverAddress, serverPort)
            //Send auth request to server
            val outputStream = client.getOutputStream()
            val writer = BufferedWriter(OutputStreamWriter(outputStream))
            val rsa = getRSAKey()
            writer.write(
                encryptRSA("AES", rsa)
            )
            writer.flush()

            writer.write(encryptRSA(aesKey, rsa))
            writer.flush()

            val inputStream = client.getInputStream()
            val reader = BufferedReader(InputStreamReader(inputStream))
            uuid = reader.readLine()



            client.close()

        }catch (e: Exception){
            e.printStackTrace()
        }

    }



    fun getPoop(id: String, dateLow: String = "0", dateHigh: String = "99999999", poopScoreLow: String = "0", poopScoreHigh: String = "9", orderBy: String = "DateOfShit", order: String = "DESC"): MutableSet<List<String>>? {
        var idLow = id
        var idHigh = id
        if (id == "All"){
            idLow = "0"
            idHigh= "${Int.MAX_VALUE}"
        }
        val command = "Poop;$idLow;$idHigh;$dateLow;$dateHigh;$poopScoreLow;$poopScoreHigh;$orderBy;$order"
        return handleResponseBasic(command)
    }

    fun getInfo(id: String): MutableSet<List<String>>? {
        var idLow = id
        var idHigh = id
        if (id == "All"){
            idLow = "0"
            idHigh= "${Int.MAX_VALUE}"
        }
        val command = "Info;$idLow;$idHigh"
        return handleResponseBasic(command)
    }

    fun getNameSuggestion(dog: String): List<String>? {
        val command = "Name;$dog"
        val response = sendCommand(command) ?: return null

        return response.split(";")
    }

    fun getVetAppointments(id: String, dateLow: String = "0", dateHigh: String = "99999999", notes: String = "", orderBy: String = "DateOfVetAppointment", order: String = "ASC"): MutableSet<List<String>>?{

        var idLow = id
        var idHigh = id
        if (id == "All"){
            idLow = "0"
            idHigh= "${Int.MAX_VALUE}"
        }
        val command = "VetA;$idLow;$idHigh;$dateLow;$dateHigh;$notes;$orderBy;$order"

        return handleResponseBasic(command)
    }
    private fun getRSAKey(): PublicKey? { //Made this when drunk. Effectively black magic fuckery
        try {
            val client = Socket(serverAddress,serverPort)
            val outputStream = client.getOutputStream()
            val writer = BufferedWriter(OutputStreamWriter(outputStream))
            writer.write("RSA")
            writer.flush()
            BufferedReader(InputStreamReader(client.getInputStream())).use {
                reader ->
                val publicKeyBuilder = StringBuilder()

                var line: String?
                while (reader.readLine().also { line = it } != null){
                    publicKeyBuilder.append(line).append("\n")
                }


                val publicKeyPem = publicKeyBuilder.toString()

                val pK = publicKeyPem
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replace("\\s".toRegex(), "")
                    .replace("""\n""", "")

                client.close()
                return publicinputToKey(pK)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("RSA", "ERROR")

            return null
        }
    }

    private fun publicinputToKey(keyRecieved : String): PublicKey?{
        val decoded: ByteArray = Base64.getDecoder().decode(keyRecieved)
        val keySpec = X509EncodedKeySpec(decoded)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(keySpec)
    }

    private fun encryptRSA(message: String, publicKey: PublicKey?): String{
        val cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val encryptedBytes = cipher.doFinal(message.toByteArray(Charsets.UTF_8))
        return Base64.getEncoder().encodeToString(encryptedBytes)
    }

    private fun generateAESKey(): String{
        val keyGen = KeyGenerator.getInstance("AES")
        keyGen.init(256)
        val key = keyGen.generateKey()

        return Base64.getEncoder().encodeToString(key.encoded)
    }


    private fun encryptAES(message: String, key: String): String{
        try {
            val decodedKey = Base64.getDecoder().decode(key)
            val secretKey: SecretKey = SecretKeySpec(decodedKey, 0, decodedKey.size, "AES")

            val iv = ByteArray(16)
            SecureRandom().nextBytes(iv)
            val ivSpec = IvParameterSpec(iv)

            val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)

            val encryptedBytes = cipher.doFinal(message.toByteArray(Charsets.UTF_8))
            val encryptedMessageWithIV = iv + encryptedBytes
            return Base64.getEncoder().encodeToString(encryptedMessageWithIV)
        } catch (e: Exception){
            e.printStackTrace()
            return ""
        }
    }

    private fun decryptAES(encryptedMessage: String, key: String):String{

        val decodedKey = Base64.getDecoder().decode(key)
        val secretKey: SecretKey = SecretKeySpec(decodedKey, 0, decodedKey.size, "AES")

        val encryptedDataWithIV = Base64.getDecoder().decode(encryptedMessage)

        //Remove IV
        val iv = encryptedDataWithIV.sliceArray(0 until 16)
        val ivSpec = IvParameterSpec(iv)

        val ciphertext = encryptedDataWithIV.sliceArray(16 until encryptedDataWithIV.size)

        val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)


        val decryptedBytes = cipher.doFinal(ciphertext)
        return String(decryptedBytes, Charsets.UTF_8)
    }

    private fun sendCommand(command: String): String? {
        try {
            val client = Socket(serverAddress, serverPort)
            val outputStream = client.getOutputStream()
            val writer = BufferedWriter(OutputStreamWriter(outputStream))
            val inputStream = client.getInputStream()
            val reader = BufferedReader(InputStreamReader(inputStream))

            // Send command with AES encryption
            writer.write(uuid + encryptAES(command, aesKey))
            writer.flush()

            // Read encrypted response from server
            val encryptedMessage = reader.readLine()

            // Send disconnect signal and close socket
            writer.write("Disconnect")
            writer.flush()
            client.close()

            // Decrypt and return the response
            return decryptAES(encryptedMessage, aesKey)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun handleResponseBasic(command: String): MutableSet<List<String>>? {
        val response = sendCommand(command) ?: return null
        val splitMessage = response.split(";")
        val newMessage = mutableSetOf<List<String>>()
        for (part in splitMessage) {
            val new = part.split(",")
            newMessage.add(new)
        }

        return newMessage
    }
}
