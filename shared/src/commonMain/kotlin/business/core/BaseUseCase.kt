package business.core

import business.constants.DataStoreKeys
import business.datasource.network.common.JAlertResponse
import business.datasource.network.common.MainGenericResponse
import business.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import logger.Logger

abstract class BaseUseCase<Params, ApiResponse, Result>(
    private val appDataStoreManager: AppDataStore? = null // Optional
) {

    abstract suspend fun run(params: Params, token: String): MainGenericResponse<ApiResponse>?

    abstract fun mapApiResponse(apiResponse: MainGenericResponse<ApiResponse>?): Result?

    abstract val progressBarType: ProgressBarState // Default progress bar type
    abstract val needNetworkState: Boolean  // Whether to emit network states
    abstract val createException: Boolean // Check if we need to to throw exception
    abstract val checkToken: Boolean // Check token
    abstract val showDialog: Boolean

    fun execute(params: Params): Flow<DataState<Result>> = flow {
        try {

            // Emit loading state
            emit(DataState.Loading(progressBarType))

            // Retrieve token if AppDataStore is available
            val token = appDataStoreManager?.readValue(DataStoreKeys.TOKEN)

            if (checkToken) {
                requireNotNull(token) { "Token is required" }
            }

            // Execute core logic
            val result = run(params, token ?: "")

            // Optionally emit network state
            if (needNetworkState) {
                emit(DataState.NetworkStatus(NetworkState.Good))
            }

            if (showDialog) {
                result?.let { (status, code, message, result, alert) ->
                    if (status == false) {
                        message?.let { m ->
                            emit(
                                DataState.Response(
                                    uiComponent = UIComponent.Dialog(
                                        alert = JAlertResponse(title = if (status) "Success" else "Error", message = m),
                                        status = status
                                    )
                                )
                            )
                        }
                    }
                }
            }

            emit(DataState.Data(data = mapApiResponse(result), status = result?.status, code = result?.code))
        } catch (e: Exception) {

            if (createException) {
                /*emit(
                    DataState.Response(
                        uiComponent = UIComponent.Dialog(
                            alert = JAlertResponse(title = "Exception", message = e.toString()),
                            status = false
                        )
                    )
                )*/
                Logger.e(e.toString())
            }

            // Optionally emit network failure state
            if (needNetworkState) {
                emit(DataState.NetworkStatus(NetworkState.Failed))
            }

            // Handle exceptions
            e.printStackTrace()
            emit(handleUseCaseException(e))
        } finally {
            // Emit idle loading state
            emit(DataState.Loading(ProgressBarState.Idle))
        }
    }

}
