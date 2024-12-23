package student.testing.system.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import stdio.lilith.annotations.AllStatesReadyToUse

@HiltAndroidApp
@AllStatesReadyToUse
class App : Application()