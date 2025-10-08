package common

actual fun getImageSaveShare(): ImageSaveShare {

    return AndroidImageSaveShareImpl(context = ContextProvider.context)
}