package features.auth.data

data class UserModel(
    val id: String,
    val role: Role?,
    val location: String?,
    val carNumberPlate: String? = null,
    val password: String? = null,
    val name: String? = null
)

enum class Role {
    NORMAL, ADMIN
}