package com.example.emptycomposeactivity.network.user

@kotlinx.serialization.Serializable
data class UserData(
    val id: String,
    val nickName: String,
    val email: String,
    val avatarLink: String = "https://s3-alpha-sig.figma.com/img/a92b/ba97/a13937d71ea4ab29b068a92fd325aa74?Expires=1668988800&Signature=PMOWOcFshz~iLSRZLvi6WE~CKCngLi7WpMoR44oMZEIROtNCJjaX3UxugCdLhzxYOX~iFsQ2YEt9GAe0yCLP0l8F4W7V-Ndc-3NFxenpkVmji5IweylmDYJ7ratNHIZ6NroftMSLiaPlglIssrOl0tg0NR~xPjrWKQLqOXLp9wFuoSvIm7IAcB4vNapJnOhMRF1Q9u1Da1h5H3Cl79Btg4WaB09aF7Yrf0IonCKszUYr189k6N7nDuQ5UgL7H9VeVtzkTNu1Y0SnjWYHONqOWHe8Q~3m3jo8eoAkX2OGYpm-QWKbJFGnqCbZkZoA~w3L8WVmSI4a6OHj8k-i16OStA__&Key-Pair-Id=APKAINTVSUGEWH5XD5UA",
    val name: String,
    val birthDate: String,
    val gender: Int
)
