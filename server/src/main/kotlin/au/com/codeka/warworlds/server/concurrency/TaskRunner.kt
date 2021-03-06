package au.com.codeka.warworlds.server.concurrency

import java.util.*

/**
 * This is a class for running tasks on various threads. You can run a task on any thread defined
 * in [Threads].
 */
class TaskRunner private constructor() {
  companion object {
    var i = TaskRunner()
  }

  private val timer: Timer
  fun runTask(runnable: Runnable, thread: Threads) {
    thread.runTask(runnable)
  }

  /** Run a task after the given delay.  */
  fun runTask(runnable: Runnable, thread: Threads, delayMs: Long) {
    if (delayMs == 0L) {
      runTask(runnable, thread)
    } else {
      timer.schedule(object : TimerTask() {
        override fun run() {
          runTask(runnable, thread)
        }
      }, delayMs)
    }
  }

  init {
    val backgroundThreadPool = ThreadPool(
        Threads.BACKGROUND,
        2500 /* maxQueuedItems */,
        10 /* minThreads */,
        50 /* maxThreads */,
        5000 /* keepAliveMs */)
    Threads.BACKGROUND.setThreadPool(backgroundThreadPool)
    timer = Timer("Timer")
  }
}