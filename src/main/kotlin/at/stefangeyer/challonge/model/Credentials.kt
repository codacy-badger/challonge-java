package at.stefangeyer.challonge.model

import org.apache.commons.codec.binary.Base64

/**
 * Challonge credentials containing username and api-key.
 *
 * @author Stefan Geyer
 * @version 2018-06-30
 */
class Credentials(private val username: String, private val apiKey: String) {
    /**
     * Creates a HTTP basic auth String from the given credentials
     *
     * @return HTTP basic auth String
     */
    fun toHttpAuthString(): String {
        val credentials = this.username + ":" + this.apiKey
        return "Basic " + Base64.encodeBase64String(credentials.toByteArray())
    }
}
