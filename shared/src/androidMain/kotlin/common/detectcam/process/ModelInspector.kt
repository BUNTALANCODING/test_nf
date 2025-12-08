package common.detectcam.process

import android.util.Log
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter

data class OutputInfo(
    val index: Int,
    val name: String,
    val shape: IntArray,
    val dataType: DataType
)

enum class ModelKind {
    DETECTOR,
    CLASSIFIER,
    UNKNOWN
}

data class ModelInfo(
    val kind: ModelKind,
    val detectionHead: OutputInfo? = null,
    val classificationHead: OutputInfo? = null
)

fun Interpreter.getAllOutputInfos(): List<OutputInfo> {
    val list = mutableListOf<OutputInfo>()
    for (i in 0 until outputTensorCount) {
        val t = getOutputTensor(i)
        list.add(
            OutputInfo(
                index = i,
                name = t.name(),
                shape = t.shape(),
                dataType = t.dataType()
            )
        )
    }
    return list
}

fun Interpreter.inspectModel(): ModelInfo {
    val outputs = getAllOutputInfos()

    Log.d(
        "ModelInspector",
        "outputs=" + outputs.joinToString {
            "idx=${it.index}, name=${it.name}, shape=${it.shape.contentToString()}, type=${it.dataType}"
        }
    )

    val det = outputs.firstOrNull { info ->
        val s = info.shape
        s.size == 3 &&
                s[0] == 1 &&
                s[2] >= 4
    }

    if (det != null) {
        return ModelInfo(
            kind = ModelKind.DETECTOR,
            detectionHead = det
        )
    }

    val cls = outputs.firstOrNull { info ->
        val s = info.shape
        s.size == 2 &&
                s[0] == 1 &&
                s[1] >= 2
    }

    if (cls != null) {
        return ModelInfo(
            kind = ModelKind.CLASSIFIER,
            classificationHead = cls
        )
    }

    return ModelInfo(kind = ModelKind.UNKNOWN)
}
