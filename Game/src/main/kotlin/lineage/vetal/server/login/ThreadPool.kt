package lineage.vetal.server.login

import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

// TODO add config here
class ThreadPool {
    val scheduledPools: Array<ScheduledThreadPoolExecutor> = Array(2) {
        ScheduledThreadPoolExecutor(2)
    }
    val instantPools: Array<ThreadPoolExecutor> = Array(2) {
        ThreadPoolExecutor(2, 4, 0, TimeUnit.SECONDS, ArrayBlockingQueue(100000))
    }
}