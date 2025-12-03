package com.example.appmilsabores.sync

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.appmilsabores.AppMilSaboresApplication
import com.example.appmilsabores.data.sync.RemoteToLocalSync
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RemoteSyncReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("SyncReceiver", "Received broadcast to trigger remote sync")
        val db = AppMilSaboresApplication.database
        CoroutineScope(Dispatchers.IO).launch {
            val ok = RemoteToLocalSync.syncProducts(db, context)
            Log.d("SyncReceiver", "Remote sync finished. success=$ok")
        }
    }
}
