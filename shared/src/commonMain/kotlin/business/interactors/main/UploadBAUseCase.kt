package business.interactors.main

import business.core.DataState
import business.core.UIComponent
import business.datasource.network.main.MainService
import business.datasource.network.main.responses.ChunkResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UploadChunkUseCase(
    private val mainService: MainService
) {
    fun execute(
        token: String,
        fileName: String,
        uniqueKey: String,
        chunkIndex: Int,
        totalChunks: Int,
        chunk: ByteArray,
    ): Flow<DataState<ChunkResponse>> = flow {

        emit(DataState.Loading())

        try {
            val res = mainService.uploadChunkFile(
                token = token,
                fileName = fileName,
                uniqueKey = uniqueKey,
                chunkIndex = chunkIndex,
                totalChunks = totalChunks,
                chunk = chunk
            )

            emit(
                DataState.Data(
                    data = res,
                    status = res.status,
                    code = res.code
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            emit(
                DataState.Response(
                    UIComponent.DialogSimple(
                        title = "Upload chunk gagal",
                        description = e.message ?: "Terjadi kesalahan"
                    )
                )
            )
        }
    }
}
