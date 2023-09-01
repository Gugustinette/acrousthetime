package fr.iutna.lpmiar.td1.batch

import org.springframework.beans.factory.annotation.Autowired

// RunBatch Class
class RunBatch() {
    // Main to run ImportICS.importIcs()
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            ImportICS().importIcs()
        }
    }
}
