package common

actual fun getImageSaveShare(): ImageSaveShare {
    return IOSImageSaveShareImpl()
}