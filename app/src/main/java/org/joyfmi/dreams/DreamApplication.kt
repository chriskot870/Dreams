package org.joyfmi.dreams

import android.app.Application
import android.graphics.Color
import org.joyfmi.dreams.repository.DreamRepository

class DreamApplication : Application() {

    companion object {
        var localTextColor: Int = Color.BLUE
        var commonTextColor: Int = Color.BLACK
    }

    val repository: DreamRepository by lazy { DreamRepository.getRepository(this)}
}