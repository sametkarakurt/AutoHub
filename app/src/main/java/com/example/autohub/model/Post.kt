import android.os.Parcel
import android.os.Parcelable

data class Post(
    val username: String?,
    val userEmail: String?,
    val brand: String?,
    val accessories: String?,
    val comment: String?,
    val downloadUrl: String?,
    val downloadUrl2: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeString(userEmail)
        parcel.writeString(brand)
        parcel.writeString(accessories)
        parcel.writeString(comment)
        parcel.writeString(downloadUrl)
        parcel.writeString(downloadUrl2)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Post> {
        override fun createFromParcel(parcel: Parcel): Post {
            return Post(parcel)
        }

        override fun newArray(size: Int): Array<Post?> {
            return arrayOfNulls(size)
        }
    }
}
