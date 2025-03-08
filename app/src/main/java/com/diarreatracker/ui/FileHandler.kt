package com.diarreatracker.ui
import android.content.Context
import com.diarreatracker.ui.component.DogItemView
import com.diarreatracker.ui.component.ViewState
import org.json.JSONArray
import org.json.JSONObject
import java.io.File


class FileHandler(private val context: Context) {


    private val fileName = "data.json"

    // Save a list of com.diarreatracker.ui.component.ViewState objects as a JSON array
    fun saveViewStates(viewStates: List<ViewState>) {
        try {
            val jsonArray = JSONArray()

            // Convert each com.diarreatracker.ui.component.ViewState into a JSONObject and add it to the JSON array
            viewStates.forEach { viewState ->
                val jsonObject = JSONObject().apply {
                    put("x", viewState.x)
                    put("y", viewState.y)
                    put("width", viewState.width)
                    put("height", viewState.height)
                    put("id", viewState.id)
                    put("rowName", viewState.rowName)
                    put("scaleX", viewState.scaleX)
                    put("scaleY", viewState.scaleY)
                    put("dogs", JSONArray().apply {
                        viewState.dogs.forEach { dog ->
                            put(JSONObject().apply {
                                put("dogname", dog.dogname)
                                put("gender", dog.gender)
                                put("dateOfBirth", dog.dateOfBirth)
                                put("castrationText", dog.castrationText)
                                put("bodyScore", dog.bodyScore)
                                put("heat", dog.heat)
                            })
                        }
                    })
                }


                jsonArray.put(jsonObject)
            }

            // Write JSON array to file
            val file = File(context.filesDir, fileName)
            file.writeText(jsonArray.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Load a list of com.diarreatracker.ui.component.ViewState objects from a JSON array
    fun loadViewStates(): List<ViewState> {
        val viewStates = mutableListOf<ViewState>()

        try {
            val file = File(context.filesDir, fileName)
            if (file.exists()) {
                val content = file.readText()
                val jsonArray = JSONArray(content)

                // Convert each JSONObject in the JSON array back into a com.diarreatracker.ui.component.ViewState object
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)

                    val dogsArray: JSONArray = jsonObject.getJSONArray("dogs")
                    val dogList = mutableListOf<DogItemView>()

                    for (item in 0 until dogsArray.length()){
                        val dogObject = dogsArray.getJSONObject(item)
                        val dog = DogItemView(
                            dogname = dogObject.getString("dogname"),
                            gender = dogObject.getBoolean("gender"),
                            dateOfBirth = dogObject.getString("dateOfBirth"),
                            castrationText = dogObject.getString("castrationText"),
                            bodyScore = dogObject.getString("bodyScore"),
                            heat = dogObject.getBoolean("heat")
                        )
                        dogList.add(dog)
                    }


                    val viewState = ViewState(
                        x = jsonObject.getDouble("x").toFloat(),
                        y = jsonObject.getDouble("y").toFloat(),
                        width = jsonObject.getInt("width"),
                        height = jsonObject.getInt("height"),
                        id = jsonObject.getInt("id"),
                        rowName = jsonObject.getString("rowName"),
                        scaleX = jsonObject.getDouble("scaleX").toFloat(),
                        scaleY = jsonObject.getDouble("scaleY").toFloat(),
                        dogs = dogList
                    )
                    viewStates.add(viewState)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return viewStates
    }
}
