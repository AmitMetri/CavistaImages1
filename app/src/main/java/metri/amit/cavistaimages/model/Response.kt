package metri.amit.cavistaimages.model

import java.io.Serializable

class Response : Serializable {
    var data: List<Datum>? = null
    var success: Boolean? = null
    var status: Int? = null
}