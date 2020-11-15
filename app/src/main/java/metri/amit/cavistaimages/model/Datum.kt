package metri.amit.cavistaimages.model

import java.io.Serializable

class Datum : Serializable {
    var id: String? = null
    var title: String? = null
    var images: List<Image>? = null
}