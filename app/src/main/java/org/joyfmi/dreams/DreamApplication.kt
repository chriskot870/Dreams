package org.joyfmi.dreams

import android.app.Application
import org.joyfmi.dreams.repository.DreamRepository

class DreamApplication : Application() {

    val repository: DreamRepository by lazy { DreamRepository.getRepository(this)}
}