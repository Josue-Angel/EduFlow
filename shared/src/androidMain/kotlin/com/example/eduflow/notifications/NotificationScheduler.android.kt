package com.example.eduflow.notifications

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

actual object NotificationScheduler {

    // 15 minutos es el mínimo que WorkManager permite para trabajo periódico,
    // y encima no garantiza que la primera corrida sea inmediata. Por eso
    // lanzamos también un chequeo único al momento (abajo) para que el
    // usuario no espere hasta 15 min para ver su primera notificación.
    actual fun iniciar() {
        val restricciones = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val chequeoInmediato = OneTimeWorkRequestBuilder<NotificacionesSyncWorker>()
            .setConstraints(restricciones)
            .build()

        WorkManager.getInstance(AppContextHolder.appContext)
            .enqueueUniqueWork(
                "${NotificacionesSyncWorker.NOMBRE_TRABAJO}_inmediato",
                ExistingWorkPolicy.REPLACE,
                chequeoInmediato
            )

        val solicitud = PeriodicWorkRequestBuilder<NotificacionesSyncWorker>(
            15, TimeUnit.MINUTES
        ).setConstraints(restricciones).build()

        WorkManager.getInstance(AppContextHolder.appContext)
            .enqueueUniquePeriodicWork(
                NotificacionesSyncWorker.NOMBRE_TRABAJO,
                ExistingPeriodicWorkPolicy.KEEP,
                solicitud
            )
    }

    actual fun detener() {
        WorkManager.getInstance(AppContextHolder.appContext)
            .cancelUniqueWork(NotificacionesSyncWorker.NOMBRE_TRABAJO)
    }
}
